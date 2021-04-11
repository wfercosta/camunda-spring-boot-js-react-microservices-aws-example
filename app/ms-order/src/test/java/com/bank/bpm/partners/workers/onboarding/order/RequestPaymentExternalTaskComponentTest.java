package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Sql(value = "/db/migrations/h2/data/load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/migrations/h2/data/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
		initializers = {WireMockInitializer.class},
		classes = {TestApplication.class}
)
@Tag(TestType.COMPONENT)
public class RequestPaymentExternalTaskComponentTest {

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCapture;

	@Autowired
	private RequestPaymentExternalTask sut;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WireMockServer server;

	@Autowired
	private OrderRepository orderRepository;


	@Test
	@DisplayName("Should return correlation id When payment request received successfully")
	public void Should_return_correlation_When_payment_request_received_successfully() throws JsonProcessingException {

		//Arrange
		final Long orderId = 7L;
		final String correlationId = UUID.randomUUID().toString();
		final Order order = orderRepository.findById(orderId).orElse(null);

		when(externalTask.getVariable("order")).thenReturn(objectMapper.writeValueAsString(order));

		server.stubFor(
				post(urlEqualTo("/v1/payments/payment"))
						.willReturn(
								aResponse()
										.withStatus(HttpStatus.CREATED.value())
										.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
										.withBody(objectMapper.writeValueAsString(CreatePaymentResponse.builder()
												.correlationId(correlationId)
												.build()))
						)
		);

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assertions
		verify(externalTaskService).complete(eq(externalTask), variablesCapture.capture());

		final Map<String, Object> captured = variablesCapture.getValue();
		assertThat(captured, notNullValue());
		assertThat(captured, hasEntry("payment_correlation_id", correlationId));

		final Order updated = orderRepository.findById(orderId).orElse(null);
		assertThat(updated, notNullValue());
		assertThat(updated.getStatus(), is(OrderStatus.ORDER_PENDING_PAYMENT));

	}

}

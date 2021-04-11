package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
		initializers = {WireMockInitializer.class},
		classes = {TestApplication.class}
)
@Tag(TestType.INTEGRATION)
public class PaymentApiClientTest {

	@Autowired
	private WireMockServer server;

	@Autowired
	private PaymentApiClient sut;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}


	@Test
	@DisplayName("Should return correlation When payment request created successfully")
	public void Should_return_correlation_When_payment_request_created_successfully() throws JsonProcessingException {

		final Order order = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);

		server.stubFor(
				post(urlEqualTo("/v1/payments/payment"))
						.willReturn(
								aResponse()
										.withStatus(HttpStatus.CREATED.value())
										.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
										.withBody(objectMapper.writeValueAsString(
												CreatePaymentResponse
														.builder()
														.correlationId(UUID.randomUUID().toString())
														.build())
										)
						)
		);


		CreatePaymentResponse result = sut.create(CreatePaymentRequest.from(order));

		assertThat(result, notNullValue());
		assertThat(result.getCorrelationId(), notNullValue());
	}
}

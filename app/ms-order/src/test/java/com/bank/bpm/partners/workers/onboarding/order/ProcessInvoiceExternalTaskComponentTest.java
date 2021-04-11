package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
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
public class ProcessInvoiceExternalTaskComponentTest {

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCapture;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProcessInvoiceExternalTask sut;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@DisplayName("Should return invoiced created and status as invoiced")
	public void Should_return_invoiced_created_and_status_invoiced() throws JsonProcessingException {

		//Arrange
		final Order order = orderRepository.findById(6L).orElse(null);

		when(externalTask.getVariable("order"))
				.thenReturn(objectMapper.writeValueAsString(requireNonNull(order)));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assertions
		verify(externalTaskService).complete(eq(externalTask), variablesCapture.capture());

		final Map<String, Object> captured = variablesCapture.getValue();

		assertThat(captured, hasKey("invoice"));
		assertThat(captured, hasKey("order"));

		final Order updated = objectMapper.readValue((String) captured.get("order"), Order.class);

		assertThat(updated.getStatus(), is(OrderStatus.ORDER_INVOICED));
	}
}

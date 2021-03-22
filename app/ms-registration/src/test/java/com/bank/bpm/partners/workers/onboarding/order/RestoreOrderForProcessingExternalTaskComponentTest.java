package com.bank.bpm.partners.workers.onboarding.order;


import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.DisplayName;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
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

public class RestoreOrderForProcessingExternalTaskComponentTest {

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCapture;

	@Autowired
	private RestoreOrderForProcessingExternalTask sut;

	@Test
	@DisplayName("Should restore a order When order id is valid and its state is new")
	public void Should_restore_order_When_order_valid_and_its_state_new() {
		//Arrange
		final Long orderId = 1L;
		when(externalTask.getVariable("order_id")).thenReturn(orderId);

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assertion
		verify(externalTaskService).complete(eq(externalTask), variablesCapture.capture());
		final Map<String, Object> captured = variablesCapture.getValue();

		assertThat(captured, hasKey("order"));
	}

}

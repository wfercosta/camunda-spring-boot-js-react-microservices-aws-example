package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RequestPaymentExternalTaskTest {


	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Mock
	private RequestPaymentExternalUseCase useCase;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCapture;

	private ObjectMapper objectMapper = new ObjectMapper();

	private RequestPaymentExternalTask sut;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new RequestPaymentExternalTask(objectMapper, useCase);
	}

	@Test
	@DisplayName("Should return the payment identification When it received successfully")
	public void Should_return_payment_id_When_received_successfully() throws JsonProcessingException {

		//Arrange
		final Order order = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);
		final String correlationId = UUID.randomUUID().toString();

		when(externalTask.getVariable("order")).thenReturn(objectMapper.writeValueAsString(order));
		when(useCase.execute(order)).thenReturn(Optional.of(correlationId));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Asserts
		verify(useCase).execute(eq(order));
		verify(externalTaskService).complete(eq(externalTask), variablesCapture.capture());

		final Map<String, Object> captured = variablesCapture.getValue();

		assertThat(captured, hasEntry("payment_correlation_id", correlationId));

	}

}

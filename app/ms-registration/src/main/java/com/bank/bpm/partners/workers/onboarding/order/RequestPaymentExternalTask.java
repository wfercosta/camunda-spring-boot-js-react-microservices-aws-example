package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.Map;
import java.util.Optional;

@ExternalTaskController(topic = "order_request_payment")
public class RequestPaymentExternalTask implements ExternalTaskHandler {
	private final ObjectMapper objectMapper;
	private final RequestPaymentExternalUseCase useCase;

	public RequestPaymentExternalTask(final ObjectMapper objectMapper, final RequestPaymentExternalUseCase useCase) {
		this.objectMapper = objectMapper;
		this.useCase = useCase;
	}

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		final String jsonOder = externalTask.getVariable("order");
		final Order order = objectMapper.readValue(jsonOder, Order.class);

		Optional<String> optional = useCase.execute(order);

		externalTaskService.complete(externalTask, Map.of("payment_correlation_id", optional.get()));

	}
}

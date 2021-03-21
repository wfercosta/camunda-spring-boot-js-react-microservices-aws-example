package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.Map;

@ExternalTaskController(topic = "order_restore")
public class RestoreOrderForProcessingExternalTask implements ExternalTaskHandler {

	private final ObjectMapper objectMapper;
	private final RestoreOderUseCase useCase;

	public RestoreOrderForProcessingExternalTask(final ObjectMapper objectMapper, final RestoreOderUseCase useCase) {
		this.objectMapper = objectMapper;
		this.useCase = useCase;
	}

	@Override
	@SneakyThrows
	public void execute(final ExternalTask externalTask, final ExternalTaskService externalTaskService) {

		final Long id = externalTask.getVariable("order_id");

		final Order order = useCase.execute(id)
				.orElseThrow(() -> OrderNotFoundException.from(id));

		externalTaskService
				.complete(externalTask, Map.of("order", this.objectMapper.writeValueAsString(order)));
	}

}

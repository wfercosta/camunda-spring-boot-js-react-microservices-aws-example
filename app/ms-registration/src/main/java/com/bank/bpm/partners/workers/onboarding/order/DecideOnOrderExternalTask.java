package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.Map;

@ExternalTaskController(topic = "process_order")
public class DecideOnOrderExternalTask implements ExternalTaskHandler {

	private ObjectMapper objectMapper;

	public DecideOnOrderExternalTask(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		String jsonOrder = externalTask.getVariable("order");

		Order order = objectMapper.readValue(jsonOrder, Order.class);

		final Boolean isMakePayment = order.getCost() > 0;
		final Boolean isPhysical = order.getProductId() > 0;

		externalTaskService
				.complete(externalTask,
						Map.of("is_make_payment", isMakePayment,
								"is_physical", isPhysical));
	}
}

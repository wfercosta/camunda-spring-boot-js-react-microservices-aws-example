package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.Map;

@ExternalTaskController(topic = "order_reserve_items")
public class ReserveItemsInWarehouseExternalTask implements ExternalTaskHandler {

	private ObjectMapper objectMapper;
	private ReserveItemsInWarehouseUseCase useCase;

	public ReserveItemsInWarehouseExternalTask(final ObjectMapper objectMapper, final ReserveItemsInWarehouseUseCase useCase) {
		this.objectMapper = objectMapper;
		this.useCase = useCase;
	}

	@Override
	@SneakyThrows
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		final String jsonOrder = externalTask.getVariable("order");
		final Order order = objectMapper.readValue(jsonOrder, Order.class);

		Boolean result = useCase.execute(order);

		externalTaskService.complete(externalTask, Map.of("is_items_reserved", result));
	}

}

package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.Map;
import java.util.Optional;

@ExternalTaskController(topic = "order_process_invoice")
public class ProcessInvoiceExternalTask implements ExternalTaskHandler {
	private ObjectMapper objectMapper;
	private ProcessInvoiceUseCase useCase;

	public ProcessInvoiceExternalTask(final ObjectMapper objectMapper, final ProcessInvoiceUseCase useCase) {
		this.objectMapper = objectMapper;
		this.useCase = useCase;
	}

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		final String jsonOrder = externalTask.getVariable("order");
		final Order order = objectMapper.readValue(jsonOrder, Order.class);

		Optional<Invoice> optional = useCase.execute(order);

		Invoice invoice = optional.get();

		externalTaskService.complete(externalTask,
				Map.of("invoice", objectMapper.writeValueAsString(invoice),
						"order", objectMapper.writeValueAsString(invoice.getOrder())));
	}
}

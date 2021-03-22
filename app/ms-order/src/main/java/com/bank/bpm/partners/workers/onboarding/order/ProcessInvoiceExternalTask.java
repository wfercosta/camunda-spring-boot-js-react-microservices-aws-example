package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@ExternalTaskController(topic = "order_invoice_process")
public class ProcessInvoiceExternalTask implements ExternalTaskHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessInvoiceExternalTask.class);

    private ObjectMapper objectMapper;
    private ProcessInvoiceUseCase useCase;

    public ProcessInvoiceExternalTask(final ObjectMapper objectMapper, final ProcessInvoiceUseCase useCase) {
        this.objectMapper = objectMapper;
        this.useCase = useCase;
    }

    @SneakyThrows
    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        LOG.info("{} - Start", this.getClass().getName());

        final String jsonOrder = externalTask.getVariable("order");
        final Order order = objectMapper.readValue(jsonOrder, Order.class);

        Invoice invoice = useCase.execute(order)
                .orElseThrow(() -> InvoiceNotProcessedException.from(order));

        externalTaskService.complete(externalTask,
                Map.of("invoice", objectMapper.writeValueAsString(invoice),
                        "order", objectMapper.writeValueAsString(invoice.getOrder())));

        LOG.info("{} - Finish", this.getClass().getName());
    }
}

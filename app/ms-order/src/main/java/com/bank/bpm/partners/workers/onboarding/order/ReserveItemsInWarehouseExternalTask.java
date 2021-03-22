package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExternalTaskController(topic = "order_items_reserve")
public class ReserveItemsInWarehouseExternalTask implements ExternalTaskHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReserveItemsInWarehouseExternalTask.class);

    private ObjectMapper objectMapper;
    private ReserveItemsInWarehouseUseCase useCase;

    public ReserveItemsInWarehouseExternalTask(final ObjectMapper objectMapper, final ReserveItemsInWarehouseUseCase useCase) {
        this.objectMapper = objectMapper;
        this.useCase = useCase;
    }

    @Override
    @SneakyThrows
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        LOG.info("{} - Start", this.getClass().getName());

        final String jsonOrder = externalTask.getVariable("order");
        final Order order = objectMapper.readValue(jsonOrder, Order.class);

        useCase.execute(order);

        externalTaskService.complete(externalTask);

        LOG.info("{} - Finish", this.getClass().getName());
    }

}

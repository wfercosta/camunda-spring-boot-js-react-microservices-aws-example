package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExternalTaskController(topic = "order_items_release")
public class ReleaseItemsInWarehouseExternalTask implements ExternalTaskHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReleaseItemsInWarehouseExternalTask.class);

    @Override
    @SneakyThrows
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOG.info("{} - Start", this.getClass().getName());
        externalTaskService.complete(externalTask);
        LOG.info("{} - Finish", this.getClass().getName());
    }

}

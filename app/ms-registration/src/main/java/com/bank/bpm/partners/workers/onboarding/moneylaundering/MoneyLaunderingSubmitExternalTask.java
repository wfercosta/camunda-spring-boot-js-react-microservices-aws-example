package com.bank.bpm.partners.workers.onboarding.moneylaundering;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.UUID;

import static com.bank.bpm.partners.workers.onboarding.moneylaundering.MoneyLaunderingConstants.PROCESS_INSTANCE_VAR_CORRELATION_ID;

@ExternalTaskController(topic = "mlp_submit")
public class MoneyLaunderingSubmitExternalTask implements ExternalTaskHandler {

	private static final Logger LOG = LoggerFactory.getLogger(MoneyLaunderingSubmitExternalTask.class);

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		final String correlationId = UUID.randomUUID().toString();

		LOG.info("  -> (External Task) Correlation ID: {}", correlationId);

		externalTaskService.complete(externalTask,
				Collections.singletonMap(PROCESS_INSTANCE_VAR_CORRELATION_ID, correlationId));
	}
}


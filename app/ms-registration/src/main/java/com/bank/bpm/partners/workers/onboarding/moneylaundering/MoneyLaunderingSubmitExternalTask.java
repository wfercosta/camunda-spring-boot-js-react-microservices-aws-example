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
import static com.bank.bpm.partners.workers.onboarding.moneylaundering.MoneyLaunderingConstants.VARIABLE_BODY;

@ExternalTaskController(topic = "mlp_submit")
public class MoneyLaunderingSubmitExternalTask implements ExternalTaskHandler {

	private static final Logger LOG = LoggerFactory.getLogger(MoneyLaunderingSubmitExternalTask.class);

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		LOG.info("  -> External Task started: {}", this.getClass().getName());

		final String correlationId = UUID.randomUUID().toString();

		LOG.info("      ----> Correlation ID: {}", correlationId);
		LOG.info("      ----> Message: {}", (String) externalTask.getVariable(VARIABLE_BODY));

		externalTaskService.complete(externalTask,
				Collections.singletonMap(PROCESS_INSTANCE_VAR_CORRELATION_ID, correlationId));
	}
}


package com.bank.bpm.partners.workers.onboarding.accreditation;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bank.bpm.partners.workers.onboarding.accreditation.PartnerConstants.VARIABLE_BODY;

@ExternalTaskController(topic = "partner_save")
public class PartnerSaveExternalTask implements ExternalTaskHandler {

	private static final Logger LOG = LoggerFactory.getLogger(PartnerSaveExternalTask.class);

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		LOG.info("  -> External Task started: {}", this.getClass().getName());
		LOG.info("      ----> Message: {}", (String) externalTask.getVariable(VARIABLE_BODY));
		externalTaskService.complete(externalTask);
	}
}


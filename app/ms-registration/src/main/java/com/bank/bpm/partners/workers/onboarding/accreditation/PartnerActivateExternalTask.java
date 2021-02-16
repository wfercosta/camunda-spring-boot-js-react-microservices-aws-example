package com.bank.bpm.partners.workers.onboarding.accreditation;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskController;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bank.bpm.partners.workers.onboarding.accreditation.PartnerConstants.*;

@ExternalTaskController(topic = "partner_activate")
public class PartnerActivateExternalTask implements ExternalTaskHandler {

	private static final Logger LOG = LoggerFactory.getLogger(PartnerActivateExternalTask.class);

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		LOG.info("  -> External Task started: {}", this.getClass().getName());
		LOG.info("      ----> Message: {}", (String) externalTask.getVariable(VARIABLE_BODY));
		LOG.info("      ----> Money Laundering:");
		LOG.info("            ----> status: {}", (String) externalTask.getVariable(VARIABLE_MLP_STATUS));
		LOG.info("            ----> analyze result: {}", (String) externalTask.getVariable(VARIABLE_MLP_RESULTS));
		externalTaskService.complete(externalTask);
	}
}


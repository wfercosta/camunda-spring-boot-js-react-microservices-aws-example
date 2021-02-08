package com.bank.bpm.partners.workers.onboarding.accreditation;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

@ExternalTaskComponent(topic="partner_registration_draft")
public class PartnerAccreditationExternalTask implements ExternalTaskHandler {

	private final ObjectMapper mapper;

	public PartnerAccreditationExternalTask(final ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@SneakyThrows
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		externalTask.getAllVariables().forEach((k, v) -> System.out.println(k + " = " + v));

		PartnerAccreditation accreditation = mapper.readValue(externalTask.<String>getVariable("body"), PartnerAccreditation.class);

		if (true) {
			throw new PartnerAccreditationDuplicationException(accreditation);
		}

		externalTaskService.complete(externalTask);
	}
}

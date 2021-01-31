package com.bank.bpm.partners.workers.onboarding;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class ExternalTask {

	@Scheduled(fixedRate = 30000)
	public void execute() {

		ExternalTaskClient client = ExternalTaskClient.create()
				.baseUrl("http://localhost:8080/engine-rest")
				.asyncResponseTimeout(1000)
				.build();

		// subscribe to the topic
		client.subscribe("registerPartner")
			.handler((externalTask, externalTaskService) -> {

				Map<String, Object> variables = new HashMap<>();

				System.out.println("executado");

				externalTask.

				externalTaskService.complete(externalTask, variables);

			}).open();

	}


}

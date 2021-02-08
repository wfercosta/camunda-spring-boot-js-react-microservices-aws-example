package com.bank.bpm.partners.workers.onboarding.configuration;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskManager;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;


@Configuration
public class ExternalTaskConfiguration {

	@Bean
	public ExternalTaskManager externalTaskManager(final ExternalTasksProperties properties, final ApplicationContext context) {

		Map<String, ExternalTaskHandler> beans = context.getBeansOfType(ExternalTaskHandler.class);

		return ExternalTaskManager.Builder
				.fromExternalTaskSettings(properties)
				.withListeners(beans.values().stream().collect(Collectors.toList()))
				.build();

	}

}

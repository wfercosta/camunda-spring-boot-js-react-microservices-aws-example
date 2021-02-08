package com.bank.bpm.partners.onboarding.starters;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskManager;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnClass(ExternalTaskManager.class)
@EnableConfigurationProperties(ExternalTasksProperties.class)
public class ExternalTaskAutoConfiguration {

	@Bean
	public ExternalTaskManager externalTaskManager(final ExternalTasksProperties properties, final ApplicationContext context) {

		Map<String, ExternalTaskHandler> beans = context.getBeansOfType(ExternalTaskHandler.class);

		return ExternalTaskManager.Builder
				.fromExternalTaskSettings(properties)
				.withListeners(beans.values().stream().collect(Collectors.toList()))
				.build();

	}

}

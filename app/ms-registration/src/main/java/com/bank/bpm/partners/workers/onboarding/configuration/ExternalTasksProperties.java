package com.bank.bpm.partners.workers.onboarding.configuration;

import com.bank.bpm.partners.workers.onboarding.component.ExternalTaskSettings;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "itau.components.camunda-external-task.connection")
public class ExternalTasksProperties implements ExternalTaskSettings {

	@DurationUnit(ChronoUnit.SECONDS)
	Duration asyncResponseTime = Duration.ofSeconds(1);

	@DurationUnit(ChronoUnit.SECONDS)
	Duration lockDuration = Duration.ofSeconds(20);

	Integer maxRetries = 1;

	URL baseUrl;

}

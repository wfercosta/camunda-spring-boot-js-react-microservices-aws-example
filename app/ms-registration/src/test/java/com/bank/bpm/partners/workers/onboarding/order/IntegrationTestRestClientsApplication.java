package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.onboarding.starters.ExternalTaskAutoConfiguration;
import com.bank.bpm.partners.workers.onboarding.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class, ExternalTaskAutoConfiguration.class})
public class IntegrationTestRestClientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

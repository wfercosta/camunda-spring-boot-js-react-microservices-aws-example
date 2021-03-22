package com.bank.bpm.partners.workers.onboarding.order;

import com.bank.bpm.partners.onboarding.starters.ExternalTaskAutoConfiguration;
import com.bank.bpm.partners.workers.onboarding.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@EnableAutoConfiguration(exclude = {ExternalTaskAutoConfiguration.class})
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

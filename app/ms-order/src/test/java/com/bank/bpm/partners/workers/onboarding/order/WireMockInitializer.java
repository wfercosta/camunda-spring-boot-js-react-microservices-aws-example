package com.bank.bpm.partners.workers.onboarding.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


	public static final String BEAN_NAME_WIRE_MOCK_SERVER = "wireMockServer";

	@Override
	public void initialize(final ConfigurableApplicationContext config) {
		final WireMockServer wireMockServer = new WireMockServer(9561);

		wireMockServer.start();
		config.getBeanFactory().registerSingleton(BEAN_NAME_WIRE_MOCK_SERVER, wireMockServer);

		config.addApplicationListener(applicationEvent -> {
			if (applicationEvent instanceof ContextClosedEvent) {
				wireMockServer.stop();
			}
		});
	}
}

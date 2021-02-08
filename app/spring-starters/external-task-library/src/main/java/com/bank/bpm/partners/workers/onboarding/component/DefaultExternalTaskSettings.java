package com.bank.bpm.partners.workers.onboarding.component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DefaultExternalTaskSettings implements ExternalTaskSettings {

	@Override
	public Duration getAsyncResponseTime() {
		return Duration.ofSeconds(1);
	}

	@Override
	public Duration getLockDuration() {
		return Duration.ofSeconds(20);
	}

	@Override
	public URL getBaseUrl() {
		try {
			return new URL("http://localhost:8080");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer getMaxRetries() {
		return 1;
	}
}

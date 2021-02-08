package com.bank.bpm.partners.workers.onboarding.component;

import java.net.URL;
import java.time.Duration;

public interface ExternalTaskSettings {

	static ExternalTaskSettings defaultSettings() {
		return new DefaultExternalTaskSettings();
	}

	Duration getAsyncResponseTime();
	Duration getLockDuration();
	URL getBaseUrl();
	Integer getMaxRetries();

}

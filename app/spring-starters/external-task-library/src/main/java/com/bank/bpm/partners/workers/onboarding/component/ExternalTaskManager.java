package com.bank.bpm.partners.workers.onboarding.component;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.topic.TopicSubscription;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExternalTaskManager {

	private final ExternalTaskSettings settings;
	private final ExternalTaskClient client;
	private final List<TopicSubscription> subscriptions;

	private ExternalTaskManager(ExternalTaskClient client, ExternalTaskSettings settings, List<TopicSubscription> subscriptions) {
		this.client = client;
		this.settings = settings;
		this.subscriptions = Collections.unmodifiableList(subscriptions);
	}

	public ExternalTaskClient getClient() {
		return client;
	}

	public List<TopicSubscription> getSubscriptions() {
		return subscriptions;
	}

	public static class Builder {

		private static final String CAMUNDA_URI_RELATIVE_API = "/engine-rest";

		private ExternalTaskSettings settings;
		private List<ExternalTaskHandler> listeners = new ArrayList<>();
		private List<TopicSubscription> subscriptions = new ArrayList<>();

		private Builder() {
			this.settings = ExternalTaskSettings.defaultSettings();
		}

		private Builder(final ExternalTaskSettings settings) {
			this.settings = settings;
		}

		public static Builder fromDefaultConfiguration() {
			return new Builder();
		}

		public static Builder fromExternalTaskSettings(final ExternalTaskSettings configuration) {
			return new Builder(configuration);
		}

		public Builder withListeners(final List<ExternalTaskHandler> listeners) {

			if (Objects.isNull(listeners) || listeners.isEmpty()) {
				throw new IllegalStateException("Expected to have listernes to initialize");
			}

			this.listeners = Collections.unmodifiableList(listeners);

			return this;
		}

		public com.bank.bpm.partners.workers.onboarding.component.ExternalTaskManager build() {
			try {

				final ExternalTaskClient client = ExternalTaskClient.create()
						.baseUrl(settings.getBaseUrl().toURI().resolve(CAMUNDA_URI_RELATIVE_API).toString())
						.lockDuration(settings.getLockDuration().toMillis())
						.asyncResponseTimeout(settings.getAsyncResponseTime().toMillis())
						.build();

				this.listeners.forEach( handler -> {
					com.bank.bpm.partners.workers.onboarding.component.ExternalTaskComponent annotation = handler.getClass().getAnnotation(com.bank.bpm.partners.workers.onboarding.component.ExternalTaskComponent.class);

					final TopicSubscription subscription = client.subscribe(annotation.topic())
							.handler(com.bank.bpm.partners.workers.onboarding.component.ExternalTaskHandlerDecorator.from(handler, settings)).open();

					this.subscriptions.add(subscription);
				});

				return new com.bank.bpm.partners.workers.onboarding.component.ExternalTaskManager(client, settings, subscriptions);

			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

		}
	}
}

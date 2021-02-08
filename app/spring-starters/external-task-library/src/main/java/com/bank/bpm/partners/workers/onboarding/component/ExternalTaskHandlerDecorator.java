package com.bank.bpm.partners.workers.onboarding.component;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.Objects;

public class ExternalTaskHandlerDecorator implements ExternalTaskHandler {

	private static final String GENERIC_FAILURE = "generic_failure";
	private static final int MIN_DELAY_MILLIS =  1000;

	private final ExternalTaskHandler handler;
	private final ExternalTaskSettings settings;

	private ExternalTaskHandlerDecorator(final ExternalTaskHandler handler, final ExternalTaskSettings settings) {
		this.handler = handler;
		this.settings = settings;
	}

	public static com.bank.bpm.partners.workers.onboarding.component.ExternalTaskHandlerDecorator from(final ExternalTaskHandler handler, final ExternalTaskSettings settings) {
		return new com.bank.bpm.partners.workers.onboarding.component.ExternalTaskHandlerDecorator(handler, settings);
	}

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		try {
			handler.execute(externalTask, externalTaskService);
		} catch (BusinessProcessModelException e) {
			externalTaskService.handleBpmnError(externalTask, e.getErrorCode(), e.getMessage(), e.getVariables());
		} catch (Exception e) {
			externalTaskService.handleFailure(externalTask,
					GENERIC_FAILURE,
					e.getMessage(),
					decrementRetries(externalTask),
					calculateBackoff(externalTask));
		}
	}

	private long calculateBackoff(ExternalTask task) {
		final int retry = Objects.isNull(task.getRetries()) ? 0 : settings.getMaxRetries() - task.getRetries();
		final long weight = fibonacci(retry);
		final long delay = weight * MIN_DELAY_MILLIS;
		return delay;
	}

	private int decrementRetries(ExternalTask task) {
		return Objects.isNull(task.getRetries()) ? settings.getMaxRetries() - 1 : task.getRetries() - 1;
	}

	private long fibonacci(int n) {
		return (n < 2) ? n : fibonacci(n - 1) + fibonacci(n - 2);
	}

}

package com.bank.bpm.partners.workers.onboarding.order;

public abstract class ApplicationException extends RuntimeException {
	public ApplicationException(String message) {
		super(message);
	}
}

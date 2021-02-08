package com.bank.bpm.partners.workers.onboarding.component;

import lombok.Getter;

@Getter
public abstract class BusinessProcessModelException extends RuntimeException {

	private final String errorCode;

	public BusinessProcessModelException(final String errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}


}

package com.bank.bpm.partners.workers.onboarding.component;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public abstract class BusinessProcessModelException extends RuntimeException {

	private final String errorCode;
	private Map<String, Object> variables = Collections.emptyMap();

	public BusinessProcessModelException(final String errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public BusinessProcessModelException(final String errorCode, final String message, final Map<String, Object> variables) {
		this(errorCode, message);

		if (Objects.nonNull(variables)) {
			this.variables = Collections.unmodifiableMap(variables);
		}

	}


}

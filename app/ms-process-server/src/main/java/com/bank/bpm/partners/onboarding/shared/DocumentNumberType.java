package com.bank.bpm.partners.onboarding.shared;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public enum DocumentNumberType {
	CPF(" (^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)"),
	CNPJ("(^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$)"),
	NOT_DEFINED(null);

	private String pattern;

	DocumentNumberType(String pattern) {
		this.pattern = pattern;
	}

	private boolean matchThis(String documentNumber) {

		if (nonNull(this.pattern)) {
			Matcher matcher = Pattern.compile(this.pattern)
					.matcher(documentNumber);
			return matcher.find();
		}

		return false;
	}

	public static DocumentNumberType findTypeBy(String documentNumber) {

		for (DocumentNumberType type : DocumentNumberType.values()) {
			if (type.matchThis(documentNumber)) {
				return type;
			}
		}

		return NOT_DEFINED;
	}

}

package com.bank.bpm.partners.workers.onboarding.shared;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonDeserialize(using = DocumentNumberDeserializer.class)
@RequiredArgsConstructor
public class DocumentNumber {

	@JsonValue
	private final String value;
	private final DocumentNumberType type;

	public static final DocumentNumber of(String documentNumber) {
		return new DocumentNumber(documentNumber, DocumentNumberType.findTypeBy(documentNumber));
	}

}

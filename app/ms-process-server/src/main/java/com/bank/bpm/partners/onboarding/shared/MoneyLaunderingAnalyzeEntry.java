package com.bank.bpm.partners.onboarding.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoneyLaunderingAnalyzeEntry {

	@JsonProperty("document_number")
	private DocumentNumber documentNumber;
	private String name;
	private MoneyLaunderingStatusType status;

	public MoneyLaunderingAnalyzeEntry(String name, DocumentNumber documentNumber) {
		this.name = name;
		this.documentNumber = documentNumber;
	}

	public static MoneyLaunderingAnalyzeEntry of(String name, DocumentNumber document) {
		return new MoneyLaunderingAnalyzeEntry(name, document);
	}
}

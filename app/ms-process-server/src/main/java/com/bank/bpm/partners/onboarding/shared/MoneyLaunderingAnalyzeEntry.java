package com.bank.bpm.partners.onboarding.shared;

import com.bank.bpm.partners.onboarding.moneylaundering.MoneyLaunderingStatusType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoneyLaunderingAnalyzeEntry {
	private String document;
	private String name;
	private MoneyLaunderingStatusType status;

	public MoneyLaunderingAnalyzeEntry(String name, String document) {
		this.name = name;
		this.document = document;
	}

	public static MoneyLaunderingAnalyzeEntry of(String name, String document) {
		return new MoneyLaunderingAnalyzeEntry(name, document);
	}
}

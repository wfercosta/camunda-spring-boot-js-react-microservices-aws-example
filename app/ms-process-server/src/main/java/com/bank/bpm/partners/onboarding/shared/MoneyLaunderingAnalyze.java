package com.bank.bpm.partners.onboarding.shared;

import lombok.Data;

import java.util.List;

@Data
public class MoneyLaunderingAnalyze {

	private String correlationId;
	private List<MoneyLaunderingAnalyzeEntry> entries;

}

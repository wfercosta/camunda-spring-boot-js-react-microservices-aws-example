package com.bank.bpm.partners.workers.onboarding.shared;

import lombok.Data;

@Data
public class MoneyLaunderingAnalyze {

	private DocumentNumber documentNumber;
	private String name;
	private MoneyLaunderingStatusType status;
}

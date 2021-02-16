package com.bank.bpm.partners.workers.onboarding.moneylaundering;

import lombok.Data;

@Data
public class MoneyLaunderingAnalyze {
	
	private String document;
	private String name;
	private MoneyLaunderingStatusType status;
}

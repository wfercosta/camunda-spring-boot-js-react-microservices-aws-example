package com.bank.bpm.partners.onboarding.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MoneyLaunderingAnalyze {

	@JsonProperty("correlation_id")
	private String correlationId;
	private List<MoneyLaunderingAnalyzeEntry> entries;

}

package com.bank.bpm.partners.workers.onboarding.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Affiliate {

	@JsonProperty("document_number")
	private DocumentNumber documentNumber;

	@JsonProperty("business_name")
	private String businessName;

}

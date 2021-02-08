package com.bank.bpm.partners.onboarding.accreditation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Employee {

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("document_number")
	private String documentNumber;

}

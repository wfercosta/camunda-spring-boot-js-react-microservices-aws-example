package com.bank.bpm.partners.workers.onboarding.accreditation;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartnerAccreditation {

	@JsonProperty("document_number")
	private String documentNumber;

	@JsonProperty("business_name")
	private String businessName;

	private List<Affiliate> affiliates = new ArrayList<>();
	private List<Employee> employees = new ArrayList<>();
	private List<Brand> brands = new ArrayList<>();

}

package com.bank.bpm.partners.onboarding.partner;

import com.bank.bpm.partners.onboarding.shared.MoneyLaunderingAnalyzeEntry;
import com.bank.bpm.partners.onboarding.shared.Partner;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateVariableMapping;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bank.bpm.partners.onboarding.partner.PartnerConstants.*;

@Component
public class PartnerActivityAnalyseMoneyLaunderingMapping implements DelegateVariableMapping {

	private final ObjectMapper objectMapper;

	public PartnerActivityAnalyseMoneyLaunderingMapping(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@SneakyThrows
	@Override
	public void mapInputVariables(DelegateExecution superExecution, VariableMap subVariables) {

		final List<MoneyLaunderingAnalyzeEntry> entries = new ArrayList<>();

		final Partner partner = objectMapper.readValue((String) superExecution.getVariable(VARIABLE_BODY), Partner.class);

		entries.add(MoneyLaunderingAnalyzeEntry.of(partner.getBusinessName(), partner.getDocumentNumber()));
		entries.addAll(partner.getAffiliates().stream()
				.map(entry -> MoneyLaunderingAnalyzeEntry.of(entry.getBusinessName(), entry.getDocumentNumber()))
				.collect(Collectors.toList()));

		subVariables.putValue(ACTIVITY_MLP_VAR_BODY, objectMapper.writeValueAsString(entries));

	}

	@Override
	public void mapOutputVariables(DelegateExecution superExecution, VariableScope subInstance) {
		superExecution.setVariable(PartnerConstants.VARIABLE_MLP_RESULTS, subInstance.getVariable(ACTIVITY_MLP_VAR_BODY));
		superExecution.setVariable(PartnerConstants.VARIABLE_MLP_STATUS, subInstance.getVariable(ACTIVITY_MLP_VAR_STATUS));
	}

}

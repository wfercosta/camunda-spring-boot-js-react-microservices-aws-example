package com.bank.bpm.partners.onboarding.moneylaundering;

import com.bank.bpm.partners.onboarding.shared.MoneyLaunderingAnalyze;
import com.bank.bpm.partners.onboarding.shared.MoneyLaunderingAnalyzeEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bank.bpm.partners.onboarding.moneylaundering.MoneyLaunderingConstants.*;
import static com.bank.bpm.partners.onboarding.shared.MoneyLaunderingStatusType.SUITABLE;


@RestController
@RequestMapping("/money-laundering")
public class MoneyLaunderingController {

	private final ObjectMapper objectMapper;
	private ProcessEngine engine;

	public MoneyLaunderingController(final ProcessEngine engine, final ObjectMapper objectMapper) {
		this.engine = engine;
		this.objectMapper = objectMapper;
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Void> create(@RequestBody final MoneyLaunderingAnalyze analyze) throws JsonProcessingException {

		engine.getRuntimeService().createMessageCorrelation(MoneyLaunderingConstants.MESSAGE_NAME)
				.processInstanceVariableEquals(PROCESS_INSTANCE_VAR_CORRELATION_ID, analyze.getCorrelationId())
				.setVariable(VARIABLE_BODY, objectMapper.writeValueAsString(analyze))
				.setVariable(VARIABLE_STATUS, obtainMostCriticalStatus(analyze))
				.correlate();

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	private String obtainMostCriticalStatus(final MoneyLaunderingAnalyze analyze) {
		return analyze.getEntries().stream().map(MoneyLaunderingAnalyzeEntry::getStatus)
				.reduce(SUITABLE,
						(current, value) -> value.getWeight() > current.getWeight() ? value : current)
				.name();
	}

}

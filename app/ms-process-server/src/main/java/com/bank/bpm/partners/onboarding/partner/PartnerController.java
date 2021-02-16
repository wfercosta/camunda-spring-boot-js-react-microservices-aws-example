package com.bank.bpm.partners.onboarding.partner;

import com.bank.bpm.partners.onboarding.shared.Partner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bank.bpm.partners.onboarding.partner.PartnerConstants.PROCESS_NAME;
import static com.bank.bpm.partners.onboarding.partner.PartnerConstants.VARIABLE_BODY;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/partners")
public class PartnerController {

	private final ObjectMapper objectMapper;
	private ProcessEngine engine;

	public PartnerController(final ProcessEngine engine, final ObjectMapper objectMapper) {
		this.engine = engine;
		this.objectMapper = objectMapper;
	}

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity create(@RequestBody final Partner accreditation) throws JsonProcessingException {

		final ProcessInstance instance = engine.getRuntimeService().startProcessInstanceByKey(PROCESS_NAME,
				Variables.putValue(VARIABLE_BODY, objectMapper.writeValueAsString(accreditation))
		);

		return ResponseEntity.status(CREATED).body(ProcessInfo.from(instance));
	}

}

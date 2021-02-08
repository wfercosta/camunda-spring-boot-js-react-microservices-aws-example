package com.bank.bpm.partners.onboarding.accreditation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Getter
@RequiredArgsConstructor
public class ProcessInfo {

	private final String id;
	private final String processName;

	public static ProcessInfo from(ProcessInstance instance) {
		final ExecutionEntity entity = ((ProcessInstanceWithVariablesImpl) instance).getExecutionEntity();
		return new ProcessInfo(instance.getProcessInstanceId(), entity.getProcessDefinition().getKey());
	}
}

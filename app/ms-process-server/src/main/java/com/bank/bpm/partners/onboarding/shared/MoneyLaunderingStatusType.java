package com.bank.bpm.partners.onboarding.shared;

import lombok.Getter;

@Getter
public enum MoneyLaunderingStatusType {
	SUITABLE(1), UNFAITHFUL(2), INCONCLUSIVE(3);

	private int weight;

	MoneyLaunderingStatusType(final int weight) {
		this.weight = weight;
	}

}

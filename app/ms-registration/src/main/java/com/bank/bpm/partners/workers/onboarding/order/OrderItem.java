package com.bank.bpm.partners.workers.onboarding.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OrderItem {

	@EqualsAndHashCode.Exclude
	private Long id;
	private boolean virtual;
}

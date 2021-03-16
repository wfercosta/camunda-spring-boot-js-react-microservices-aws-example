package com.bank.bpm.partners.workers.onboarding.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Order {

	private Long id;

	@EqualsAndHashCode.Exclude
	private OrderItem item;

	@EqualsAndHashCode.Exclude
	private Double cost;
}

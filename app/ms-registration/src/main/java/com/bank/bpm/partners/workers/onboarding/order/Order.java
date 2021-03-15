package com.bank.bpm.partners.workers.onboarding.order;

import lombok.Data;

@Data
public class Order {

	private Long id;
	private Long productId;
	private Double cost;
}

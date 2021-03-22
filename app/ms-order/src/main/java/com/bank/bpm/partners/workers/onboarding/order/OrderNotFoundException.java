package com.bank.bpm.partners.workers.onboarding.order;

public class OrderNotFoundException extends RuntimeException {

	public OrderNotFoundException(Long id) {
		super(String.format("Order with id %d was not found", id));
	}

	public static OrderNotFoundException from(Long id) {
		return new OrderNotFoundException(id);
	}
}

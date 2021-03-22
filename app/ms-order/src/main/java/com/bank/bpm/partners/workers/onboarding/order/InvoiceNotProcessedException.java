package com.bank.bpm.partners.workers.onboarding.order;

public class InvoiceNotProcessedException extends RuntimeException {

	public InvoiceNotProcessedException(final Order order) {
		super(String.format("Invoice not processed for order %d", order.getId()));
	}

	public static InvoiceNotProcessedException from(final Order order) {
		return new InvoiceNotProcessedException(order);
	}
}

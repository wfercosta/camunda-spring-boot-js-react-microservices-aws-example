package com.bank.bpm.partners.workers.onboarding.order;

public class DuplicatedProductSKUException extends ApplicationException {
	public DuplicatedProductSKUException(String sku) {
		super(String.format("Duplicated SKU %s", sku));
	}
}

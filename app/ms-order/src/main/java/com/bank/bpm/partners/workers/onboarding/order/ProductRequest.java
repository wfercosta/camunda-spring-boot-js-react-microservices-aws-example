package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductRequest {

	@NotNull
	private String sku;

	@NotNull
	@Min(0)
	private Integer amount;

	@NotNull
	@JsonProperty(value = "is_dispatchable")
	private Boolean dispatchable;

	public Product toProduct() {
		return Product.builder()
				.sku(this.getSku())
				.amount(this.getAmount())
				.dispatchable(this.getDispatchable())
				.build();
	}
}

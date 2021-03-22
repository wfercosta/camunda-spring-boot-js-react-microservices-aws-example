package com.bank.bpm.partners.workers.onboarding.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

	private Long orderId;
	private Double total;

	public static CreatePaymentRequest from(Order order) {

		final Double total = order.getItems()
				.stream()
				.map(OrderItem::getPrice)
				.reduce(0d, Double::sum);

		return new CreatePaymentRequest(order.getId(), total);
	}
}

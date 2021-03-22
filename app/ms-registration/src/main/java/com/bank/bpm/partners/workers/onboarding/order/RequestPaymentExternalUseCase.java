package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RequestPaymentExternalUseCase implements UseCase<Optional<String>, Order> {
	private final RestPaymentClient client;
	private final OrderRepository repository;

	public RequestPaymentExternalUseCase(final RestPaymentClient client, final OrderRepository repository) {
		this.client = client;
		this.repository = repository;
	}

	@Override
	public Optional<String> execute(Order in) {

		final Optional<Order> optional = repository.findByIdAndStatus(in.getId(), OrderStatus.ORDER_PROCESSING);

		if (optional.isPresent()) {

			final Order order = optional.get();
			order.setStatus(OrderStatus.ORDER_PENDING_PAYMENT);
			repository.save(order);

			CreatePaymentResponse response = client.create(CreatePaymentRequest.from(order));

			return Optional.of(response.getCorrelationId());

		}

		return Optional.empty();
	}
}

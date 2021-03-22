package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestoreOrderUseCase implements UseCase<Optional<Order>, Long> {

	private final OrderRepository repository;

	public RestoreOrderUseCase(final OrderRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<Order> execute(Long id) {

		Optional<Order> optional = repository.findByIdAndStatus(id, OrderStatus.ORDER_NEW);

		if (optional.isEmpty()) {
			return optional;
		}

		Order order = optional.get();
		order.setStatus(OrderStatus.ORDER_PROCESSING);

		return Optional.of(repository.save(order));
	}

}

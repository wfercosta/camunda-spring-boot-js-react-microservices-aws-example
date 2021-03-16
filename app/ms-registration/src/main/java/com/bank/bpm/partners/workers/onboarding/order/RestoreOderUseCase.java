package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestoreOderUseCase implements UseCase<Optional<Order>, Long> {

	private final OrderRepository repository;

	public RestoreOderUseCase(final OrderRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<Order> execute(Long id) {
		return repository.findById(id);
	}

}

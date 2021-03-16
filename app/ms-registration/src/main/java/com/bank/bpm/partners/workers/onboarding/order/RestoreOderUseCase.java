package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestoreOderUseCase implements UseCase<Optional<Order>, Long> {

	@Override
	public Optional<Order> execute(Long in) {
		return Optional.empty();
	}
	
}

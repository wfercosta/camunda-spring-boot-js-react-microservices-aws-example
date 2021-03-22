package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {


	Optional<Order> findByIdAndStatus(Long id, OrderStatus status);
}

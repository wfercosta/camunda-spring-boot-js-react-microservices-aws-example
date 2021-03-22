package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
	List<Product> findAllBySkuInAndDispatchableTrue(List<String> skus);
}

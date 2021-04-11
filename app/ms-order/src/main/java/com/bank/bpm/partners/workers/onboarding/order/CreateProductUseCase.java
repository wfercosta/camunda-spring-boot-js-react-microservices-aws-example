package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class CreateProductUseCase implements UseCase<Product, Product> {
	private final ProductRepository repository;

	public CreateProductUseCase(final ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public Product execute(Product in) {
		try {
			return repository.save(in);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new DuplicatedProductSKUException(in.getSku());
		}
	}
}

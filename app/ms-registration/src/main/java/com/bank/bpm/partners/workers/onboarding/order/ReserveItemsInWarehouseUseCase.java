package com.bank.bpm.partners.workers.onboarding.order;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReserveItemsInWarehouseUseCase implements UseCase<Void, Order> {
	private ProductRepository repository;

	public ReserveItemsInWarehouseUseCase(final ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public Void execute(Order order) {

		final List<String> skus = order.getItems().stream()
				.map(OrderItem::getSku)
				.collect(Collectors.toList());

		Map<String, Product> products = repository.findAllBySkuInAndDispatchableTrue(skus)
				.stream()
				.collect(Collectors.toMap(Product::getSku, Function.identity()));


		order.getItems().forEach(item -> {
			products.computeIfPresent(item.getSku(), (key, product) -> {
				int remaining = product.getAmount() - item.getQuantity();
				product.setAmount(remaining);
				return product;
			});
		});

		repository.saveAll(products.values());

		return null;
	}
}

package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProcessInvoiceUseCase implements UseCase<Optional<Invoice>, Order> {

	private InvoiceRepository invoiceRepository;
	private OrderRepository orderRepository;

	public ProcessInvoiceUseCase(final InvoiceRepository invoiceRepository, final OrderRepository orderRepository) {
		this.invoiceRepository = invoiceRepository;
		this.orderRepository = orderRepository;
	}

	@Override
	public Optional<Invoice> execute(Order in) {

		Optional<Order> optional = orderRepository.findByIdAndStatus(in.getId(), OrderStatus.ORDER_PENDING_PAYMENT);

		if (optional.isPresent()) {
			final Order order = optional.get();
			final Double total = order.getItems()
					.stream().map(OrderItem::getPrice)
					.reduce(0d, Double::sum);

			order.setStatus(OrderStatus.ORDER_INVOICED);

			Invoice invoice = Invoice.builder()
					.total(total)
					.order(order)
					.build();

			return Optional.of(invoiceRepository.save(invoice));
		}

		return Optional.empty();
	}

}

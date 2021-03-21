package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.six2six.fixturefactory.processor.Processor;

import java.time.LocalDateTime;

public class InvoiceTemplate implements TemplateLoader {

	public static final String BASIC = "BASIC";

	@Override
	public void load() {
		Fixture.of(Invoice.class).addTemplate(BASIC, new Rule() {{
			add("id", random(Long.class, range(1L, 1000L)));
			add("order", one(Order.class, OrderTemplate.BASIC_STATE_PENDING_PAYMENT));
			add("total", random(Double.class, range(1d, 10000d)));
			add("createdAt", LocalDateTime.now());
			add("updatedAt", LocalDateTime.now());
		}});
	}


	public static final class InvoiceTotalProcessor implements Processor {

		@Override
		public void execute(Object result) {
			final Invoice invoice = (Invoice) result;

			Double total = invoice.getOrder()
					.getItems().stream()
					.map(OrderItem::getPrice)
					.reduce(0d, Double::sum);

			invoice.setTotal(total);
		}
	}


}

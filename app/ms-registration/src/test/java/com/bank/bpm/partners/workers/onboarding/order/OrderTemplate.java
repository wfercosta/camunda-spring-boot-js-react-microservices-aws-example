package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.LocalDateTime;

public class OrderTemplate implements TemplateLoader {

	public static final String NO_DISPATCHABLE_PRODUCT = "NO_DISPATCHABLE_PRODUCT";
	public static final String NO_WAREHOUSE_RESERVATION = "NO_WAREHOUSE_RESERVATION";

	@Override
	public void load() {
		Fixture.of(Order.class).addTemplate(NO_DISPATCHABLE_PRODUCT, new Rule() {{
			add("id", random(Long.class, range(1L, 200L)));
			add("items", has(2).of(OrderItem.class, OrderItemTemplate.DISPATCHABLE_PRODUCT));
			add("status", OrderStatus.ORDER_NEW);
			add("createdAt", LocalDateTime.now());
			add("updatedAt", LocalDateTime.now());
		}});

		Fixture.of(Order.class).addTemplate(NO_WAREHOUSE_RESERVATION).inherits(NO_DISPATCHABLE_PRODUCT, new Rule() {{
			add("items", has(2).of(OrderItem.class, OrderItemTemplate.NON_DISPATCHABLE_PRODUCT));
		}});
	}
}

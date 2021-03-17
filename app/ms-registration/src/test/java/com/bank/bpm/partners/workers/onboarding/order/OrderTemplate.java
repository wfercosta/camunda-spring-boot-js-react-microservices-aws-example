package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class OrderTemplate implements TemplateLoader {

	public static final String NEEDS_PAYMENT_AND_PHYSICAL = "NEEDS_PAYMENT_AND_PHYSICAL";
	public static final String NO_PAYMENT_IS_NEEDED = "NO_PAYMENT_IS_NEEDED";
	public static final String NO_WAREHOUSE_RESERVATION = "NO_WAREHOUSE_RESERVATION";

	@Override
	public void load() {
		Fixture.of(Order.class).addTemplate(NEEDS_PAYMENT_AND_PHYSICAL, new Rule() {{
			add("id", random(Long.class, range(1L, 200L)));
			add("item", one(OrderItem.class, OrderItemTemplate.DISPATCHABLE_PRODUCT));
			add("cost", 10.0);
		}});

		Fixture.of(Order.class).addTemplate(NO_PAYMENT_IS_NEEDED).inherits(NEEDS_PAYMENT_AND_PHYSICAL, new Rule() {{
			add("cost", 0.0);
		}});

		Fixture.of(Order.class).addTemplate(NO_WAREHOUSE_RESERVATION).inherits(NEEDS_PAYMENT_AND_PHYSICAL, new Rule() {{
			add("item", one(OrderItem.class, OrderItemTemplate.NON_DISPATCHABLE_PRODUCT));
		}});
	}
}

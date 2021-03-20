package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.LocalDateTime;

public class OrderTemplate implements TemplateLoader {

	public static final String STATE_PROCESSING_DISPATCHABLE_PRODUCT = "STATE_PROCESSING_DISPATCHABLE_PRODUCT";
	public static final String STATE_PROCESSING_NON_DISPATCHABLE_PRODUCT = "STATE_PROCESSING_NON_DISPATCHABLE_PRODUCT";
	public static final String BASIC = "BASIC";
	public static final String BASIC_STATE_PROCESSING = "BASIC_STATE_PROCESSING";

	@Override
	public void load() {

		Fixture.of(Order.class).addTemplate(BASIC, new Rule() {{
			add("id", random(Long.class, range(1L, 200L)));
			add("items", has(2).of(OrderItem.class, OrderItemTemplate.DISPATCHABLE_PRODUCT));
			add("status", OrderStatus.ORDER_NEW);
			add("createdAt", LocalDateTime.now());
			add("updatedAt", LocalDateTime.now());
		}});

		Fixture.of(Order.class).addTemplate(BASIC_STATE_PROCESSING).inherits(BASIC, new Rule() {{
			add("status", OrderStatus.ORDER_PROCESSING);
		}});

		Fixture.of(Order.class).addTemplate(STATE_PROCESSING_DISPATCHABLE_PRODUCT).inherits(BASIC_STATE_PROCESSING, new Rule() {{
			add("items", has(2).of(OrderItem.class, OrderItemTemplate.DISPATCHABLE_PRODUCT));
		}});

		Fixture.of(Order.class).addTemplate(STATE_PROCESSING_NON_DISPATCHABLE_PRODUCT).inherits(BASIC_STATE_PROCESSING, new Rule() {{
			add("items", has(2).of(OrderItem.class, OrderItemTemplate.NON_DISPATCHABLE_PRODUCT));
		}});
		
	}
}

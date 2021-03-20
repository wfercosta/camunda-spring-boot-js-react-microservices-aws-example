package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class OrderItemTemplate implements TemplateLoader {


	public static final String DISPATCHABLE_PRODUCT = "DISPATCHABLE_PRODUCT";
	public static final String NON_DISPATCHABLE_PRODUCT = "NON_DISPATCHABLE_PRODUCT";

	@Override
	public void load() {
		Fixture.of(OrderItem.class).addTemplate(DISPATCHABLE_PRODUCT, new Rule() {{
			add("id", random(Long.class, range(1L, 200L)));
			add("sku", random(IntStream.range(1, 11).mapToObj(item -> String.format("SKU-%d", item)).toArray()));
			add("price", random(Double.class, range(1d, 200d)));
			add("quantity", random(Integer.class, range(1, 10)));
			add("virtual", false);
			add("createdAt", LocalDateTime.now());
			add("updatedAt", LocalDateTime.now());
		}});

		Fixture.of(OrderItem.class).addTemplate(NON_DISPATCHABLE_PRODUCT)
				.inherits(DISPATCHABLE_PRODUCT, new Rule() {{
					add("virtual", true);
				}});
	}
}
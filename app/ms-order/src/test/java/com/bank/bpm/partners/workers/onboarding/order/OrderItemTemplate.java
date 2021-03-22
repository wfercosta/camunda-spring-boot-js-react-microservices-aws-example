package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class OrderItemTemplate implements TemplateLoader {


	public static final String BASIC = "DISPATCHABLE_PRODUCT";

	@Override
	public void load() {
		Fixture.of(OrderItem.class).addTemplate(BASIC, new Rule() {{
			add("id", random(Long.class, range(1L, 200L)));
			add("sku", uniqueRandom(IntStream.range(1, 100).mapToObj(item -> String.format("SKU-%d", item)).toArray()));
			add("price", random(Double.class, range(1d, 200d)));
			add("quantity", random(Integer.class, range(1, 10)));
			add("createdAt", LocalDateTime.now());
			add("updatedAt", LocalDateTime.now());
		}});
	}
}

package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class ProductTemplate implements TemplateLoader {

	public static final String BASIC = "BASIC";
	public static final String DISPATCHABLE_WITH_INVENTORY_AVAILABLE = "DISPATCHABLE_WITH_INVENTORY_AVAILABLE";
	public static final String NON_DISPATCHABLE = "DISPATCHABLE_WITH_INVENTORY_ITEMS";

	@Override
	public void load() {
		Fixture.of(Product.class).addTemplate(BASIC, new Rule() {{
			add("id", random(Long.class, range(1L, 100L)));
			add("sku", random(IntStream.range(1, 11).mapToObj(item -> String.format("SKU-%d", item)).toArray()));
			add("amount", random(Integer.class, range(100, 1000)));
			add("dispatchable", Boolean.TRUE);
			add("createdAt", LocalDateTime.now());
			add("updatedAt", LocalDateTime.now());
		}});

		Fixture.of(Product.class).addTemplate(DISPATCHABLE_WITH_INVENTORY_AVAILABLE).inherits(BASIC, new Rule() {{
			add("dispatchable", Boolean.TRUE);
		}});

		Fixture.of(Product.class).addTemplate(NON_DISPATCHABLE).inherits(BASIC, new Rule() {{
			add("amount", 0);
			add("dispatchable", Boolean.FALSE);
		}});

	}
}

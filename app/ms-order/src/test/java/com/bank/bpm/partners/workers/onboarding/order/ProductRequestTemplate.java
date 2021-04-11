package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

import java.util.stream.IntStream;

public class ProductRequestTemplate implements TemplateLoader {

	public static final String BASIC = "BASIC";

	@Override
	public void load() {
		Fixture.of(ProductRequest.class).addTemplate(BASIC, new Rule() {{
			add("sku", uniqueRandom(IntStream.range(1, 100).mapToObj(item -> String.format("SKU-%d", item)).toArray()));
			add("amount", random(Integer.class, range(0, 500)));
			add("dispatchable", Boolean.TRUE);
		}});
	}

}

package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ProductRequestTemplate implements TemplateLoader {

	public static final String BASIC = "BASIC";

	@Override
	public void load() {
		Fixture.of(ProductRequest.class).addTemplate(BASIC, new Rule() {{
			add("sku", random("APPLE-MACBOOKPRO-15-ALUMINIUM", "MICRO-XBOX-BLACK-SERIESX"));
			add("amount", random(Integer.class, range(0, 500)));
			add("dispatchable", Boolean.TRUE);
		}});
	}

}

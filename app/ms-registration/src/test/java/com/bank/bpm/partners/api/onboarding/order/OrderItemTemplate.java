package com.bank.bpm.partners.api.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.bank.bpm.partners.workers.onboarding.order.OrderItem;

public class OrderItemTemplate implements TemplateLoader {


	public static final String DISPATCHABLE_PRODUCT = "DISPATCHABLE_PRODUCT";
	public static final String NON_DISPATCHABLE_PRODUCT = "NON_DISPATCHABLE_PRODUCT";

	@Override
	public void load() {
		Fixture.of(OrderItem.class).addTemplate(DISPATCHABLE_PRODUCT, new Rule() {{
			add("id", random(Long.class, range(1L, 200L)));
			add("virtual", false);
		}});

		Fixture.of(OrderItem.class).addTemplate(NON_DISPATCHABLE_PRODUCT)
				.inherits(DISPATCHABLE_PRODUCT, new Rule() {{
					add("virtual", true);
				}});
	}
}

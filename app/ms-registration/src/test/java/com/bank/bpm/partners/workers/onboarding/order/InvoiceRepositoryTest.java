package com.bank.bpm.partners.workers.onboarding.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(value = "/db/migrations/h2/data/load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/migrations/h2/data/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class InvoiceRepositoryTest {


	@Autowired
	private InvoiceRepository sut;

	@Autowired
	private OrderRepository orderRepository;


	@Test
	@DisplayName("Should save a new inventory")
	public void Should_save_new_inventory() {

		//Arrange
		final Long orderId = 3L;
		final Order order = orderRepository.findById(orderId).orElse(null);

		order.setStatus(OrderStatus.ORDER_INVOICED);

		final Invoice invoice = Invoice.builder()
				.total(order.getItems().stream()
						.map(OrderItem::getPrice)
						.reduce(0d, Double::sum))
				.order(order)
				.build();

		//Act
		Invoice saved = sut.save(invoice);

		//Asserts
		assertThat(saved.getId(), greaterThan(0L));
		assertThat(saved.getOrder(), notNullValue());
		assertThat(saved.getOrder().getStatus(), is(OrderStatus.ORDER_INVOICED));

	}


}

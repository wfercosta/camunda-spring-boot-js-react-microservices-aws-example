package com.bank.bpm.partners.workers.onboarding.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.bank.bpm.partners.workers.onboarding.order.OrderStatus.ORDER_NEW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(value = "/db/migrations/h2/data/load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/migrations/h2/data/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository sut;

	@Test
	@DisplayName("Should return order details When its id exists")
	public void Should_return_order_details_When_its_id_exists() {

		//Arrange
		final Long fixture = 1L;

		//Act
		final Optional<Order> result = sut.findById(fixture);

		//Asserts
		assertThat(result, notNullValue());
		assertThat(result.isPresent(), is(Boolean.TRUE));
	}

	@Test
	@DisplayName("Should return order details Whe its id exists and and with the required status")
	public void Should_return_order_details_When_exists_and_with_required_status() {
		//Arrange
		final Long fixture = 2L;

		//Act
		final Optional<Order> result = sut.findByIdAndStatus(fixture, ORDER_NEW);

		//Asserts
		assertThat(result, notNullValue());
		assertThat(result.isPresent(), is(Boolean.TRUE));
	}
}

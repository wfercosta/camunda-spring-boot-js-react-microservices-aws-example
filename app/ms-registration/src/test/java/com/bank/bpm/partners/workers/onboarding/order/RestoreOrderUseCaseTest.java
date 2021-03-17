package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RestoreOrderUseCaseTest {

	private RestoreOderUseCase sut;

	@Mock
	private OrderRepository repository;


	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new RestoreOderUseCase(repository);
	}

	@Test
	@DisplayName("Should restore the order details When its id is valid")
	public void Should_restore_order_When_its_id_is_valid() {

		//Arrange
		final Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.NEEDS_PAYMENT_AND_PHYSICAL);
		when(repository.findById(fixture.getId())).thenReturn(Optional.of(fixture));

		//Act
		final Optional<Order> result = this.sut.execute(fixture.getId());

		//Assert
		verify(repository).findById(eq(fixture.getId()));
		assertThat(result, notNullValue());
		assertThat(result.isPresent(), is(Boolean.TRUE));
	}

	@Test
	@DisplayName("Should return empty When it is invalid order id")
	public void Should_return_empty_When_it_is_invalid_order_id() {

		//Arrange
		final Long fixture = 100L;
		when(repository.findById(fixture)).thenReturn(Optional.empty());

		//Act
		final Optional<Order> result = this.sut.execute(fixture);

		//Assert
		verify(repository).findById(fixture);
		assertThat(result, notNullValue());
		assertThat(result.isPresent(), is(Boolean.FALSE));

	}

}

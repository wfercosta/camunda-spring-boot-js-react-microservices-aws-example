package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.bank.bpm.partners.workers.onboarding.order.OrderStatus.ORDER_NEW;
import static com.bank.bpm.partners.workers.onboarding.order.OrderStatus.ORDER_PROCESSING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Tag(TestType.UNIT)
public class RestoreOrderUseCaseTest {

	private RestoreOrderUseCase sut;

	@Mock
	private OrderRepository repository;


	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new RestoreOrderUseCase(repository);
	}

	@Test
	@DisplayName("Should restore the order with processing state When the order exists and its state is new")
	public void Should_restore_order_processing_state_When_order_exists_and_state_new() {

		//Arrange
		final Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.BASIC);
		final Order fixtureUpdated = fixture.toBuilder().status(ORDER_PROCESSING).build();

		when(repository.findByIdAndStatus(fixture.getId(), ORDER_NEW)).thenReturn(Optional.of(fixture));
		when(repository.save(fixtureUpdated)).thenReturn(fixtureUpdated);

		//Act
		final Order result = this.sut.execute(fixture.getId()).orElse(null);

		//Assert
		verify(repository).findByIdAndStatus(eq(fixture.getId()), eq(ORDER_NEW));
		verify(repository).save(eq(fixtureUpdated));

		assertThat(result, notNullValue());
		assertThat(result.getStatus(), is(ORDER_PROCESSING));
	}

	@Test
	@DisplayName("Should return empty When order not exists")
	public void Should_return_empty_When_order_not_exists() {

		//Arrange
		final Long fixture = 100L;
		when(repository.findByIdAndStatus(fixture, ORDER_NEW)).thenReturn(Optional.empty());

		//Act
		final Order result = this.sut.execute(fixture).orElse(null);

		//Assert
		verify(repository).findByIdAndStatus(eq(fixture), eq(ORDER_NEW));
		assertThat(result, nullValue());

	}

}

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
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RequestPaymentExternalUseCaseTest {

	@Mock
	private PaymentApiClient client;

	@Mock
	private OrderRepository repository;

	private RequestPaymentExternalUseCase sut;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new RequestPaymentExternalUseCase(client, repository);
	}

	@Test
	@DisplayName("Should return the correlation id When the payment request was successfully received")
	public void Should_return_correlation_id_When_payment_request_successfully_received() {

		//Arrange
		final Order order = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);
		final CreatePaymentResponse response = CreatePaymentResponse.builder()
				.correlationId(UUID.randomUUID().toString())
				.build();

		when(repository.findByIdAndStatus(order.getId(), OrderStatus.ORDER_PROCESSING)).thenReturn(Optional.of(order));
		when(client.create(any(CreatePaymentRequest.class))).thenReturn(response);

		//Act
		String ticket = sut.execute(order).orElse(null);

		//Asserts
		verify(repository).findByIdAndStatus(eq(order.getId()), eq(OrderStatus.ORDER_PROCESSING));
		verify(client).create(any(CreatePaymentRequest.class));
		assertThat(ticket, notNullValue());
	}
}

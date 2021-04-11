package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Tag(TestType.UNIT)
public class ProcessInvoiceUseCaseTest {

	@Mock
	private InvoiceRepository invoiceRepository;

	@Mock
	OrderRepository orderRepository;

	private ProcessInvoiceUseCase sut;


	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new ProcessInvoiceUseCase(invoiceRepository, orderRepository);
	}

	@Test
	@DisplayName("Should return the invoice created and order state invoiced When order exists and is in the right state")
	public void Should_return_invoice_created_order_state_invoiced_When_order_exists_and_state_pending_payment() {

		//Arrange
		final Invoice invoice = Fixture.from(Invoice.class)
				.uses(new InvoiceTemplate.InvoiceTotalProcessor())
				.gimme(InvoiceTemplate.BASIC);

		final Order order = invoice.getOrder();
		final Long orderId = order.getId();

		when(orderRepository.findByIdAndStatus(orderId, OrderStatus.ORDER_PENDING_PAYMENT)).thenReturn(Optional.of(order));
		when(invoiceRepository.save(any())).thenReturn(invoice);

		//Act
		final Invoice result = sut.execute(order)
				.orElse(null);

		//Assertions
		verify(orderRepository).findByIdAndStatus(eq(orderId)
				, eq(OrderStatus.ORDER_PENDING_PAYMENT));

		verify(invoiceRepository).save(any(Invoice.class));

		assertThat(result, notNullValue());
		assertThat(result.getTotal(), is(invoice.getTotal()));
		assertThat(result.getOrder().getStatus(), is(OrderStatus.ORDER_INVOICED));
	}

	@Test
	@DisplayName("Should return empty when order id does not exist or not in order not pending payment status")
	public void Should_return_empty_When_order_does_not_exist_or_not_in_pending_payment_status() {

		//Arrange
		final Order order = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);

		final Long orderId = order.getId();

		when(orderRepository.findByIdAndStatus(orderId, OrderStatus.ORDER_PENDING_PAYMENT)).thenReturn(Optional.empty());

		//Act
		final Invoice result = sut.execute(order).orElse(null);

		//Assertions
		verify(orderRepository).findByIdAndStatus(eq(orderId), eq(OrderStatus.ORDER_PENDING_PAYMENT));
		assertThat(result, nullValue());

	}


}

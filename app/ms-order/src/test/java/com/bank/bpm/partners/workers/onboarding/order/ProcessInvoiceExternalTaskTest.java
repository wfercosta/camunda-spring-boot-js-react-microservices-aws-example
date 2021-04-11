package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Tag(TestType.UNIT)
public class ProcessInvoiceExternalTaskTest {

	private ProcessInvoiceExternalTask sut;

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Mock
	private ProcessInvoiceUseCase useCase;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCaptor;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new ProcessInvoiceExternalTask(objectMapper, useCase);
	}

	@Test
	@DisplayName("Should return order with status invoiced and the invoice When the order is with status pending payment")
	public void Should_return_order_status_invoiced_and_invoice_When_order_status_pending_payment() throws JsonProcessingException {

		//Arrange
		final Invoice invoice = Fixture.from(Invoice.class).gimme(InvoiceTemplate.BASIC);

		when(externalTask.getVariable("order")).thenReturn(objectMapper.writeValueAsString(invoice.getOrder()));
		when(useCase.execute(invoice.getOrder()))
				.thenReturn(Optional.of(invoice
						.toBuilder()
						.order(invoice
								.getOrder().toBuilder()
								.status(OrderStatus.ORDER_INVOICED)
								.build())
						.build()));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Asserts
		verify(useCase).execute(eq(invoice.getOrder()));
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		final Map<String, Object> captured = variablesCaptor.getValue();
		assertThat(captured, hasKey("invoice"));
		assertThat(captured, hasKey("order"));
		assertThat((String) captured.get("order"), containsString(OrderStatus.ORDER_INVOICED.name()));

	}

	@Test
	@DisplayName("Should throws an error Invoice Not Processed When it is not present")
	public void Should_throws_error_invoice_not_processed_When_it_is_not_present() throws JsonProcessingException {

		//Arrange
		final Invoice invoice = Fixture.from(Invoice.class).gimme(InvoiceTemplate.BASIC);

		when(externalTask.getVariable("order")).thenReturn(objectMapper.writeValueAsString(invoice.getOrder()));
		when(useCase.execute(invoice.getOrder())).thenReturn(Optional.empty());

		//Act & Assert
		assertThrows(InvoiceNotProcessedException.class, () -> sut.execute(externalTask, externalTaskService));

	}
}

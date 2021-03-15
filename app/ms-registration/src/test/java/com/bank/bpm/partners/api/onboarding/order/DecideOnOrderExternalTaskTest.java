package com.bank.bpm.partners.api.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.bank.bpm.partners.workers.onboarding.order.DecideOnOrderExternalTask;
import com.bank.bpm.partners.workers.onboarding.order.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DecideOnOrderExternalTaskTest {

	private DecideOnOrderExternalTask sut;

	private ExternalTask externalTask;
	private ExternalTaskService externalTaskService;

	@Spy
	private ObjectMapper mapper = new ObjectMapper();

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCaptor;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		this.mapper = new ObjectMapper();
		this.externalTask = mock(ExternalTask.class);
		this.externalTaskService = mock(ExternalTaskService.class);
		this.sut = new DecideOnOrderExternalTask(this.mapper);
	}


	@Test
	@DisplayName("Should return requires payment and reserve warehouse When item has cost and is physical")
	public void Should_return_requires_payment_and_reserve_warehouse_When_item_has_cost_and_is_Physical() throws JsonProcessingException {

		//Arrange
		Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.NEEDS_PAYMENT_AND_PHYSICAL);
		when(externalTask.getVariable("order")).thenReturn(this.mapper.writeValueAsString(fixture));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assertion
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasEntry("is_make_payment", Boolean.TRUE));
		assertThat(captured, hasEntry("is_physical", Boolean.TRUE));

	}

	@Test
	@DisplayName("Should return  no payment required and reserve warehouse When item has cost equals zero and is physical")
	public void Should_return_requires_no_payment_required_and_reserve_warehouse_When_item_has_cost_equals_zero_and_is_physical() throws JsonProcessingException {

		//Arrange
		Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.NO_PAYMENT_IS_NEEDED);
		when(externalTask.getVariable("order")).thenReturn(this.mapper.writeValueAsString(fixture));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assert
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasEntry("is_make_payment", Boolean.FALSE));
		assertThat(captured, hasEntry("is_physical", Boolean.TRUE));
	}


	@Test
	@DisplayName("Should return requires payment and no warehouse reservation is needed when has cost greater than zero and no id product")
	public void Should_return_requires_payment_and_no_warehouse_reservation_is_needed_when_has_cost_greater_than_zero_and_no_product_id() throws JsonProcessingException {

		Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.NO_WAREHOUSE_RESERVATION);
		when(externalTask.getVariable("order")).thenReturn(this.mapper.writeValueAsString(fixture));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assert
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasEntry("is_make_payment", Boolean.TRUE));
		assertThat(captured, hasEntry("is_physical", Boolean.FALSE));

	}

}

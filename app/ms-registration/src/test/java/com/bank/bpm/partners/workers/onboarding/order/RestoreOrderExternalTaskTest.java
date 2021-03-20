package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
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
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RestoreOrderExternalTaskTest {

	private RestoreOrderExternalTask sut;

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Mock
	private RestoreOderUseCase useCase;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCaptor;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		this.sut = new RestoreOrderExternalTask(new ObjectMapper(), useCase);
	}

	@Test
	@DisplayName("Should return requires payment and reserve warehouse When item has cost and is physical")
	public void Should_return_requires_payment_and_reserve_warehouse_When_item_has_cost_and_is_Physical() throws JsonProcessingException {

		//Arrange
		Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.NO_DISPATCHABLE_PRODUCT);
		when(externalTask.getVariable("order_id")).thenReturn(fixture.getId());
		when(useCase.execute(fixture.getId())).thenReturn(Optional.of(fixture));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assertion
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasEntry("has_dispatchable", Boolean.TRUE));
		assertThat(captured, hasKey("order"));

	}

	@Test
	@DisplayName("Should return requires payment and no warehouse reservation is needed when has cost greater than zero and no id product")
	public void Should_return_requires_payment_and_no_warehouse_reservation_is_needed_when_has_cost_greater_than_zero_and_no_product_id() throws JsonProcessingException {

		//Arrange
		Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.NO_WAREHOUSE_RESERVATION);
		when(externalTask.getVariable("order_id")).thenReturn(fixture.getId());
		when(useCase.execute(fixture.getId())).thenReturn(Optional.of(fixture));

		System.out.println("------------------------> " + fixture);

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assert
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasEntry("has_dispatchable", Boolean.FALSE));
		assertThat(captured, hasKey("order"));

	}

	@Test
	@DisplayName("Should throw order not found When it is not found")
	public void Should_throw_order_not_found_When_it_is_not_found() {

		//Arrange
		final Long fixture = 100L;
		when(externalTask.getVariable("order_id")).thenReturn(fixture);
		when(useCase.execute(fixture)).thenReturn(Optional.empty());

		//Act & Assert
		assertThrows(OrderNotFoundException.class, () -> sut.execute(externalTask, externalTaskService));

	}

}

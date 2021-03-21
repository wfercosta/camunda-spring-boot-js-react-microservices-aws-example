package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ReserveItemsInWarehouseExternalTaskTest {

	public static final Void VOID = null;
	public static final Void VOID_OBJECT_VALUE = VOID;
	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Mock
	private ReserveItemsInWarehouseUseCase useCase;

	private ReserveItemsInWarehouseExternalTask sut;
	
	private ObjectMapper objectMapper;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		objectMapper = new ObjectMapper();
		sut = new ReserveItemsInWarehouseExternalTask(objectMapper, useCase);
	}

	@Test
	@DisplayName("Should return reserved When all dispatchable items are successfully reserved")
	public void Should_return_reserved_When_all_dispatchable_items_are_successfully_reserved() throws Exception {

		//Arrange
		final Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);
		when(externalTask.getVariable("order")).thenReturn(objectMapper.writeValueAsString(fixture));
		doNothing().when(useCase).execute(fixture);

		//Act
		sut.execute(externalTask, externalTaskService);

		//Asserts
		verify(useCase).execute(eq(fixture));
		verify(externalTaskService).complete(eq(externalTask));

	}

}

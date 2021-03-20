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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ReserveItemsInWarehouseExternalTaskTest {

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Mock
	private ReserveItemsInWarehouseUseCase useCase;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCaptor;

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
		final Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.STATE_PROCESSING_DISPATCHABLE_PRODUCT);
		when(externalTask.getVariable("order")).thenReturn(objectMapper.writeValueAsString(fixture));
		when(useCase.execute(fixture)).thenReturn(Boolean.TRUE);

		//Act
		sut.execute(externalTask, externalTaskService);

		//Asserts
		verify(useCase).execute(eq(fixture));
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		final Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasEntry("is_items_reserved", Boolean.TRUE));
	}
	
}

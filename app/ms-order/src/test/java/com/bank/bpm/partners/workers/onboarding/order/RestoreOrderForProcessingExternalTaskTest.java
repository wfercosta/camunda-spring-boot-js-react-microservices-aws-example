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
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Tag(TestType.UNIT)
public class RestoreOrderForProcessingExternalTaskTest {

	private RestoreOrderForProcessingExternalTask sut;

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Mock
	private RestoreOrderUseCase useCase;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCaptor;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		this.sut = new RestoreOrderForProcessingExternalTask(new ObjectMapper(), useCase);
	}

	@Test
	@DisplayName("Should restore order as dispatchable when the product is not virtual")
	public void Should_restore_order_as_dispatchable_When_the_product_is_not_virtual() throws JsonProcessingException {

		//Arrange
		Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);
		when(externalTask.getVariable("order_id")).thenReturn(fixture.getId());
		when(useCase.execute(fixture.getId())).thenReturn(Optional.of(fixture));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assertion
		verify(useCase).execute(eq(fixture.getId()));
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasKey("order"));

	}

	@Test
	@DisplayName("Should restore order as non dispatchable when the product is virtual")
	public void Should_restore_order_as_non_dispatchable_When_the_product_is_virtual() throws JsonProcessingException {

		//Arrange
		Order fixture = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);
		when(externalTask.getVariable("order_id")).thenReturn(fixture.getId());
		when(useCase.execute(fixture.getId())).thenReturn(Optional.of(fixture));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Assert
		verify(useCase).execute(eq(fixture.getId()));
		verify(externalTaskService).complete(eq(externalTask), variablesCaptor.capture());

		Map<String, Object> captured = variablesCaptor.getValue();

		assertThat(captured, hasKey("order"));

	}

	@Test
	@DisplayName("Should throw order not found when invalid ID")
	public void Should_throw_order_not_Found_When_invalid_ID() {

		//Arrange
		final Long fixture = 100L;
		when(externalTask.getVariable("order_id")).thenReturn(fixture);
		when(useCase.execute(fixture)).thenReturn(Optional.empty());

		//Act & Assert
		assertThrows(OrderNotFoundException.class, () -> sut.execute(externalTask, externalTaskService));
		verify(useCase).execute(eq(fixture));

	}

}

package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Sql(value = "/db/migrations/h2/data/load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/migrations/h2/data/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
		initializers = {WireMockInitializer.class},
		classes = {TestApplication.class}
)
public class ReserveItemsInWarehouseExternalTaskComponentTest {

	@Mock
	private ExternalTask externalTask;

	@Mock
	private ExternalTaskService externalTaskService;

	@Captor
	private ArgumentCaptor<Map<String, Object>> variablesCapture;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ReserveItemsInWarehouseExternalTask sut;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("Should reserve items in warehouse When order is valid and with status processing")
	public void Should_reserve_items_warehouse_When_order_valid_and_with_status_processing() throws JsonProcessingException {

		//Arrange
		final Order order = orderRepository.findById(5L).orElse(null);
		final List<String> skus = requireNonNull(order).getItems()
				.stream().map(OrderItem::getSku)
				.collect(Collectors.toList());

		when(externalTask.getVariable("order")).thenReturn(objectMapper.writeValueAsString(order));

		//Act
		sut.execute(externalTask, externalTaskService);

		//Asserts
		final List<Product> products = productRepository.findAllBySkuInAndDispatchableTrue(skus);
		final List<Integer> amounts = products.stream().map(Product::getAmount).collect(Collectors.toList());

		assertThat(amounts, is(Arrays.asList(499, 499)));

	}

}

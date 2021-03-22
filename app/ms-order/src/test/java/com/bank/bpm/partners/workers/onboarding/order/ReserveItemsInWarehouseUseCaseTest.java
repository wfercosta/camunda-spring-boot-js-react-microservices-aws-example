package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ReserveItemsInWarehouseUseCaseTest {

	private ReserveItemsInWarehouseUseCase sut;

	@Mock
	private ProductRepository repository;

	@Captor
	private ArgumentCaptor<Collection<Product>> variableCaptor;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(OrderTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new ReserveItemsInWarehouseUseCase(repository);
	}

	@Test
	@DisplayName("Should return all items reserved in stock When item is dispatchable")
	public void Should_return_all_items_reserved_stock_When_item_dispatchable() {

		//Arrange
		final Order order = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);
		final List<Product> products = Fixture.from(Product.class).gimme(order.getItems().size(), ProductTemplate.DISPATCHABLE_WITH_INVENTORY_AVAILABLE);
		final List<String> skus = order.getItems().stream().map(OrderItem::getSku).collect(Collectors.toList());

		equalizeSkuOn(order, products);

		when(repository.findAllBySkuInAndDispatchableTrue(skus)).thenReturn(products);

		//Act
		sut.execute(order);

		//Asserts
		verify(repository).findAllBySkuInAndDispatchableTrue(eq(skus));
		verify(repository).saveAll(variableCaptor.capture());

		final Collection<Product> captured = variableCaptor.getValue();

		assertThat(captured, hasSize(order.getItems().size()));

	}

	@Test
	@DisplayName("Should return reserved for all dispatchable items When it receive virtual and dispatchable")
	public void Should_return_reserved_for_all_dispatchable_items_When_it_receive_virtual_and_dispatchable() {

		//Arrange
		final Order order = Fixture.from(Order.class).gimme(OrderTemplate.BASIC_STATE_PROCESSING);
		final List<String> skus = order.getItems().stream().map(OrderItem::getSku).collect(Collectors.toList());
		final List<Product> products = Fixture.from(Product.class).gimme(order.getItems().size(), ProductTemplate.DISPATCHABLE_WITH_INVENTORY_AVAILABLE);

		equalizeSkuOn(order, products);

		when(repository.findAllBySkuInAndDispatchableTrue(skus)).thenReturn(products);

		//Act
		sut.execute(order);

		//Asserts
		verify(repository).findAllBySkuInAndDispatchableTrue(eq(skus));
		verify(repository).saveAll(variableCaptor.capture());

		final Collection<Product> captured = variableCaptor.getValue();

		assertThat(captured, hasSize(products.size()));

	}

	private void equalizeSkuOn(Order order, List<Product> products) {
		IntStream.range(0, order.getItems().size()).forEach(index -> {
			final OrderItem item = order.getItems().get(index);
			final Product product = products.get(index);
			product.setSku(item.getSku());
		});
	}

}

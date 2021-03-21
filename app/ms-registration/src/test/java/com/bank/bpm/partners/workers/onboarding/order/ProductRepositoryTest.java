package com.bank.bpm.partners.workers.onboarding.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@Sql(value = "/db/migrations/h2/data/load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/migrations/h2/data/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository sut;

	@Test
	@DisplayName("Should return all products filtering by sku and if is dispatchable")
	public void Should_return_all_products_filtering_by_sku_and_if_dispatchable() {

		//Arrange
		final List<String> skus = Arrays.asList("APPLE-MACBOOKPRO-15-ALUMINIUM", "MICRO-XBOX-BLACK-SERIESX");

		//Act
		List<Product> result = sut.findAllBySkuInAndDispatchableTrue(skus);

		//Assert
		assertThat(result, hasSize(skus.size()));

	}
}

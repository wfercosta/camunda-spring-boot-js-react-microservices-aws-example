package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.bank.bpm.partners.workers.onboarding.Application;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Sql(value = "/db/migrations/h2/data/load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/migrations/h2/data/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("test")
@Tag(TestType.INTEGRATION)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository sut;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(ProductTemplate.class.getPackageName());
	}

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

	@Test
	@DisplayName("Should throw data integrity violation When creating a product with existent SKU")
	public void Should_ThrowDataIntegrityViolation_When_CreatingProductWithExistentSKU() {

		//Arrange
		final Product fixture = Fixture.from(Product.class).gimme(ProductTemplate.BASIC);
		fixture.setSku("APPLE-MACBOOKPRO-15-ALUMINIUM");

		//Act | Assertions
		assertThrows(DataIntegrityViolationException.class, () -> sut.save(fixture));

	}

	@Test
	@DisplayName("Should save the product When it is valid record")
	public void Should_SaveProduct_When_ValidRecord() {
		//Arrange
		final Product fixture = Fixture.from(Product.class).gimme(ProductTemplate.BASIC);

		//Act
		final Product result = sut.save(fixture);

		//Asserts
		final Product actual = sut.findById(result.getId()).get();
		assertThat(actual, notNullValue());
	}
}

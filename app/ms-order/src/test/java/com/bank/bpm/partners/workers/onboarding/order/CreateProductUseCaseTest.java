package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Tag(TestType.UNIT)
public class CreateProductUseCaseTest {

	private CreateProductUseCase sut;

	@Mock
	private ProductRepository repository;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(ProductTemplate.class.getPackageName());
	}

	@BeforeEach
	public void beforeEach() {
		sut = new CreateProductUseCase(repository);
	}


	@Test
	@DisplayName("Should return product with id When it saved successfully")
	public void Should_ReturnProductID_When_SavedSuccessfully() {
		//Arrange
		final Product fixture = Fixture.from(Product.class).gimme(ProductTemplate.BASIC);
		when(repository.save(fixture)).thenReturn(fixture.toBuilder().id(1L).build());

		//Act
		final Product result = sut.execute(fixture);

		//Asserts
		verify(repository).save(eq(fixture));

		assertThat(result, notNullValue());
		assertThat(result.getId().intValue(), greaterThan(0));

	}

	@Test
	@DisplayName("Should return duplicated error When sku already existis")
	public void Should_ReturnDuplicatedError_When_SKUAlreadyExistis() {

		//Arrange
		final Product fixture = Fixture.from(Product.class).gimme(ProductTemplate.BASIC);

		when(repository.save(fixture)).thenThrow(DataIntegrityViolationException.class);

		//Act | Assertions
		assertThrows(DuplicatedProductSKUException.class, () -> sut.execute(fixture));
	}

}

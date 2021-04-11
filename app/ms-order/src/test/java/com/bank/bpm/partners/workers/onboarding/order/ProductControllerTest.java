package com.bank.bpm.partners.workers.onboarding.order;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ProductController.class)
@Tag(TestType.UNIT)
public class ProductControllerTest {

	public static final String EMPTY_JSON = "{}";
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateProductUseCase useCase;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(ProductTemplate.class.getPackageName());
	}

	@Test
	@DisplayName("Should return status created When the product is valid and meets the business rules")
	public void Should_ReturnStatusCreated_When_ProductValidAndMeetsBusinessRules() throws Exception {

		//Arrange
		final ProductRequest fixture = Fixture.from(ProductRequest.class).gimme(ProductRequestTemplate.BASIC);
		final Product product = fixture.toProduct();

		when(useCase.execute(eq(product))).thenReturn(product.toBuilder().id(1L).build());

		//Act | Assertions
		mockMvc.perform(
				post("/v1/products")
						.contentType(APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(fixture)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.id", greaterThan(0)));

	}

	@Test
	@DisplayName("Should return status bad request When the product does not have required parameters")
	public void Should_ReturnStatusBadRequest_When_ProductDoesNotHaveRequiredParameters() throws Exception {

		final int NUMBER_OF_FAILING_FIELDS = 3;

		//Act | Assertions
		mockMvc.perform(
				post("/v1/products")
						.contentType(APPLICATION_JSON_VALUE)
						.content(EMPTY_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.messages", hasSize(NUMBER_OF_FAILING_FIELDS)));

	}

	@Test
	@DisplayName("Should return status Unprocessable entity When the product has a duplicated SKU")
	public void Should_ReturnStatusUnprocessableEntity_When_ProductHasDuplicateSKU() throws Exception {

		//Arrange
		final ProductRequest fixture = Fixture.from(ProductRequest.class).gimme(ProductRequestTemplate.BASIC);
		final Product product = fixture.toProduct();

		when(useCase.execute(product)).thenThrow(new DuplicatedProductSKUException(product.getSku()));

		//Act | Assertions
		mockMvc.perform(post("/v1/products")
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(fixture)))
				.andDo(print())
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.messages", hasSize(1)))
				.andExpect(jsonPath("$.messages[0]", containsStringIgnoringCase("duplicated")));

	}
}

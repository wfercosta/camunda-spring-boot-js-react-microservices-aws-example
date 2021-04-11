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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Sql(value = "/db/migrations/h2/data/load.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db/migrations/h2/data/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(
		initializers = {WireMockInitializer.class},
		classes = {TestApplication.class}
)
@Tag(TestType.COMPONENT)
public class ProductControllerComponentTest {

	public static final String EMPTY_JSON = "{}";

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	public static void beforeAll() {
		FixtureFactoryLoader.loadTemplates(ProductTemplate.class.getPackageName());
	}

	@Test
	@DisplayName("Should return status as created When Product is created successfully")
	public void Should_ReturnStatusCreated_When_ProductCreatedSuccessfully() throws Exception {
		//Arrange
		final ProductRequest request = Fixture.from(ProductRequest.class).gimme(ProductRequestTemplate.BASIC);

		//Act | Asserts
		mockMvc.perform(
				post("/v1/products")
						.contentType(APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.id", greaterThan(0)));

	}

	@Test
	@DisplayName("Should return bad request with error messages When product record missing required field")
	public void Should_ReturnBadRequestWithErrorMessages_When_ProductRecordMissingRequiredFields() throws Exception {
		//Arrange | Act | Asserts
		mockMvc.perform(
				post("/v1/products")
						.contentType(APPLICATION_JSON_VALUE)
						.content(EMPTY_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.messages", hasSize(greaterThan(0))));
	}


}

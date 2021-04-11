package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

	private final CreateProductUseCase useCase;

	public ProductController(CreateProductUseCase useCase) {
		this.useCase = useCase;
	}

	@ResponseStatus(code = CREATED)
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEnvelop<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
		final Product out = useCase.execute(request.toProduct());

		return ResponseEnvelop.of(ProductResponse.from(out));

	}
}

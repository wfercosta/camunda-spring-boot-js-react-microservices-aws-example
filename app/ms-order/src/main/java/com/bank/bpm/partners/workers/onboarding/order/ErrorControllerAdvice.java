package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ErrorControllerAdvice {


	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ResponseEnvelop<?>> applicationException(ApplicationException exception) {
		return ResponseEntity.unprocessableEntity()
				.body(ResponseEnvelop.ofMessages(Collections.singletonList(exception.getMessage())));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseEnvelop<?>> constraintViolationException(final MethodArgumentNotValidException exception) {
		final List<String> messages = exception.getBindingResult().getFieldErrors()
				.stream()
				.map(field -> String.format("Field with name %s has an error: %s", field.getField(), field.getDefaultMessage()))
				.collect(Collectors.toList());

		return ResponseEntity.badRequest()
				.body(ResponseEnvelop.ofMessages(messages));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseEnvelop<?>> defaultHandler() {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(ResponseEnvelop.createInternalServerError());
	}
}


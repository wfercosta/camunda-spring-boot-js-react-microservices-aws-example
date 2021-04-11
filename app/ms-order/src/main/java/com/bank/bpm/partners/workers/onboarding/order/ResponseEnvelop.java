package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseEnvelop<E> {

	private E data;
	private List<String> messages;

	public ResponseEnvelop(E data) {
		this.data = data;
	}

	public ResponseEnvelop(List<String> messages) {
		this.messages = messages;
	}

	public static <R> ResponseEnvelop<R> of(R data) {
		return new ResponseEnvelop<>(data);
	}

	public static ResponseEnvelop<?> createInternalServerError() {
		return new ResponseEnvelop<>(Collections.singletonList("INTERNAL SERVER ERROR"));
	}

	public static ResponseEnvelop<?> ofMessages(List<String> messages) {
		return new ResponseEnvelop<>(messages);
	}
}

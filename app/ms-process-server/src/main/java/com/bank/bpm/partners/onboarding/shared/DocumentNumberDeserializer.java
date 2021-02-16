package com.bank.bpm.partners.onboarding.shared;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class DocumentNumberDeserializer extends StdDeserializer<DocumentNumber> {

	protected DocumentNumberDeserializer() {
		this(null);
	}

	protected DocumentNumberDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public DocumentNumber deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		return DocumentNumber.of(node.asText());
	}
}

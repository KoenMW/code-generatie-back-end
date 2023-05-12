package com.Inholland.NovaBank.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AccountListSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Account account : (Iterable<Account>) o) {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("iban", account.getIban());
            jsonGenerator.writeNumberField("balance", account.getBalance());
            jsonGenerator.writeStringField("accountType", account.getAccountType().toString());
            jsonGenerator.writeBooleanField("status", account.isActive());
            jsonGenerator.writeNumberField("absoluteLimit", account.getAbsoluteLimit());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}

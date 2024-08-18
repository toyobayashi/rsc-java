package com.toyobayashi.rsc.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.toyobayashi.rsc.core.ClientReference;

public class ClientReferenceSerializer extends StdSerializer<ClientReference> {
  public ClientReferenceSerializer() {
    super(ClientReference.class);
  }

  @Override
  public void serialize(ClientReference value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    jgen.writeStartArray();
    jgen.writeString(value.$$id);
    jgen.writeStartArray();
    jgen.writeString(value.$$id);
    jgen.writeEndArray();
    jgen.writeString(value.name);
    if (value.$$async) {
      jgen.writeNumber(1);
    }
    jgen.writeEndArray();
  }
}

package com.toyobayashi.rsc.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.toyobayashi.rsc.core.ClientReference;
import com.toyobayashi.rsc.core.RequestInstance;

public class ClientReferenceIdSerializer extends BaseSerializer<ClientReference> {
  public ClientReferenceIdSerializer(RequestInstance request) {
    super(ClientReference.class, request);
  }

  @Override
  public void serialize(ClientReference value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    serializeClientReference(false, value, jgen, provider);
  }
}

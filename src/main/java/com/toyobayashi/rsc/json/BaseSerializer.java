package com.toyobayashi.rsc.json;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.toyobayashi.rsc.core.ClientReference;
import com.toyobayashi.rsc.core.RequestInstance;

public abstract class BaseSerializer<T> extends StdSerializer<T> {
  protected RequestInstance request;

  public BaseSerializer(Class<T> clazz, RequestInstance request) {
    super(clazz);
    this.request = request;
  }

  protected static String serializeByValueID(int id) {
    return "$" + Integer.toHexString(id);
  }

  protected static String serializeLazyID(int id) {
    return "$L" + Integer.toHexString(id);
  }

  protected void serializeClientReference(boolean isInJSX, ClientReference clientReference, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    String clientReferenceKey = clientReference.$$async
      ? clientReference.$$id + "#async"
      : clientReference.$$id;
    HashMap<String, Integer> writtenClientReferences = request.writtenClientReferences;
    Integer id = writtenClientReferences.get(clientReferenceKey);
    if (id != null) {
      jgen.writeString(isInJSX ? serializeLazyID(id) : serializeByValueID(id));
      return;
    }
    try {
      request.pendingChunks++;
      Integer importId = request.getNextChunkId();

      ObjectMapper mapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addSerializer(new ClientReferenceSerializer());
      mapper.registerModule(module);
      String json = mapper.writeValueAsString(clientReference);

      String processedChunk = Integer.toHexString(importId) + ":I" + json + "\n";
      request.addCompletedImportChunk(processedChunk);
      writtenClientReferences.put(clientReferenceKey, importId);
      jgen.writeString(isInJSX ? serializeLazyID(importId) : serializeByValueID(importId));
    } catch (Exception e) {
      request.pendingChunks++;
      int parent = request.getNextChunkId();
      request.emitErrorChunk(parent, e);
      jgen.writeString(serializeByValueID(parent));
    }
  }
}

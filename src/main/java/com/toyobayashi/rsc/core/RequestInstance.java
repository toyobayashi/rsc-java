package com.toyobayashi.rsc.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.toyobayashi.rsc.json.ClientReferenceIdSerializer;
import com.toyobayashi.rsc.json.JSXElementSerializer;

public class RequestInstance {
  public int nextChunkId = 0;
  public int pendingChunks = 0;

  public WeakHashMap<Object, String> writtenObjects = new WeakHashMap<>();
  public HashMap<String, Integer> writtenClientReferences = new HashMap<>();
  public ArrayList<String> completedImportChunks = new ArrayList<>();
  public ArrayList<String> completedRegularChunks = new ArrayList<>();
  public ArrayList<String> completedErrorChunks = new ArrayList<>();
  public JSXElementSerializer jsxTreeSerializer = new JSXElementSerializer(this);
  public ClientReferenceIdSerializer clientReferenceIdSerializer = new ClientReferenceIdSerializer(this);

  public boolean addCompletedImportChunk(String chunk) {
    return completedImportChunks.add(chunk);
  }

  public boolean addCompletedRegularChunk(String chunk) {
    return completedRegularChunks.add(chunk);
  }

  public boolean addCompletedErrorChunk(String chunk) {
    return completedErrorChunks.add(chunk);
  }

  public void emitModelChunk(int id, String json) {
    this.completedRegularChunks.add(Integer.toHexString(id) + ":" + json + "\n");
  }

  public void emitErrorChunk(int id, Object digest) {
    String json;
    try {
      json = new ObjectMapper().writeValueAsString(digest);
    } catch (Exception e) {
      json = "\"Error serializing digest: " + e.getMessage() + "\"";
    }
    this.completedErrorChunks.add(Integer.toHexString(id) + ":E{\"digest\":" + json + "}\n");
  }

  public void renderModel(Object model) throws IOException {
    // if (writtenObjects.containsKey(model)) {
    //   return;
    // }
    int id = getNextChunkId();
    // writtenObjects.put(model, Integer.toHexString(id));

    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(jsxTreeSerializer);
    module.addSerializer(clientReferenceIdSerializer);
    mapper.registerModule(module);

    emitModelChunk(id, mapper.writeValueAsString(model));
  }

  public int getNextChunkId() {
    return nextChunkId++;
  }
}

package com.toyobayashi.rsc.core;

import java.util.HashMap;
import java.util.AbstractMap.SimpleImmutableEntry;

public class PropsBuilder {
  private HashMap<String, Object> props = new HashMap<>();

  public static class Entry extends SimpleImmutableEntry<String, Object> {
    public Entry(String key, Object value) {
      super(key, value);
    }
  }

  public static PropsBuilder empty() {
    return new PropsBuilder();
  }

  public static Entry entry(String key, Object value) {
    return new Entry(key, value);
  }

  public static PropsBuilder of(Entry[] entries) {
    PropsBuilder builder = new PropsBuilder();
    for (Entry entry : entries) {
      builder.put(entry.getKey(), entry.getValue());
    }
    return builder;
  }

  public PropsBuilder put(String key, Object value) {
    props.put(key, value);
    return this;
  }

  public HashMap<String, Object> build() {
    return new HashMap<>(props);
  }
}

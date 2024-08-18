package com.toyobayashi.rsc.json;

@FunctionalInterface
public interface Replacer {
  Object replace(Object key, Object value, Object parent, Object root);
}

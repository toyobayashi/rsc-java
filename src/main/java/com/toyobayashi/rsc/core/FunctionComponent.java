package com.toyobayashi.rsc.core;

import java.util.HashMap;

@FunctionalInterface
public interface FunctionComponent {
  Object render(HashMap<String, Object> props);
}

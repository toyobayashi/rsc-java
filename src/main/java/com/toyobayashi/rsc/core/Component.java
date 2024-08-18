package com.toyobayashi.rsc.core;

import java.util.HashMap;

public class Component {
  protected HashMap<String, Object> props;

  public Component(HashMap<String, Object> props) {
    this.props = props == null ? new HashMap<>() : props;
  }

  public Object render() {
    return null;
  }
}

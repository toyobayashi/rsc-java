package com.toyobayashi.rsc.components;

import java.util.HashMap;

import com.toyobayashi.rsc.core.Component;
import com.toyobayashi.rsc.core.JSXElement;
import com.toyobayashi.rsc.core.PropsBuilder;

public class Head extends Component {
  public Head(HashMap<String, Object> props) {
    super(props);
  }

  @Override
  public JSXElement render() {
    return JSXElement.h("head", null,
      JSXElement.h("meta", PropsBuilder.empty().put("charset", "utf-8").build()),
      JSXElement.h("meta", PropsBuilder.empty().put("viewport", "width=device-width, initial-scale=1").build()),
      JSXElement.h("title", null, "React Servre Component in Java"),
      this.props.get("children")
    );
  }
}

package com.toyobayashi.rsc.app;

import java.util.HashMap;

import com.toyobayashi.rsc.components.Head;
import com.toyobayashi.rsc.components.ServerTime;
import com.toyobayashi.rsc.core.ClientReference;
import com.toyobayashi.rsc.core.Component;
import com.toyobayashi.rsc.core.JSXElement;
import com.toyobayashi.rsc.core.PropsBuilder;

public class Index extends Component {

  public Index(HashMap<String, Object> props) {
    super(props);
  }

  @Override
  public JSXElement render() {
    return JSXElement.h("html", PropsBuilder.empty().put("lang", "en").build(),
      JSXElement.h(Head.class, null, this.props.get("scripts")),
      JSXElement.h("body", null,
        JSXElement.h("h1", null, "React Server Component in Java"),
        JSXElement.h(ServerTime.class, PropsBuilder.empty()
          .put("timestamp", this.props.get("timestamp")).build()),
        JSXElement.h("div", null, 
          "Client Component: ",
          JSXElement.h(new ClientReference("ClientComponent"),
            PropsBuilder.empty().put("is", "Button").build(), 
            "ButtonTextFromServer"
          )
        )
      )
    );
  }
}

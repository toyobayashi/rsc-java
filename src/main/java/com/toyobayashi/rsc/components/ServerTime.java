package com.toyobayashi.rsc.components;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.toyobayashi.rsc.core.Component;
import com.toyobayashi.rsc.core.JSXElement;

import java.util.Date;

public class ServerTime extends Component {
  public ServerTime(HashMap<String, Object> props) {
    super(props);
  }

  @Override
  public JSXElement render() {
    long timestamp = (long) this.props.get("timestamp");
    return JSXElement.h("div", null,
      JSXElement.h("div", null, "System.currentTimeMillis() => " + timestamp),
      JSXElement.h("div", null, "Server Time：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        .format(new Date(timestamp)))
    );
  }
}

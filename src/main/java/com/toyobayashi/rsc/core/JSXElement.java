package com.toyobayashi.rsc.core;

import java.util.HashMap;

public class JSXElement {
  public static Symbol ElementType = Symbol._for("react.transitional.element"); // "$Sreact.transitional.element"
  public static Symbol FragmentType = Symbol._for("react.fragment"); // "$Sreact.fragment"
  public static Symbol SuspenseType = Symbol._for("react.suspense"); // "$Sreact.suspense"
  public static Symbol StrictModeType = Symbol._for("react.strict_mode"); // "$Sreact.strict_mode"
  public static Symbol ClientReferenceType = Symbol._for("react.client.reference"); // "$Sreact.client.reference"

  @FunctionalInterface
  public interface Visitor {
    JSXElement visit(JSXElement el, JSXElement parent);
  }

  public static JSXElement visit(JSXElement el, Visitor visitor) {
    return visit(el, visitor, null);
  }

  public static JSXElement visit(JSXElement el, Visitor visitor, JSXElement parent) {
    JSXElement newNode = visitor.visit(el, parent);
    if (newNode == el) {
      Object children = el.getChildren();
      if (children != null) {
        if (children instanceof Object[] c) {
          Object[] newChildren = new Object[c.length];
          for (int i = 0; i < c.length; ++i) {
            if (c[i] instanceof JSXElement child) {
              newChildren[i] = visit(child, visitor, el);
            } else {
              newChildren[i] = c[i];
            }
          }
          el.props.put("children", newChildren);
        } else if (children instanceof JSXElement child) {
          el.props.put("children", visit(child, visitor, el));
        }
      }
      return el;
    } else {
      return newNode;
    }
  }

  public static JSXElement cloneElement(JSXElement el, HashMap<String, Object> props, Object... children) {
    JSXElement cloned = new JSXElement(el.type, el.props);

    HashMap<String, Object> mergedProps;
    if (children.length > 0) {
      HashMap<String, Object> clonedProps = props == null ? new HashMap<>() : new HashMap<>(props);
      clonedProps.put("children", children.length == 1 ? children[0] : children);
      mergedProps = clonedProps;
    }
    mergedProps = props == null ? new HashMap<>() : props;

    if (mergedProps.containsKey("key")) {
      cloned.key = mergedProps.get("key");
      mergedProps.remove("key");
    } else {
      cloned.key = el.key;
    }

    if (mergedProps.containsKey("ref")) {
      cloned.key = mergedProps.get("ref");
    } else {
      cloned.ref = el.ref;
    }

    for (String key : mergedProps.keySet()) {
      cloned.props.put(key, mergedProps.get(key));
    }
    return cloned;
  }

  public static JSXElement createElement(Object type) {
    return new JSXElement(type);
  }

  public static JSXElement createElement(Object type, HashMap<String, Object> props, Object... children) {
    if (children.length > 0) {
      HashMap<String, Object> clonedProps = props == null ? new HashMap<>() : new HashMap<>(props);
      clonedProps.put("children", children.length == 1 ? children[0] : children);
      return new JSXElement(type, clonedProps);
    }
    HashMap<String, Object> clonedProps = props == null ? new HashMap<>() : props;
    return new JSXElement(type, clonedProps);
  }

  public static JSXElement h(Object type) {
    return createElement(type);
  }

  public static JSXElement h(Object type, HashMap<String, Object> props, Object... children) {
    if (children.length > 0) {
      HashMap<String, Object> clonedProps = props == null ? new HashMap<>() : new HashMap<>(props);
      clonedProps.put("children", children.length == 1 ? children[0] : children);
      return new JSXElement(type, clonedProps);
    }
    HashMap<String, Object> clonedProps = props == null ? new HashMap<>() : props;
    return new JSXElement(type, clonedProps);
  }

  public static JSXElement jsx(Object type) {
    return new JSXElement(type);
  }

  public static JSXElement jsx(Object type, HashMap<String, Object> props) {
    return new JSXElement(type, props);
  }

  public Symbol $$typeof;
  public Object type;
  public Object key;
  public Object ref;
  public HashMap<String, Object> props;

  public JSXElement(Object type) {
    this(type, new HashMap<>());
  }

  public JSXElement(Object type, HashMap<String, Object> props) {
    HashMap<String, Object> clonedProps = props == null ? new HashMap<>() : new HashMap<>(props);
    this.$$typeof = JSXElement.ElementType;
    this.type = type;
    if (clonedProps.containsKey("key")) {
      this.key = clonedProps.get("key");
      clonedProps.remove("key");
    } else {
      this.key = null;
    }
    this.ref = null;
    this.props = clonedProps;
  }

  public Object getProperty(String key) {
    return this.props.get(key);
  }

  public Object getChildren() {
    return getProperty("children");
  }
}

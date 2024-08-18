package com.toyobayashi.rsc.core;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

public class RenderUtil {
  public static void nodeToString(Object node, OutputStream writable) throws IOException {
    if (node == null || node.equals(false)) return;
    if (node instanceof Object[] arr) {
      for (Object n : arr) {
        nodeToString(n, writable);
      }
      return;
    }
    if (node instanceof JSXElement jsxElement) {
      renderToString(jsxElement, writable);
    } else {
      writable.write(node.toString().getBytes());
    }
  }

  public static void renderToString(JSXElement node, OutputStream writable) throws IOException {
    if (node.type == JSXElement.FragmentType || node.type == JSXElement.StrictModeType) {
      Object children = node.getChildren();
      nodeToString(children, writable);
      return;
    }

    if (node.type == JSXElement.SuspenseType) {
      writable.write("<!-- Suspense Starts -->".getBytes());
      Object fallback = node.getProperty("fallback");
      if (fallback != null) {
        nodeToString(fallback, writable);
      }
      writable.write("<!-- Suspense Ends -->".getBytes());
      return;
    }

    if (node.type instanceof Class<?> c && Component.class.isAssignableFrom(c)) {
      try {
        Constructor<?> ctor = c.getConstructor(HashMap.class);
        Object component = ctor.newInstance(node.props);
        Method render = c.getMethod("render");
        Object result = render.invoke(component);
        nodeToString(result, writable);
      } catch (Exception e) {
        throw new IOException(e);
      }
      return;
    }

    if (node.type instanceof FunctionComponent f) {
      Object result = f.render(node.props);
      nodeToString(result, writable);
      return;
    }

    if (node.type instanceof ClientReference) {
      writable.write("<!-- Client Reference -->".getBytes());
      return;
    }

    if (node.type instanceof String type) {
      writable.write(("<" + type).getBytes());
      for (var entry : node.props.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (key.equals("ref") || key.equals("children")) continue;
        if (value == null) continue;
        if (value instanceof Boolean b) {
          if (b) writable.write((" " + key).getBytes());
          continue;
        }
        writable.write((" " + key + "=\"" + value.toString() + "\"").getBytes());
      }
      writable.write(">".getBytes());
      Object children = node.getChildren();
      nodeToString(children, writable);
      writable.write(("</" + type + ">").getBytes());
    }
  }

  public static void renderToPayload(JSXElement node, OutputStream writable) throws IOException {
    RequestInstance request = new RequestInstance();
    request.renderModel(node);

    for (var str : request.completedImportChunks) {
      writable.write(str.getBytes());
    }
    request.completedImportChunks.clear();

    for (var str : request.completedRegularChunks) {
      writable.write(str.getBytes());
    }
    request.completedRegularChunks.clear();

    for (var str : request.completedErrorChunks) {
      writable.write(str.getBytes());
    }
    request.completedErrorChunks.clear();
  }
}

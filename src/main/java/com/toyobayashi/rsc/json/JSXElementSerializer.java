package com.toyobayashi.rsc.json;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.toyobayashi.rsc.core.ClientReference;
import com.toyobayashi.rsc.core.Component;
import com.toyobayashi.rsc.core.FunctionComponent;
import com.toyobayashi.rsc.core.JSXElement;
import com.toyobayashi.rsc.core.RequestInstance;
import com.toyobayashi.rsc.core.Symbol;

public class JSXElementSerializer extends BaseSerializer<JSXElement> {
  public JSXElementSerializer(RequestInstance request) {
    super(JSXElement.class, request);
  }
  @Override
  public void serialize(JSXElement value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException, UnsupportedOperationException {
    JSXElement jsxElement = value;
    if (jsxElement.type instanceof Class<?> c && Component.class.isAssignableFrom(c)) {
      try {
        Constructor<?> ctor = c.getConstructor(HashMap.class);
        Object component = ctor.newInstance(jsxElement.props);
        Method render = c.getMethod("render");
        Object result = render.invoke(component);

        ObjectMapper mapper = (ObjectMapper) jgen.getCodec();
        String stringValue = mapper.writeValueAsString(result);
        jgen.writeRawValue(stringValue);
        return;
      } catch (Exception e) {
        throw new IOException(e);
      }
    }

    jgen.writeStartArray();
    jgen.writeString("$");

    if (jsxElement.type instanceof String type) {
      jgen.writeString(type);
    } else if (jsxElement.type == JSXElement.SuspenseType) {
      // TODO
      throw new UnsupportedOperationException("Suspense is currently not supported in serialization");
    } else if (jsxElement.type instanceof Symbol symbolType) {
      jgen.writeString(symbolType.toJSON());
    } else if (jsxElement.type instanceof FunctionComponent f) {
      Object result = f.render(jsxElement.props);
      ObjectMapper mapper = (ObjectMapper) jgen.getCodec();
      jgen.writeRawValue(mapper.writeValueAsString(result));
    } else if (jsxElement.type instanceof ClientReference clientReference) {
      serializeClientReference(true, clientReference, jgen, provider);
    } else {
      throw new UnsupportedOperationException("unsupported type in serialization");
    }

    ObjectMapper mapper = (ObjectMapper) jgen.getCodec();
    jgen.writeRawValue(mapper.writeValueAsString(jsxElement.key));

    jgen.writeStartObject();
    for (var entry : jsxElement.props.entrySet()) {
      String key = entry.getKey();
      Object prop = entry.getValue();
      jgen.writeFieldName(key);
      if (key == "children") {
        if (prop instanceof Object[] children) {
          jgen.writeStartArray();
          for (Object child : children) {
            jgen.writeRawValue(mapper.writeValueAsString(child));
          }
          jgen.writeEndArray();
        } else {
          jgen.writeRawValue(mapper.writeValueAsString(prop));
        }
      } else {
        jgen.writeRawValue(mapper.writeValueAsString(prop));
      }
    }
    jgen.writeEndObject();

    jgen.writeEndArray();
  }
}

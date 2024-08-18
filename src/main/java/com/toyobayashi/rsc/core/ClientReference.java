package com.toyobayashi.rsc.core;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.toyobayashi.rsc.json.ClientReferenceSerializer;

@JsonSerialize(using = ClientReferenceSerializer.class)
public class ClientReference {
  public Symbol $$typeof;
  public String $$id;
  public boolean $$async;
  public String name;

  public ClientReference(String id) {
    this(id, true);
  }

  public ClientReference(String id, boolean async) {
    this(id, async, "");
  }

  public ClientReference(String id, boolean async, String name) {
    this.$$typeof = JSXElement.ClientReferenceType;
    this.$$id = id;
    this.$$async = async;
    this.name = name;
  }
}

package com.toyobayashi.rsc.core;

import java.util.HashMap;

public class Symbol {
  private static HashMap<String, Symbol> symbols = new HashMap<>();

  private final String description;

  public static Symbol _for(String key) {
    if (symbols.containsKey(key)) {
      return symbols.get(key);
    }
    Symbol symbol = new Symbol(key);
    symbols.put(key, symbol);
    return symbol;
  }

  public Symbol(String description) {
    this.description = description;
  }

  public String toString() {
    return "Symbol(" + this.description + ")";
  }

  public String toJSON() {
    return "$S" + this.description;
  }

  public boolean equals(Object other) {
    return this == other;
  }

  public int hashCode() {
    return this.toString().hashCode();
  }
}

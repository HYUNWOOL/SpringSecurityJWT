package com.example.securityJwt.practice.constant;

public enum Role {
  USER("1", "ROLE_USER"),
  ADMIN("2", "ROLE_ADMIN");
  private final String code;
  private final String symbol;

  Role(String code, String symbol) {
    this.code = code;
    this.symbol = symbol;
  }

  public String getCode() {
    return code;
  }

  public String getSymbol() {
    return symbol;
  }
}

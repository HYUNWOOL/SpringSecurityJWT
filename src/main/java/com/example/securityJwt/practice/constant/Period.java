package com.example.securityJwt.practice.constant;

public enum Period {
  DAY("1", "DAY"),
  WEEK("2", "WEEK"),
  MONTH("3", "MONTH");
  private final String code;
  private final String symbol;

  Period(String code, String symbol) {
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

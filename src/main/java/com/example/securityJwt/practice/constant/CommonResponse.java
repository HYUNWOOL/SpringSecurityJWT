package com.example.securityJwt.practice.constant;

// enum으로 api 요청 결과에 대한 code, message를 정의합니다.
public enum CommonResponse {
  SUCCESS(0, "성공하였습니디."),
  FAIL(-1, "실패하였습니다.");

  int code;
  String msg;

  CommonResponse(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}

package com.example.securityJwt.practice.service;

import com.example.securityJwt.practice.constant.CommonResponse;
import com.example.securityJwt.practice.model.response.CommonResult;
import com.example.securityJwt.practice.model.response.ListResult;
import com.example.securityJwt.practice.model.response.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 해당 Class가 Service임을 명시합니다.
public class ResponseService {
  // 단일건 결과를 처리하는 메소드
  public <T> SingleResult<T> getSingleResult(T data) {
    SingleResult<T> result = new SingleResult<>();
    result.setData(data);
    setSuccessResult(result);
    return result;
  }
  // 다중건 결과를 처리하는 메소드
  public <T> ListResult<T> getListResult(List<T> list) {
    ListResult<T> result = new ListResult<>();
    result.setList(list);
    setSuccessResult(result);
    return result;
  }
  // 성공 결과만 처리하는 메소드
  public CommonResult getSuccessResult() {
    CommonResult result = new CommonResult();
    setSuccessResult(result);
    return result;
  }

  // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
  private void setSuccessResult(CommonResult result) {
    result.setSuccess(true);
    result.setCode(CommonResponse.SUCCESS.getCode());
    result.setMsg(CommonResponse.SUCCESS.getMsg());
  }

  // 실패 결과만 처리하는 메소드
  public CommonResult getFailResult(int code, String msg) {
    CommonResult result = new CommonResult();
    result.setSuccess(false);
    result.setCode(code);
    result.setMsg(msg);
    return result;
  }
}

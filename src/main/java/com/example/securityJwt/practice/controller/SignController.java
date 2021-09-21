package com.example.securityJwt.practice.controller;

import com.example.securityJwt.practice.config.security.JwtTokenProvider;
import com.example.securityJwt.practice.constant.Role;
import com.example.securityJwt.practice.entity.User;
import com.example.securityJwt.practice.exception.CEmailSigninFailedException;
import com.example.securityJwt.practice.model.response.CommonResult;
import com.example.securityJwt.practice.model.response.SingleResult;
import com.example.securityJwt.practice.repository.UserRepository;
import com.example.securityJwt.practice.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class SignController {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final ResponseService responseService;
  private final PasswordEncoder passwordEncoder;
  private final StringRedisTemplate redisTemplate;

  @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
  @PostMapping(value = "/signin")
  public SingleResult<String> signin(
      @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
      @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
    User user = userRepository.findByEmail(id).orElseThrow(CEmailSigninFailedException::new);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      log.error("Sign In Error , {}", user.getEmail());
      throw new CEmailSigninFailedException();
    }

    return responseService.getSingleResult(
        jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));
  }

  @ApiOperation(value = "가입", notes = "회원가입을 한다.")
  @PostMapping(value = "/signup")
  public CommonResult signin(
      @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
      @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
      @ApiParam(value = "이름", required = true) @RequestParam String name,
      @ApiParam(value = "핸드폰번호", required = true) @RequestParam String phone,
      @ApiParam(value = "권한", required = true) @RequestParam Role auth) {
    userRepository.save(
        User.builder()
            .email(id)
            .password(passwordEncoder.encode(password))
            .name(name)
            .phone(phone)
            .auth(auth)
            .roles(Collections.singletonList(auth.getSymbol()))
            .build());
    return responseService.getSuccessResult();
  }

  @ApiOperation(value = "로그아웃", notes = "로그아웃 한다.")
  @PostMapping(value = "/signout")
  public CommonResult signout(String jwt) {
    ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
    logoutValueOperations.set(jwt, jwt); // redis set 명령어
    User user = (User) jwtTokenProvider.getAuthentication(jwt).getPrincipal();
    log.info("SignOut UserId : '{}' , User Name : '{}'", user.getEmail(), user.getName());

    return responseService.getSuccessResult();
  }
}

package com.example.securityJwt.practice.controller;

import com.example.securityJwt.practice.constant.Period;
import com.example.securityJwt.practice.constant.Role;
import com.example.securityJwt.practice.entity.User;
import com.example.securityJwt.practice.exception.CUserNotFoundException;
import com.example.securityJwt.practice.model.dto.PeriodDTO;
import com.example.securityJwt.practice.model.response.CommonResult;
import com.example.securityJwt.practice.model.response.ListResult;
import com.example.securityJwt.practice.model.response.SingleResult;
import com.example.securityJwt.practice.repository.UserRepository;
import com.example.securityJwt.practice.service.ResponseService;
import com.example.securityJwt.practice.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class UserController {

  private final UserRepository userRepository; // 유저 레포지토리
  private final ResponseService responseService; // 결과를 처리할 Service
  private final UserService userService;

  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "X-AUTH-TOKEN",
        value = "로그인 성공 후 access_token",
        required = true,
        dataType = "String",
        paramType = "header")
  })
  @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
  @GetMapping(value = "/users")
  public ListResult<User> findAllUser() {
    log.info("find UserList All");
    // 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
    return responseService.getListResult(userRepository.findAll());
  }

  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "X-AUTH-TOKEN",
        value = "로그인 성공 후 access_token",
        required = true,
        dataType = "String",
        paramType = "header")
  })
  @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다")
  @GetMapping(value = "/user/{id}")
  public SingleResult<User> findUserById(
      @ApiParam(value = "회원ID", required = true) @PathVariable long id) {
    log.info("find by userid {}", id);
    // 결과데이터가 단일건인경우 getSingleResult 이용해서 결과를 출력한다. 없을 경우 Exception Data 전달.
    return responseService.getSingleResult(
        userRepository.findById(id).orElseThrow(CUserNotFoundException::new));
  }

  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "X-AUTH-TOKEN",
        value = "로그인 성공 후 access_token",
        required = true,
        dataType = "String",
        paramType = "header")
  })
  @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
  @PutMapping(value = "/user")
  public SingleResult<User> modify(
      @ApiParam(value = "회원번호", required = true) @RequestParam long id,
      @ApiParam(value = "회원아이디", required = true) @RequestParam String email,
      @ApiParam(value = "회원이름", required = true) @RequestParam String name,
      @ApiParam(value = "연락처", required = true) @RequestParam String phone) {
    User user = User.builder().id(id).email(email).name(name).phone(phone).build();

    return responseService.getSingleResult(userRepository.save(user));
  }

  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "X-AUTH-TOKEN",
        value = "로그인 성공 후 access_token",
        required = true,
        dataType = "String",
        paramType = "header")
  })
  @ApiOperation(value = "회원 권한수정", notes = "회원정보를 수정한다")
  @PutMapping(value = "/user/auth")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public SingleResult<User> modify(
      @ApiParam(value = "회원번호", required = true) @RequestParam Long id,
      @ApiParam(value = "권한", required = true) @RequestParam Role auth) {

    Optional<User> user = userRepository.findById(id);
    User newUser;
    if (user.isPresent()) {
      newUser =
          User.builder()
              .id(id)
              .email(user.get().getEmail())
              .roles(Collections.singletonList(auth.getSymbol()))
              .name(user.get().getName())
              .phone(user.get().getPhone())
              .auth(auth)
              .password(user.get().getPassword())
              .build();
    } else {
      throw new CUserNotFoundException();
    }
    return responseService.getSingleResult(userRepository.save(newUser));
  }

  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "X-AUTH-TOKEN",
        value = "로그인 성공 후 access_token",
        required = true,
        dataType = "String",
        paramType = "header")
  })
  @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
  @DeleteMapping(value = "/user/{id}")
  public CommonResult delete(@ApiParam(value = "회원번호", required = true) @PathVariable long id) {

    userRepository.deleteById(id);
    // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
    return responseService.getSuccessResult();
  }

  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "X-AUTH-TOKEN",
        value = "로그인 성공 후 access_token",
        required = true,
        dataType = "String",
        paramType = "header")
  })
  @ApiOperation(value = "기간별 가입자 조회", notes = "기간별 가입 회원을 조회한다")
  @GetMapping(value = "/users/period")
  public SingleResult<PeriodDTO> findUsersByDate(Period period) {
    LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
    LocalDateTime startDatetime;
    if (period.equals(Period.DAY)) {
      startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
    } else if (period.equals(Period.WEEK)) {
      startDatetime = LocalDateTime.of(LocalDate.now().minusWeeks(1), LocalTime.of(0, 0, 0));
    } else {
      startDatetime = LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.of(0, 0, 0));
    }

    return responseService.getSingleResult(userService.stats(startDatetime, endDatetime, period));
  }
}

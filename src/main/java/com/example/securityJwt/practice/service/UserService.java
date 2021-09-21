package com.example.securityJwt.practice.service;

import com.example.securityJwt.practice.constant.Period;
import com.example.securityJwt.practice.entity.User;
import com.example.securityJwt.practice.model.dto.PeriodDTO;
import com.example.securityJwt.practice.model.dto.UserDTO;
import com.example.securityJwt.practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  public PeriodDTO stats(LocalDateTime start, LocalDateTime end, Period period) {
    List<UserDTO> userDtos = new ArrayList<>();
    for (User user : userRepository.findByCreatedDateBetween(start, end)) {
      UserDTO userDTO =
          UserDTO.builder()
              .createAt(user.getCreatedDate())
              .name(user.getName())
              .email(user.getEmail())
              .phone(user.getPhone())
              .build();

      userDtos.add(userDTO);
    }

    return PeriodDTO.builder()
        .count(userDtos.size())
        .startTime(start)
        .endTime(end)
        .period(period)
        .userList(userDtos)
        .build();
  }
}

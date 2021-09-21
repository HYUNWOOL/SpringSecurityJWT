package com.example.securityJwt.practice.model.dto;

import com.example.securityJwt.practice.constant.Period;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PeriodDTO {
  private Integer count;
  private Period period;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private List<UserDTO> userList;
}

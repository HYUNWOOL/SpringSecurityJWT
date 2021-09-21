package com.example.securityJwt.practice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
  private String email;
  private LocalDateTime createAt;
  private String name;
  private String phone;
}

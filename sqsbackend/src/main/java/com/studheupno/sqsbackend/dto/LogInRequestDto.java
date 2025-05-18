package com.studheupno.sqsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogInRequestDto {
    private String email;
    private String password;
}
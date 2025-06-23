package com.studheupno.sqsbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
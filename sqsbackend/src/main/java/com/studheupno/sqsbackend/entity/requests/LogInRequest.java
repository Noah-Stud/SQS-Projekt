package com.studheupno.sqsbackend.entity.requests;

import lombok.Data;

@Data
public class LogInRequest {
    private String email;
    private String password;
}
package com.studheupno.sqsbackend.requests;

import lombok.Data;

@Data
public class LogInRequest {
    private String email;
    private String password;
}
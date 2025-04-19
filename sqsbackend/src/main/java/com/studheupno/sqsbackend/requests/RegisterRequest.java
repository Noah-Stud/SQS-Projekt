package com.studheupno.sqsbackend.requests;

import lombok.Data;

@Data
public class RegisterRequest {
    private String id;
    private String name;
    private String email;
    private String password;
}
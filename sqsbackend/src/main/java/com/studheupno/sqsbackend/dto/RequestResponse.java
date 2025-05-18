package com.studheupno.sqsbackend.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponse {
    private String status;
    private String message;
    private Object payload;
}

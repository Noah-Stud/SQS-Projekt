package com.studheupno.sqsbackend.requests;

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

package com.studheupno.sqsbackend.entity.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseObjectEntity {
    private String status;
    private String message;
    private Object payload;
}

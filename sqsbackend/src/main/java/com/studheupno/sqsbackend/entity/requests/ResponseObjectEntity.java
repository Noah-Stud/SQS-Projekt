package com.studheupno.sqsbackend.entity.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseObjectEntity {
    private String status;
    private String message;
    private Object payload;
}

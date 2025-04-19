package com.studheupno.sqsbackend.entity.requests;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObjectEntity {
    private String status;
    private String message;
    private Object payload;
}

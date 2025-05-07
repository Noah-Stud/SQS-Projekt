package com.studheupno.sqsbackend.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDto {
    private String q;
    private String a;
    private String h;
}

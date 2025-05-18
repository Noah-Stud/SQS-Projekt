package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.QuoteDto;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestClient;

@AllArgsConstructor
public class QuoteService {

    private final RestClient restClient;

    public String getQuote() {
        QuoteDto[] quote = restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(QuoteDto[].class);
        if (quote == null) {
            return "No data found";
        } else
            return quote[0].getQ() + " \n " + quote[0].getA();
    }
}

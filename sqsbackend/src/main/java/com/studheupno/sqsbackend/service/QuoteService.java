package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.requests.QuoteDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class QuoteService {

    private final RestClient restClient = RestClient.create();

    public String getQuote() {
        QuoteDto[] quote = restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(QuoteDto[].class);
        if (quote == null) {
            return "No data found";
        } else
            return quote[0].getQ() + " \n " + quote[0].getA();
    }
}

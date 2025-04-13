package com.studheupno.sqsbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class QuoteService {

    private final RestClient restClient = RestClient.create();

    public String getQuote() {
        return restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(String.class);
    }
}

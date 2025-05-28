package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.QuoteDto;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestClient;

/**
 * Service that is responsible for calling the Quote-API.
 */
@AllArgsConstructor
public class QuoteService {

    private final RestClient restClient;

    /**
     * Request a Quote from the Quote-API (<a href="https://zenquotes.io/api/random">...</a>), parses into a QuoteDto[],
     * takes the first (and only) entry, builds a String (Quote and Author) from it and then returns the String.
     * If this process (retrieving and parsing) does not succeed, "No quote found" is returned instead.
     *
     * @return String (Quote and Author)
     */
    public String getQuote() {
        QuoteDto[] quote = restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(QuoteDto[].class);
        if (quote == null) {
            return "No quote found";
        } else
            return quote[0].getQ() + " \n " + quote[0].getA();
    }
}

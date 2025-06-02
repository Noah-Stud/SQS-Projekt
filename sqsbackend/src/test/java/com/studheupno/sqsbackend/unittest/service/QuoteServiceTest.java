package com.studheupno.sqsbackend.unittest.service;

import com.studheupno.sqsbackend.dto.QuoteDto;
import com.studheupno.sqsbackend.service.QuoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    RestClient restClient;
    @InjectMocks
    QuoteService quoteService;

    @Test
    void getQuote() {
        //Not Quote returned from API
        when(restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(QuoteDto[].class))
                .thenReturn(null);

        String quote = quoteService.getQuote();
        assertNotNull(quote);
        assertEquals("No quote found", quote);

        //xyz
        QuoteDto quoteDto = new QuoteDto("error message", "zenquotes.io", "h");

        when(restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(QuoteDto[].class))
                .thenReturn(new QuoteDto[]{quoteDto});

        quote = quoteService.getQuote();
        assertNotNull(quote);
        assertEquals("No quote found", quote);

        //Quote returned from API
        quoteDto = new QuoteDto("quote", "author", "h");

        when(restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(QuoteDto[].class))
                .thenReturn(new QuoteDto[]{quoteDto});

        quote = quoteService.getQuote();
        assertNotNull(quote);
        assertEquals("quote (author)", quote);
    }
}
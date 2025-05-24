package com.studheupno.sqsbackend.unittest.service;

import com.studheupno.sqsbackend.service.QuoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;


@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock
    RestClient restClient;
    @InjectMocks
    QuoteService quoteService;

    @Test
    void getQuote() {
        //User does not exist
//        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
//        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
//
//        when(restClient.get()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.headers(any())).thenReturn(requestBodySpec);
//        when(restClient.get().uri("https://zenquotes.io/api/random").retrieve().body(QuoteDto[].class))
//                .thenReturn(null);
//
//        String response = quoteService.getQuote();
//
//        assertNotNull(response);
//        assertEquals("No data found", response);

        //User exist
    }
}
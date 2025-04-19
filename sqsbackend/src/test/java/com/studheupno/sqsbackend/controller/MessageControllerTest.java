package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.requests.RequestResponse;

import com.studheupno.sqsbackend.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageControllerTest {

    @Mock
    MessageService messageService;

    @InjectMocks
    MessageController messageController;

    @Test
    void insertPost() {
    }

    @Test
    void getAllMessagesTest() {
        RequestResponse responseObjMessageService = new RequestResponse();
        responseObjMessageService.setStatus("fail");
        responseObjMessageService.setMessage("cannot find messages");
        responseObjMessageService.setPayload(null);
        when(messageService.getAllMessages()).thenReturn(responseObjMessageService);

        ResponseEntity<RequestResponse> responseObject;
        responseObject = messageController.getAllMessages();

        assertNotNull(responseObject);
        assertNull(Objects.requireNonNull(responseObject.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObject.getStatusCode());
    }
}
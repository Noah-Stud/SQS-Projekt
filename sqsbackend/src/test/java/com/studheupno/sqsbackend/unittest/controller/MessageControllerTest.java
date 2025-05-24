package com.studheupno.sqsbackend.unittest.controller;

import com.studheupno.sqsbackend.controller.MessageController;
import com.studheupno.sqsbackend.dto.MessagesRequestResponse;
import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    MessageService messageService;

    @InjectMocks
    MessageController messageController;

    private final UserEntity user = new UserEntity("id1", "von@mail.de", "1234", "user");
    private final MessageEntity message = new MessageEntity("id1", user, "Test1234", Instant.now(),
            "Quote", new ArrayList<>(), new ArrayList<>());

    @Test
    void getAllMessages() {
        RequestResponse responseObjMock = new RequestResponse();
        ResponseEntity<RequestResponse> responseObject;

        responseObjMock.setStatus("success");
        responseObjMock.setMessage("success");
        responseObjMock.setPayload(new MessageEntity[]{message});
        when(messageService.getAllMessages()).thenReturn(responseObjMock);

        responseObject = messageController.getAllMessages();

        assertNotNull(responseObject);
        assertNotNull(Objects.requireNonNull(responseObject.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObject.getStatusCode());
        assertEquals("success", responseObject.getBody().getStatus());
    }

    @Test
    void insertMessage() {
        RequestResponse responseObjMock = new RequestResponse();
        ResponseEntity<RequestResponse> responseObjectActual;

        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("user with email: " + user.getEmail() + " does no exist");
        responseObjMock.setPayload(null);
        when(messageService.insertMessage(user.getEmail(), "Test1234")).thenReturn(responseObjMock);

        responseObjectActual = messageController.insertMessage(user, "Test1234=");

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        responseObjMock = new RequestResponse();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("success");
        responseObjMock.setPayload(new MessagesRequestResponse(message));
        when(messageService.insertMessage(user.getEmail(), "Test1234")).thenReturn(responseObjMock);

        responseObjectActual = messageController.insertMessage(user, "Test1234=");

        assertNotNull(responseObjectActual);
        assertNotNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObjectActual.getStatusCode());
        assertEquals("success", responseObjectActual.getBody().getStatus());
    }

    @Test
    void getMessageById() {
        RequestResponse responseObjMock = new RequestResponse();
        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("message id: " + message.getId() + " does no exist");
        responseObjMock.setPayload(null);
        when(messageService.getMessageById(message.getId())).thenReturn(responseObjMock);

        ResponseEntity<RequestResponse> responseObjectActual;
        responseObjectActual = messageController.getMessageById(message.getId());

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        responseObjMock = new RequestResponse();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("success");
        responseObjMock.setPayload(new MessagesRequestResponse(message));
        when(messageService.getMessageById(message.getId())).thenReturn(responseObjMock);

        responseObjectActual = messageController.getMessageById(message.getId());

        assertNotNull(responseObjectActual);
        assertNotNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObjectActual.getStatusCode());
        assertEquals("success", responseObjectActual.getBody().getStatus());
        assertEquals(message.getContent(), ((MessagesRequestResponse) responseObjectActual.getBody().getPayload()).getContent());
    }

    @Test
    void likeMessage() {
        ResponseEntity<RequestResponse> responseObjectActual;
        RequestResponse responseObjMock = new RequestResponse();

        //User does not exist
        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("user with email: " + user.getEmail() + " does no exist");
        responseObjMock.setPayload(null);
        when(messageService.updateMessageByLike(user.getEmail(), message.getId())).thenReturn(responseObjMock);

        responseObjectActual = messageController.likeMessage(user, message.getId() + "=");

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        //Message does not exist
        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("message with id: " + message.getId() + " does no exist");
        responseObjMock.setPayload(null);
        when(messageService.updateMessageByLike(user.getEmail(), message.getId())).thenReturn(responseObjMock);

        responseObjectActual = messageController.likeMessage(user, message.getId() + "=");

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        //Message and User do exist
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("update likes to the target post id: " + message.getId());
        responseObjMock.setPayload(null);
        when(messageService.updateMessageByLike(user.getEmail(), message.getId())).thenReturn(responseObjMock);

        responseObjectActual = messageController.likeMessage(user, message.getId() + "=");

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObjectActual.getStatusCode());
        assertEquals("success", responseObjectActual.getBody().getStatus());
    }
}
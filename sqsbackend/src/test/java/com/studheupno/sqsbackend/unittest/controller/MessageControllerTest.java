package com.studheupno.sqsbackend.unittest.controller;

import com.studheupno.sqsbackend.controller.MessageController;
import com.studheupno.sqsbackend.dto.MessageRequestDto;
import com.studheupno.sqsbackend.dto.MessageResponseDto;
import com.studheupno.sqsbackend.dto.RequestResponseDto;
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
import java.util.List;
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
        List<MessageResponseDto> messagesObjMock = new ArrayList<>();
        messagesObjMock.add(new MessageResponseDto(message));

        RequestResponseDto responseObjMock = new RequestResponseDto();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("success");
        responseObjMock.setPayload(messagesObjMock);
        when(messageService.getAllMessages()).thenReturn(responseObjMock);

        ResponseEntity<RequestResponseDto> responseObject = messageController.getAllMessages();

        assertNotNull(responseObject);
        assertNotNull(Objects.requireNonNull(responseObject.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObject.getStatusCode());
        assertEquals("success", responseObject.getBody().getStatus());
        assertEquals(message.getUser().getEmail(), ((List<MessageResponseDto>) responseObject.getBody().getPayload())
                .getFirst().getUserEmail());
    }

    @Test
    void insertMessage() {
        //User does not exist
        RequestResponseDto responseObjMock = new RequestResponseDto();
        ResponseEntity<RequestResponseDto> responseObjectActual;

        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("user with email: " + user.getEmail() + " does no exist");
        responseObjMock.setPayload(null);
        when(messageService.insertMessage(user.getEmail(), "Test1234")).thenReturn(responseObjMock);

        responseObjectActual = messageController.insertMessage(user, new MessageRequestDto("Test1234"));

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        //User does exist
        responseObjMock = new RequestResponseDto();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("success");
        responseObjMock.setPayload(new MessageResponseDto(message));
        when(messageService.insertMessage(user.getEmail(), "Test1234")).thenReturn(responseObjMock);

        responseObjectActual = messageController.insertMessage(user, new MessageRequestDto("Test1234"));

        assertNotNull(responseObjectActual);
        assertNotNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObjectActual.getStatusCode());
        assertEquals("success", responseObjectActual.getBody().getStatus());
        assertEquals("von@mail.de", ((MessageResponseDto) responseObjectActual.getBody().getPayload()).getUserEmail());
        assertEquals("Test1234", ((MessageResponseDto) responseObjectActual.getBody().getPayload()).getContent());
    }

    @Test
    void getMessageById() {
        //Message does not exist
        RequestResponseDto responseObjMock = new RequestResponseDto();
        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("message id: " + message.getId() + " does no exist");
        responseObjMock.setPayload(null);
        when(messageService.getMessageById(message.getId())).thenReturn(responseObjMock);

        ResponseEntity<RequestResponseDto> responseObjectActual;
        responseObjectActual = messageController.getMessageById(message.getId());

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        //Message does exist
        responseObjMock = new RequestResponseDto();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("success");
        responseObjMock.setPayload(new MessageResponseDto(message));
        when(messageService.getMessageById(message.getId())).thenReturn(responseObjMock);

        responseObjectActual = messageController.getMessageById(message.getId());

        assertNotNull(responseObjectActual);
        assertNotNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObjectActual.getStatusCode());
        assertEquals("success", responseObjectActual.getBody().getStatus());
        assertEquals(message.getUser().getEmail(), ((MessageResponseDto) responseObjectActual.getBody().getPayload()).getUserEmail());
        assertEquals(message.getContent(), ((MessageResponseDto) responseObjectActual.getBody().getPayload()).getContent());
    }

    @Test
    void likeMessage() {
        //User does not exist
        RequestResponseDto responseObjMock = new RequestResponseDto();
        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("user with email: " + user.getEmail() + " does no exist");
        responseObjMock.setPayload(null);
        when(messageService.updateMessageByLike(user.getEmail(), message.getId())).thenReturn(responseObjMock);

        ResponseEntity<RequestResponseDto> responseObjectActual = messageController.likeMessage(user, message.getId() + "=");

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
        responseObjMock.setPayload(null); //Not nice!!!
        when(messageService.updateMessageByLike(user.getEmail(), message.getId())).thenReturn(responseObjMock);

        responseObjectActual = messageController.likeMessage(user, message.getId() + "=");

        assertNotNull(responseObjectActual);
        //assertNotNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObjectActual.getStatusCode());
        assertEquals("success", responseObjectActual.getBody().getStatus());
    }
}
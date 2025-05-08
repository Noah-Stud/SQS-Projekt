package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.requests.CommentRequest;
import com.studheupno.sqsbackend.requests.RequestResponse;
import com.studheupno.sqsbackend.service.CommentService;
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
class CommentControllerTest {

    @Mock
    private CommentService commentService;
    @InjectMocks
    private CommentController commentController;

    private final UserEntity user = new UserEntity("id1", "von@mail.de", "1234", "user");
    private final MessageEntity message = new MessageEntity("id1", user, "Test1234", Instant.now(),
            "Quote", new ArrayList<>(), new ArrayList<>());
    private final CommentRequest commentRequest = new CommentRequest(message.getId(), "Test-Comment");

    @Test
    void insertComment() {
        RequestResponse responseObjMock = new RequestResponse();
        ResponseEntity<RequestResponse> responseObjectActual;

        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("User with email: " + user.getEmail() + " not found");
        responseObjMock.setPayload(null);
        when(commentService.insertComment(commentRequest, user.getEmail())).thenReturn(responseObjMock);

        responseObjectActual = commentController.insertComment(user, commentRequest);

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        responseObjMock = new RequestResponse();
        responseObjMock.setStatus("fail");
        responseObjMock.setMessage("cannot find message with id: " + message.getId());
        responseObjMock.setPayload(null);
        when(commentService.insertComment(commentRequest, user.getEmail())).thenReturn(responseObjMock);

        responseObjectActual = commentController.insertComment(user, commentRequest);

        assertNotNull(responseObjectActual);
        assertNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.NOT_FOUND, responseObjectActual.getStatusCode());
        assertEquals("fail", responseObjectActual.getBody().getStatus());

        responseObjMock = new RequestResponse();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("comment has been added to message");
        //responseObjMock.setPayload(null);
        when(commentService.insertComment(commentRequest, user.getEmail())).thenReturn(responseObjMock);

        responseObjectActual = commentController.insertComment(user, commentRequest);

        assertNotNull(responseObjectActual);
        //assertNotNull(Objects.requireNonNull(responseObjectActual.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObjectActual.getStatusCode());
        assertEquals("success", responseObjectActual.getBody().getStatus());
    }
}
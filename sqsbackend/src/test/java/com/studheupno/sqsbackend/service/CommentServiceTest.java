package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.CommentRequest;
import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    UserRepo userRepo;
    @Mock
    MessageService messageService;
    @InjectMocks
    CommentService commentService;

    CommentRequest commentRequest = new CommentRequest("id4", "Hello world");
    UserEntity user = new UserEntity("id1", "von@mail.de", "1234", "user");

    @Test
    void insertComment() {
        when(userRepo.findByEmail("nicht@mail.de")).thenReturn(Optional.empty());

        RequestResponse response = commentService.insertComment(commentRequest, "nicht@mail.de");

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("User with email: " + "nicht@mail.de" + " not found", response.getMessage());

        //User exist
        RequestResponse responseObjMock = new RequestResponse();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("comment has been added to message");
        //responseObjMock.setPayload(null);

        when(userRepo.findByEmail("von@mail.de")).thenReturn(Optional.of(user));
        when(messageService.updateMessageByComment(commentRequest.getMessageId(), commentRequest.getCommentContent()))
                .thenReturn(responseObjMock);

        response = commentService.insertComment(commentRequest, user.getEmail());

        assertNotNull(response);
        //assertNotNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals("comment has been added to message", response.getMessage());
    }
}
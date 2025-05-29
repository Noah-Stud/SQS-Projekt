package com.studheupno.sqsbackend.unittest.service;

import com.studheupno.sqsbackend.dto.CommentRequestDto;
import com.studheupno.sqsbackend.dto.RequestResponseDto;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import com.studheupno.sqsbackend.service.CommentService;
import com.studheupno.sqsbackend.service.MessageService;
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

    CommentRequestDto commentRequestDto = new CommentRequestDto("id4", "Hello world");
    UserEntity user = new UserEntity("id1", "von@mail.de", "1234", "user");

    @Test
    void insertComment() {
        //User does not exist
        when(userRepo.findByEmail("nicht@mail.de")).thenReturn(Optional.empty());

        RequestResponseDto response = commentService.insertComment(commentRequestDto, "nicht@mail.de");

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("User with email: " + "nicht@mail.de" + " not found", response.getMessage());

        //User exist
        RequestResponseDto responseObjMock = new RequestResponseDto();
        responseObjMock.setStatus("success");
        responseObjMock.setMessage("comment has been added to message");
        responseObjMock.setPayload(null);

        when(userRepo.findByEmail("von@mail.de")).thenReturn(Optional.of(user));
        when(messageService.updateMessageByComment(commentRequestDto, user))
                .thenReturn(responseObjMock);

        response = commentService.insertComment(commentRequestDto, user.getEmail());

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals("comment has been added to message", response.getMessage());
    }
}
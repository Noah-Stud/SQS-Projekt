package com.studheupno.sqsbackend.unittest.service;

import com.studheupno.sqsbackend.dto.CommentRequestDto;
import com.studheupno.sqsbackend.dto.MessageResponseDto;
import com.studheupno.sqsbackend.dto.RequestResponseDto;
import com.studheupno.sqsbackend.entity.CommentEntity;
import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.CommentRepo;
import com.studheupno.sqsbackend.repo.MessageRepo;
import com.studheupno.sqsbackend.repo.UserRepo;
import com.studheupno.sqsbackend.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    UserRepo userRepo;
    @Mock
    MessageRepo messageRepo;
    @Mock
    CommentRepo commentRepo;
    @InjectMocks
    MessageService messageService;

    private UserEntity user;
    private MessageEntity message;
    private CommentEntity comment;
    private final Instant instant = Instant.now();

    @BeforeEach
    void setUp() {
        user = new UserEntity("id1", "von@mail.de", "1234", "user");
        message = new MessageEntity("id5", user, "Message input", instant, "Quote 1",
                new ArrayList<>(), new ArrayList<>());
        comment = new CommentEntity("id7", user, "New Comment", Instant.now());
    }

    @Test
    void insertMessage() {
        //User does not exist
        when(userRepo.findByEmail("nicht@mail.de")).thenReturn(Optional.empty());

        RequestResponseDto response = messageService.insertMessage("nicht@mail.de", "Message input");

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("User with email: " + "nicht@mail.de" + " not found", response.getMessage());

        //User exist
        when(userRepo.findByEmail("von@mail.de")).thenReturn(Optional.of(user));
        when(messageRepo.save(any(MessageEntity.class))).thenReturn(message);

        response = messageService.insertMessage(user.getEmail(), "Message input");

        assertNotNull(response);
        assertNotNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(new MessageResponseDto(message), response.getPayload());
    }

    @Test
    void getMessageById() {
        //Message does not exist
        when(messageRepo.findById("idNotExist")).thenReturn(Optional.empty());

        RequestResponseDto response = messageService.getMessageById("idNotExist");

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("Message with id: " + "idNotExist" + " not found", response.getMessage());

        //Message does exist
        when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));

        response = messageService.getMessageById(message.getId());

        assertNotNull(response);
        assertNotNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(new MessageResponseDto(message), response.getPayload());
    }

    @Test
    void getAllMessages() {
        when(messageRepo.findAll()).thenReturn(List.of(message));

        RequestResponseDto response = messageService.getAllMessages();

        assertNotNull(response);
        assertNotNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(new MessageResponseDto(message), ((List<?>) response.getPayload()).getFirst());
    }

    @Test
    void updateMessageByComment() {
        //Message does not exist
        CommentRequestDto commentRequestDto = new CommentRequestDto("idNotExist", "New Comment");
        UserEntity commentUser = new UserEntity("id2", "von2@mail.de", "1234", "user");

        when(messageRepo.findById("idNotExist")).thenReturn(Optional.empty());


        RequestResponseDto response = messageService.updateMessageByComment(commentRequestDto, commentUser);

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("Message with id: " + "idNotExist" + " not found", response.getMessage());

        //Message does exist
        commentRequestDto.setMessageId(message.getId());

        when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));
        when(commentRepo.save(new CommentEntity(null, user, "New Comment", any()))).thenReturn(comment);

        response = messageService.updateMessageByComment(commentRequestDto, commentUser);

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals("Comment has been added to message", response.getMessage());
    }

    @Test
    void updateMessageByLike() {
        //Message does not exist
        when(messageRepo.findById("idNotExist")).thenReturn(Optional.empty());

        RequestResponseDto response = messageService.updateMessageByLike(user.getEmail(), "idNotExist");

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("Message with id: " + "idNotExist" + " not found", response.getMessage());

        //User does not exist
        when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));
        when(userRepo.findByEmail("emailNotExist")).thenReturn(Optional.empty());

        response = messageService.updateMessageByLike("emailNotExist", message.getId());

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("User with email: " + "emailNotExist" + " not found", response.getMessage());

        //User and message do exist
        when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(messageRepo.save(any(MessageEntity.class))).thenReturn(message); //Not nice!!!

        response = messageService.updateMessageByLike(user.getEmail(), message.getId());

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("update likes to the target post id: " + message.getId(), response.getMessage());
        assertEquals(new MessageResponseDto(message), response.getPayload());
    }
}
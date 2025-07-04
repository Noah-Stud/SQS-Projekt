package com.studheupno.sqsbackend.test_container;


import com.studheupno.sqsbackend.controller.AuthController;
import com.studheupno.sqsbackend.controller.CommentController;
import com.studheupno.sqsbackend.controller.MessageController;
import com.studheupno.sqsbackend.controller.UserController;
import com.studheupno.sqsbackend.dto.*;
import com.studheupno.sqsbackend.entity.UserEntity;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class ContainerTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.4")
            .withDatabaseName("messages")
            .withPassword("mauFJcuf5dhRMQrjj");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @Autowired
    private AuthController authController;
    @Autowired
    private UserController userController;
    @Autowired
    private MessageController messageController;
    @Autowired
    private CommentController commentController;

    @Test
    @Order(1)
    void authenticationTest() {
        //Email not used
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("vonan@mail.de", "password1234");
        ResponseEntity<RequestResponseDto> responseEntity = authController.registerUser(registerRequestDto);

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("User registered successfully!", responseEntity.getBody().getMessage());

        //Email already used
        registerRequestDto = new RegisterRequestDto("vonan@mail.de", "password1234");
        responseEntity = authController.registerUser(registerRequestDto);

        assertNotNull(responseEntity.getBody());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("Error: Email is already in use!", responseEntity.getBody().getMessage());

        //Login successful
        LogInRequestDto logInRequestDto = new LogInRequestDto("vonan@mail.de", "password1234");
        responseEntity = authController.authenticateUser(logInRequestDto);

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("authenticated", responseEntity.getBody().getMessage());
        assertNotNull(responseEntity.getBody().getPayload());

        //Wrong password
        logInRequestDto = new LogInRequestDto("vonan@mail.de", "password12345");
        responseEntity = authController.authenticateUser(logInRequestDto);

        assertNotNull(responseEntity.getBody());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("unauthenticated", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getPayload());
    }

    @Test
    @Order(2)
    void UserTest() {
        UserResponseDto userResponseDto = new UserResponseDto("vonan@mail.de");

        ResponseEntity<RequestResponseDto> responseEntity = userController.findAllUsers();

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals(userResponseDto, ((List<UserResponseDto>) responseEntity.getBody().getPayload()).getFirst());
    }

    @Test
    @Order(3)
    void messageTest() {
        //No Messages exist -> Empty List
        UserEntity userExist = new UserEntity(null, "vonan@mail.de", "1234", "user");
        UserEntity userDoesNotExist = new UserEntity(null, "von@mail.de", "1234", "user");

        ResponseEntity<RequestResponseDto> responseEntity = messageController.getAllMessages();

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals(Lists.emptyList(), responseEntity.getBody().getPayload());

        //User does not exist -> Message not created
        responseEntity = messageController.insertMessage(userDoesNotExist, new MessageRequestDto("New message 1"));

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("User with email: " + "von@mail.de" + " not found", responseEntity.getBody().getMessage());

        //User does exist -> Message created
        responseEntity = messageController.insertMessage(userExist, new MessageRequestDto("New message 1"));

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals("New message 1", ((MessageResponseDto) responseEntity.getBody().getPayload()).getContent());
        assertEquals("vonan@mail.de", ((MessageResponseDto) responseEntity.getBody().getPayload()).getUserEmail());

        //One Message does exist -> List not empty
        responseEntity = messageController.getAllMessages();

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals("New message 1", ((List<MessageResponseDto>) responseEntity.getBody().getPayload())
                .getFirst().getContent());

        //Message does not exist
        responseEntity = messageController.getMessageById("Id does not exist");

        assertNotNull(responseEntity.getBody());
        assertNull(responseEntity.getBody().getPayload());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("Message with id: Id does not exist not found", responseEntity.getBody().getMessage());

        //Message does exist
        responseEntity = messageController.getAllMessages();
        MessageResponseDto existingMessage = ((List<MessageResponseDto>) responseEntity.getBody().getPayload())
                .getFirst();

        responseEntity = messageController.getMessageById(existingMessage.getId());

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals(existingMessage, responseEntity.getBody().getPayload());
    }

    @Test
    @Order(4)
    void likeMessageTest() {
        UserEntity userExist = new UserEntity(null, "vonan@mail.de", "1234", "user");
        String existingMessageId = ((List<MessageResponseDto>) messageController.getAllMessages()
                .getBody().getPayload()).getFirst().getId();

        //Message does not exist
        ResponseEntity<RequestResponseDto> responseEntity = messageController.likeMessage(userExist, "Id does not exist=");

        assertNotNull(responseEntity.getBody());
        assertNull(responseEntity.getBody().getPayload());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("Message with id: Id does not exist not found", responseEntity.getBody().getMessage());

        //Message does exist
        responseEntity = messageController.likeMessage(userExist, existingMessageId + "=");

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("update likes to the target post id: " + existingMessageId, responseEntity.getBody().getMessage());
        assertTrue(((MessageResponseDto) responseEntity.getBody().getPayload()).getLikes()
                .contains(userExist.getEmail()));
    }

    @Test
    @Order(5)
    void commentTest() {
        //User does not exist
        UserEntity userDoesNotExist = new UserEntity(null, "von@mail.de", "1234", "user");
        CommentRequestDto commentRequestDto = new CommentRequestDto("notExist", "Test Comment Not Exist");

        ResponseEntity<RequestResponseDto> responseEntity = commentController.insertComment(userDoesNotExist, commentRequestDto);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("User with email: " + "von@mail.de" + " not found", responseEntity.getBody().getMessage());

        //Message does not exist
        UserEntity userExist = new UserEntity(null, "vonan@mail.de", "1234", "user");

        responseEntity = commentController.insertComment(userExist, commentRequestDto);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("Message with id: " + "notExist" + " not found", responseEntity.getBody().getMessage());

        //User and Message do exist
        responseEntity = messageController.getAllMessages();
        String messageId = ((List<MessageResponseDto>) responseEntity.getBody().getPayload()).getFirst().getId();
        commentRequestDto = new CommentRequestDto(messageId, "Test Comment Exist");

        responseEntity = commentController.insertComment(userExist, commentRequestDto);

        assertNotNull(responseEntity.getBody());
        assertNull(responseEntity.getBody().getPayload());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("Comment has been added to message", responseEntity.getBody().getMessage());
    }
}

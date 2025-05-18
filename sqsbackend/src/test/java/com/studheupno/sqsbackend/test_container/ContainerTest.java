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
        RegisterRequest registerRequest = new RegisterRequest("vonan@mail.de", "password1234");
        ResponseEntity<RequestResponse> responseEntity = authController.registerUser(registerRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("User registered successfully!", responseEntity.getBody().getMessage());


        registerRequest = new RegisterRequest("vonan@mail.de", "password1234");
        responseEntity = authController.registerUser(registerRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("Error: Email is already in use!", responseEntity.getBody().getMessage());


        LogInRequest logInRequest = new LogInRequest("vonan@mail.de", "password1234");
        responseEntity = authController.authenticateUser(logInRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("authenticated", responseEntity.getBody().getMessage());
        assertNotNull(responseEntity.getBody().getPayload());


        logInRequest = new LogInRequest("vonan@mail.de", "password12345");
        responseEntity = authController.authenticateUser(logInRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("unauthenticated", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getPayload());
    }

    @Test
    @Order(2)
    void UserTest() {
        UserRequestResponse userRequestResponse = new UserRequestResponse("vonan@mail.de");

        ResponseEntity<RequestResponse> responseEntity = userController.findAllUsers();

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals(userRequestResponse, ((List<UserRequestResponse>) responseEntity.getBody().getPayload()).getFirst());


        responseEntity = userController.findByEmail("vonan@mail.de");

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals(userRequestResponse, responseEntity.getBody().getPayload());


        responseEntity = userController.findByEmail("notexist@mail.de");

        assertNotNull(responseEntity.getBody());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("User with email: " + "notexist@mail.de" + " not found",
                responseEntity.getBody().getMessage());
    }

    @Test
    @Order(3)
    void messageTest() {
        UserEntity userExist = new UserEntity(null, "vonan@mail.de", "1234", "user");
        UserEntity userDoesNotExist = new UserEntity(null, "von@mail.de", "1234", "user");

        ResponseEntity<RequestResponse> responseEntity = messageController.getAllMessages();

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals(Lists.emptyList(), responseEntity.getBody().getPayload());


        responseEntity = messageController.insertMessage(userDoesNotExist, "New message 1");

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("User with email: " + "von@mail.de" + " not found", responseEntity.getBody().getMessage());


        responseEntity = messageController.insertMessage(userExist, "New message 1");

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals("New message 1", ((MessagesRequestResponse) responseEntity.getBody().getPayload()).getContent());
        assertEquals("vonan@mail.de", ((MessagesRequestResponse) responseEntity.getBody().getPayload()).getUserEmail());


        responseEntity = messageController.getAllMessages();

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals("New message 1", ((List<MessagesRequestResponse>) responseEntity.getBody().getPayload())
                .getFirst().getContent());

        //
        MessagesRequestResponse messageResponse = ((List<MessagesRequestResponse>) responseEntity.getBody().getPayload())
                .getFirst();
        responseEntity = messageController.getMessageById(messageResponse.getId());

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("success", responseEntity.getBody().getMessage());
        assertEquals(messageResponse, responseEntity.getBody().getPayload());

        //
        responseEntity = messageController.likeMessage(userExist, messageResponse.getId() + "=");

        assertNotNull(responseEntity.getBody());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("update likes to the target post id: " + messageResponse.getId(), responseEntity.getBody().getMessage());
        assertTrue(((MessagesRequestResponse) responseEntity.getBody().getPayload()).getLikes()
                .contains(userExist.getEmail()));
    }

    @Test
    @Order(4)
    void commentTest() {
        //User does not exist
        UserEntity userDoesNotExist = new UserEntity(null, "von@mail.de", "1234", "user");
        CommentRequest commentRequest = new CommentRequest("notExist", "Test Comment Not Exist");

        ResponseEntity<RequestResponse> responseEntity = commentController.insertComment(userDoesNotExist, commentRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("User with email: " + "von@mail.de" + " not found", responseEntity.getBody().getMessage());

        //Message does not exist
        UserEntity userExist = new UserEntity(null, "vonan@mail.de", "1234", "user");

        responseEntity = commentController.insertComment(userExist, commentRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().getStatus());
        assertEquals("Message with id: " + "notExist" + " not found", responseEntity.getBody().getMessage());

        //User and Message do exist
        responseEntity = messageController.getAllMessages();
        String messageId = ((List<MessagesRequestResponse>) responseEntity.getBody().getPayload()).getFirst().getId();
        commentRequest = new CommentRequest(messageId, "Test Comment Exist");

        responseEntity = commentController.insertComment(userExist, commentRequest);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertEquals("Comment has been added to message", responseEntity.getBody().getMessage());
        assertEquals(userExist.getEmail(), ((CommentRequestResponse) responseEntity.getBody()
                .getPayload()).getUserEmail());
        assertEquals(commentRequest.getCommentContent(), ((CommentRequestResponse) responseEntity.getBody()
                .getPayload()).getContent());
    }
}

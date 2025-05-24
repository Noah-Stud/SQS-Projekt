package com.studheupno.sqsbackend.unittest.controller;

import com.studheupno.sqsbackend.controller.UserController;
import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    UserEntity user = new UserEntity("123", "von@mail.de", "1234", "user");

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    void insertPost() {
    }

    @Test
    void getAllTest() {
        UserEntity[] userEntities = {user};
        RequestResponse responseObj = new RequestResponse();
        responseObj.setPayload(userEntities);
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        when(userService.findAll()).thenReturn(responseObj);

        ResponseEntity<RequestResponse> responseObject = userController.findAllUsers();

        assertNotNull(responseObject);
        assertNotNull(Objects.requireNonNull(responseObject.getBody()).getPayload());
        assertEquals(HttpStatus.OK, responseObject.getStatusCode());
        assertEquals(user.getId(), ((UserEntity[]) responseObject.getBody().getPayload())[0].getId());
        assertEquals(user.getEmail(), ((UserEntity[]) responseObject.getBody().getPayload())[0].getEmail());
    }
}
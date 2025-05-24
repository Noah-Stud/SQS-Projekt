package com.studheupno.sqsbackend.unittest.service;

import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.dto.UserRequestResponse;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import com.studheupno.sqsbackend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepo userRepo;
    @InjectMocks
    UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity("id1", "von@mail.de", "1234", "user");
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findAll() {
        when(userRepo.findAll()).thenReturn(List.of(user));

        RequestResponse response = userService.findAll();

        assertNotNull(response);
        assertNotNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals(new UserRequestResponse(user), ((List<?>) response.getPayload()).getFirst());
    }

    @Test
    void findByEmail() {
        //User does not exist
        when(userRepo.findByEmail("nicht@mail.de")).thenReturn(Optional.empty());

        RequestResponse response = userService.findByEmail("nicht@mail.de");

        assertNotNull(response);
        assertNull(response.getPayload());
        assertEquals("fail", response.getStatus());
        assertEquals("User with email: " + "nicht@mail.de" + " not found", response.getMessage());

        //User exist
        when(userRepo.findByEmail("von@mail.de")).thenReturn(Optional.of(user));

        response = userService.findByEmail("von@mail.de");

        assertNotNull(response);
        assertNotNull(response.getPayload());
        assertEquals("success", response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(new UserRequestResponse(user), response.getPayload());
    }

    @Test
    void loadUserByUsername() {
        //User does not exist
        when(userRepo.findByEmail("nicht@mail.de")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nicht@mail.de"));

        //User exist
        when(userRepo.findByEmail("von@mail.de")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("von@mail.de");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }
}
package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<RequestResponse> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
}
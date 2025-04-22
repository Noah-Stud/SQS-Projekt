package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.requests.RequestResponse;
import com.studheupno.sqsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<RequestResponse> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/findByEmail")
    public ResponseEntity<RequestResponse> findByEmail(@RequestBody String inputEmail) {
        return new ResponseEntity<>(userService.findByEmail(inputEmail), HttpStatus.OK);
    }
}
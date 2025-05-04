package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.requests.RequestResponse;
import com.studheupno.sqsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<RequestResponse> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<RequestResponse> findByEmail(@RequestBody String inputEmail) {
        return new ResponseEntity<>(userService.findByEmail(inputEmail), HttpStatus.OK);
    }
}
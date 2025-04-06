package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.entity.requests.ResponseObjectEntity;
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
    public ResponseEntity<ResponseObjectEntity> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseObjectEntity> findById(@RequestBody String inputId) {
        return new ResponseEntity<>(userService.findById(inputId), HttpStatus.OK);
    }

    @PostMapping("/user/save")
    public ResponseEntity<ResponseObjectEntity> saveUser(@RequestBody UserEntity inputUser) {
        return new ResponseEntity<ResponseObjectEntity>(userService.saveUser(inputUser), HttpStatus.OK);
    }
}
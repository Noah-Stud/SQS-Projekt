package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.service.MessageService;
import com.studheupno.sqsbackend.requests.RequestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/insertmessage")
    public ResponseEntity<RequestResponse> insertMessage(@RequestBody String messageContent) {
        return new ResponseEntity<>(messageService.insertMessage(messageContent), HttpStatus.OK);
    }

    @GetMapping("/messages")
    public ResponseEntity<RequestResponse> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }
}

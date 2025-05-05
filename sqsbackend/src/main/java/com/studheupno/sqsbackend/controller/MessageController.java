package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.service.MessageService;
import com.studheupno.sqsbackend.requests.RequestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/message/v1")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/insert")
    public ResponseEntity<RequestResponse> insertMessage(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody String messageContent) {
        return new ResponseEntity<>(messageService.insertMessage(userDetails.getUsername(), messageContent), HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<RequestResponse> getMessageById(@RequestBody String messageId) {
        RequestResponse messageResponse = messageService.getMessageById(messageId);

        if(messageResponse.getStatus().equals("fail")) {
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<RequestResponse> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<RequestResponse> lovePost(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody String messageId) {
        return new ResponseEntity<>(messageService.updateMessageByLike(userDetails.getUsername(), messageId), HttpStatus.OK);
    }
}

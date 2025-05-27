package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/**
 * Rest-Controller that is responsable for Message-Related-Requests
 */
@RestController
@RequestMapping("/api/message/v1")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Post-Request (/api/message/v1/insert) that creates a new Comment for a given messageId
     *
     * @param userDetails    UserDetails found in jwt-Token
     * @param messageContent String containing the messageContent
     * @return RequestResponse
     */
    @PostMapping("/insert")
    public ResponseEntity<RequestResponse> insertMessage(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody String messageContent) {
        messageContent = messageContent.substring(0, messageContent.length() - 1);
        RequestResponse messageResponse = messageService.insertMessage(userDetails.getUsername(), messageContent);

        if (messageResponse.getStatus().equals("fail")) {
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    /**
     * Get-Request (/api/message/v1/getById) that returns the message that belongs to the given id
     *
     * @param messageId ID of the message
     * @return RequestResponse containing the MessageRequestResponse (if successful)
     */
    @GetMapping("/getById")
    public ResponseEntity<RequestResponse> getMessageById(@RequestBody String messageId) {
        RequestResponse messageResponse = messageService.getMessageById(messageId);

        if (messageResponse.getStatus().equals("fail")) {
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    /**
     * Get-Request (/api/message/v1/getAll) that returns messages in the database
     *
     * @return RequestResponse containing a list of MessageRequestResponses
     */
    @GetMapping("/getAll")
    public ResponseEntity<RequestResponse> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }

    /**
     * Post-Request (/api/message/v1/like) that gives / removes a like from the user
     *
     * @param userDetails UserDetails found in jwt-Token
     * @param messageId   ID of the message
     * @return RequestResponse
     */
    @PostMapping("/like")
    public ResponseEntity<RequestResponse> likeMessage(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody String messageId) {
        messageId = messageId.substring(0, messageId.length() - 1);        //Remove last char, because reason
        RequestResponse messageResponse = messageService.updateMessageByLike(userDetails.getUsername(), messageId);

        if (messageResponse.getStatus().equals("fail")) {
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}

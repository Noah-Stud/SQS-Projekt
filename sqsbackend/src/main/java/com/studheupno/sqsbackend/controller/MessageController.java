package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.dto.RequestResponseDto;
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
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/insert")
    public ResponseEntity<RequestResponseDto> insertMessage(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody String messageContent) {
        messageContent = messageContent.substring(0, messageContent.length() - 1);
        RequestResponseDto messageResponse = messageService.insertMessage(userDetails.getUsername(), messageContent);

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
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getById")
    public ResponseEntity<RequestResponseDto> getMessageById(@RequestBody String messageId) {
        RequestResponseDto messageResponse = messageService.getMessageById(messageId);

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
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAll")
    public ResponseEntity<RequestResponseDto> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }

    /**
     * Post-Request (/api/message/v1/like) that gives / removes a like from the user
     *
     * @param userDetails UserDetails found in jwt-Token
     * @param messageId   ID of the message
     * @return RequestResponse
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/like")
    public ResponseEntity<RequestResponseDto> likeMessage(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody String messageId) {
        messageId = messageId.substring(0, messageId.length() - 1);        //Remove last char, because reason
        RequestResponseDto messageResponse = messageService.updateMessageByLike(userDetails.getUsername(), messageId);

        if (messageResponse.getStatus().equals("fail")) {
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}

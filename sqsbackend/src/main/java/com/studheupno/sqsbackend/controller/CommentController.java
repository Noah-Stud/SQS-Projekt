package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.dto.CommentRequestDto;
import com.studheupno.sqsbackend.dto.RequestResponseDto;
import com.studheupno.sqsbackend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Rest-Controller that is responsable for Comment-Related-Requests
 */
@RestController
@RequestMapping("/api/comment/v1")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Post-Request (/api/comment/v1/create) that creates a new Comment for a given messageId
     *
     * @param userDetails   UserDetails found in jwt-Token
     * @param postedComment Request containing Comment and messageId
     * @return RequestResponse containing a list of UserRequestResponses
     */
    @PostMapping("/create")
    public ResponseEntity<RequestResponseDto> insertComment(@AuthenticationPrincipal UserDetails userDetails,
                                                            @Valid @RequestBody CommentRequestDto postedComment) {
        RequestResponseDto messageResponse = commentService.insertComment(postedComment, userDetails.getUsername());

        if (messageResponse.getStatus().equals("fail")) {
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}

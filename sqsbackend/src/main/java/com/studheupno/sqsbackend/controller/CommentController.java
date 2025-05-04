package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.requests.CommentRequest;
import com.studheupno.sqsbackend.service.CommentService;
import com.studheupno.sqsbackend.requests.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment/v1")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<RequestResponse> insertComment(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody CommentRequest postedComment) {
        String inputPostId = postedComment.getMessageId();
        return new ResponseEntity<>(commentService.insertComment(postedComment, inputPostId, userDetails.getUsername()),
                HttpStatus.OK);
    }
}

package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.entity.CommentEntity;
import com.studheupno.sqsbackend.requests.CommentRequestResponse;
import com.studheupno.sqsbackend.service.CommentService;
import com.studheupno.sqsbackend.requests.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/createcomment")
    public ResponseEntity<RequestResponse> insertComment(@RequestBody CommentRequestResponse postedComment) {
        CommentEntity inputComment = postedComment.getCommentEntity();
        String inputPostId = postedComment.getPostId();
        return new ResponseEntity<>(commentService.insertComment(inputComment,
                inputPostId), HttpStatus.OK);
    }

    @PostMapping("/getcomments")
    public ResponseEntity<RequestResponse> getComments(@RequestBody String postId) {
        return new ResponseEntity<>(commentService.getComments(postId), HttpStatus.OK);
    }
}

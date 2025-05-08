package com.studheupno.sqsbackend.requests;

import com.studheupno.sqsbackend.entity.CommentEntity;
import lombok.Data;

import java.time.Instant;


@Data
public class CommentRequestResponse {

    private String id;
    private String userEmail;
    private String content;
    private Instant createdAt;

    public CommentRequestResponse(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.userEmail = commentEntity.getUser().getEmail();
        this.content = commentEntity.getContent();
        this.createdAt = commentEntity.getCreatedAt();
    }
}

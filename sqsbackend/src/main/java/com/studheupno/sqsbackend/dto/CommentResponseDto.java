package com.studheupno.sqsbackend.dto;

import com.studheupno.sqsbackend.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;


@Data
@AllArgsConstructor
public class CommentResponseDto {

    private String id;
    private String userEmail;
    private String content;
    private Instant createdAt;

    public CommentResponseDto(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.userEmail = commentEntity.getUser().getEmail();
        this.content = commentEntity.getContent();
        this.createdAt = commentEntity.getCreatedAt();
    }
}

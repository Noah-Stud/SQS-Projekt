package com.studheupno.sqsbackend.requests;

import com.studheupno.sqsbackend.entity.CommentEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "comment")
public class CommentRequestResponse {
    @Id
    private String id;
    private String content;
    private Instant createdAt;

    public CommentRequestResponse(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.content = commentEntity.getContent();
        this.createdAt = commentEntity.getCreatedAt();
    }
}

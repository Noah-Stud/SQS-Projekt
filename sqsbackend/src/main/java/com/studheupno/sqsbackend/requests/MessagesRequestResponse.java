package com.studheupno.sqsbackend.requests;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.studheupno.sqsbackend.entity.MessageEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages response")
public class MessagesRequestResponse {
    @Id
    private String id;
    private String userEmail;
    private String content;
    private CommentRequestResponse[] comments;
    private Instant createdAt;
    private String quote;

    private List<String> likes = new ArrayList<>();

    public MessagesRequestResponse(MessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.userEmail = "UserEmail";
        this.content = messageEntity.getContent();
        this.comments = messageEntity.getComments().stream().map(CommentRequestResponse::new).toArray(CommentRequestResponse[]::new);
        this.createdAt = messageEntity.getCreatedAt();
        this.quote = messageEntity.getQuote();
        this.likes = messageEntity.getLikes();
    }
}
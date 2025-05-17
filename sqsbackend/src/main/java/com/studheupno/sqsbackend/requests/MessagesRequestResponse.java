package com.studheupno.sqsbackend.requests;

import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesRequestResponse {

    private String id;
    private String userEmail;
    private String content;
    private List<CommentRequestResponse> comments;
    private Instant createdAt;
    private String quote;

    private List<String> likes = new ArrayList<>();

    public MessagesRequestResponse(MessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.userEmail = messageEntity.getUser().getEmail();
        this.content = messageEntity.getContent();
        this.comments = messageEntity.getComments().stream().map(CommentRequestResponse::new).toList();
        this.createdAt = messageEntity.getCreatedAt();
        this.quote = messageEntity.getQuote();
        this.likes = messageEntity.getLikes().stream().map(UserEntity::getEmail).collect(Collectors.toList());
    }
}
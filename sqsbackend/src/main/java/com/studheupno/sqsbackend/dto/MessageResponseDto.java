package com.studheupno.sqsbackend.dto;

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
public class MessageResponseDto {

    private String id;
    private String userEmail;
    private String content;
    private List<CommentResponseDto> comments;
    private Instant createdAt;
    private String quote;

    private List<String> likes = new ArrayList<>();

    public MessageResponseDto(MessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.userEmail = messageEntity.getUser().getEmail();
        this.content = messageEntity.getContent();
        this.comments = messageEntity.getComments().stream().map(CommentResponseDto::new)
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                .toList();
        this.createdAt = messageEntity.getCreatedAt();
        this.quote = messageEntity.getQuote();
        this.likes = messageEntity.getLikes().stream().map(UserEntity::getEmail).collect(Collectors.toList());
    }
}
package com.studheupno.sqsbackend.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "message")
public class MessageEntity {

    @Id
    private String id;

    private String userId;

    private String content;

    private Instant createdAt;

    List<String> like = new ArrayList<>();

    List<CommentEntity> comment = new ArrayList<>();
}

package com.studheupno.sqsbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotNull
    @ManyToOne
    private UserEntity user;
    @NotNull
    private String content;
    @NotNull
    private Instant createdAt;
    @NotNull
    private String quote;

    @OneToMany
    private List<UserEntity> likes = new ArrayList<>();
    @OneToMany
    private List<CommentEntity> comments = new ArrayList<>();
}

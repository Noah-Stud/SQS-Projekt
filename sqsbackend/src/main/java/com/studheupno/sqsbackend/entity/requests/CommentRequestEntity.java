package com.studheupno.sqsbackend.entity.requests;

import com.studheupno.sqsbackend.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestEntity {
    private CommentEntity commentEntity;
    private String postId;
}

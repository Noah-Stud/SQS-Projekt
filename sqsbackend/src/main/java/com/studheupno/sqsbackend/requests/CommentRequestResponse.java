package com.studheupno.sqsbackend.requests;

import com.studheupno.sqsbackend.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestResponse {
    private CommentEntity commentEntity;
    private String postId;
}

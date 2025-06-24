package com.studheupno.sqsbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NotBlank
    private String messageId;
    @NotBlank
    private String commentContent;
}

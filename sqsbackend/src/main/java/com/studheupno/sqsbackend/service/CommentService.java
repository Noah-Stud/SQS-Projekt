package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.CommentRequestDto;
import com.studheupno.sqsbackend.dto.RequestResponseDto;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Service that is responsible for actions involving Comments.
 */
@Service
public class CommentService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageService messageService;

    /**
     * Creates a new Comment as a specific User for a specific Message.
     *
     * @param inputCommentRequestDto Contains the Message for which the Comment is to be created and the Comments content
     * @param inputUserEmail         User for which the Comment is to be created
     * @return RequestResponse
     */
    public RequestResponseDto insertComment(CommentRequestDto inputCommentRequestDto, String inputUserEmail) {
        Optional<UserEntity> optionalUserEntity = userRepo.findByEmail(inputUserEmail);

        if (optionalUserEntity.isEmpty()) {
            RequestResponseDto responseObj = new RequestResponseDto();
            responseObj.setStatus("fail");
            responseObj.setMessage("User with email: " + inputUserEmail + " not found");
            responseObj.setPayload(null);
            return responseObj;
        }
        return messageService.updateMessageByComment(inputCommentRequestDto, optionalUserEntity.get());
    }
}

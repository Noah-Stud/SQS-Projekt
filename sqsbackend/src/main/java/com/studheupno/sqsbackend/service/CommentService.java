package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.CommentRequest;
import com.studheupno.sqsbackend.dto.RequestResponse;
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
     * @param inputCommentRequest Contains the Message for which the Comment is to be created and the Comments content
     * @param inputUserEmail      User for which the Comment is to be created
     * @return RequestResponse
     */
    public RequestResponse insertComment(CommentRequest inputCommentRequest, String inputUserEmail) {
        Optional<UserEntity> optionalUserEntity = userRepo.findByEmail(inputUserEmail);

        if (optionalUserEntity.isEmpty()) {
            RequestResponse responseObj = new RequestResponse();
            responseObj.setStatus("fail");
            responseObj.setMessage("User with email: " + inputUserEmail + " not found");
            responseObj.setPayload(null);
            return responseObj;
        }
        return messageService.updateMessageByComment(inputCommentRequest, optionalUserEntity.get());
    }
}

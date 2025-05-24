package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.CommentRequest;
import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageService messageService;

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

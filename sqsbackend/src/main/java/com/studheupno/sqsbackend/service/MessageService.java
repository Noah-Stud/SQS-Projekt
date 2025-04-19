package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.entity.CommentEntity;
import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.requests.RequestResponse;
import com.studheupno.sqsbackend.repo.CommentRepo;
import com.studheupno.sqsbackend.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private CommentRepo commentRepo;

    public RequestResponse insertMessage(MessageEntity inputPost) {
        RequestResponse responseObj = new RequestResponse();
        inputPost.setCreatedAt(Instant.now());
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(messageRepo.save(inputPost));
        return responseObj;
    }

    public RequestResponse getAllMessages() {
        RequestResponse responseObj = new RequestResponse();
        List<MessageEntity> messages = messageRepo.findAll();

        if (messages.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("cannot find messages");
            responseObj.setPayload(null);
        } else {
            responseObj.setStatus("success");
            responseObj.setMessage("success");
            responseObj.setPayload(messages);
        }
        return responseObj;
    }

    public RequestResponse updateMessageByComment(String inputMessageId, CommentEntity inputComment) {
        RequestResponse responseObj = new RequestResponse();
        Optional<MessageEntity> optPost = messageRepo.findById(inputMessageId);

        if (optPost.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("cannot find message with id: " + inputMessageId);
            responseObj.setPayload(null);
        } else {
            inputComment.setCreatedAt(Instant.now());
            commentRepo.save(inputComment);

            MessageEntity targetMessage = optPost.get();
            List<CommentEntity> commentList = targetMessage.getComments();
            if (commentList == null) {
                commentList = new ArrayList<>();
            }
            commentList.add(inputComment);
            targetMessage.setComments(commentList);
            messageRepo.save(targetMessage);

            responseObj.setStatus("success");
            responseObj.setMessage("message was updated successfully");
            responseObj.setPayload(targetMessage);
        }
        return responseObj;
    }
}
package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.entity.CommentEntity;
import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.entity.requests.ResponseObjectEntity;
import com.studheupno.sqsbackend.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MessageService messageService;

    public ResponseObjectEntity insertComment(CommentEntity inputComment, String inputMessageId) {
        return messageService.updateMessageByComment(inputMessageId, inputComment);
    }

    public ResponseObjectEntity getComments(String inputMessageId) {
        ResponseObjectEntity responseObj = new ResponseObjectEntity();
        Optional<MessageEntity> optTargetPost = messageRepo.findById(inputMessageId);
        if (optTargetPost.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("Message with id: " + inputMessageId + " not found");
            responseObj.setPayload(null);
            return responseObj;
        } else {
            MessageEntity targetPost = optTargetPost.get();
            List<CommentEntity> commentList = targetPost.getComments();
            if (!commentList.isEmpty()) {
                responseObj.setStatus("success");
                responseObj.setMessage("success");
                responseObj.setPayload(commentList);
                return responseObj;
            } else {
                responseObj.setStatus("success");
                responseObj.setMessage("Message with id " + inputMessageId + " does not have any comment");
                responseObj.setPayload(null);
                return responseObj;
            }
        }
    }
}

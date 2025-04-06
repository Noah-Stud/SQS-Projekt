package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.entity.requests.ResponseObjectEntity;
import com.studheupno.sqsbackend.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public ResponseObjectEntity insertMessage(MessageEntity inputPost) {
        ResponseObjectEntity responseObj = new ResponseObjectEntity();
        inputPost.setCreatedAt(Instant.now());
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(messageRepo.save(inputPost));
        return responseObj;
    }

    public ResponseObjectEntity getAllMessages() {
        ResponseObjectEntity responseObj = new ResponseObjectEntity();
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
}
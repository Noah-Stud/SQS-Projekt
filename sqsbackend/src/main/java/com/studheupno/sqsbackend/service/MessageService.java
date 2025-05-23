package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.CommentRequest;
import com.studheupno.sqsbackend.dto.MessagesRequestResponse;
import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.entity.CommentEntity;
import com.studheupno.sqsbackend.entity.MessageEntity;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.CommentRepo;
import com.studheupno.sqsbackend.repo.MessageRepo;
import com.studheupno.sqsbackend.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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
    @Autowired
    private UserRepo userRepo;

    private final QuoteService quoteService = new QuoteService(RestClient.create());
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public RequestResponse insertMessage(String userEmail, String inputContent) {
        logger.info("Trying to insert message for user: {}", userEmail);
        Optional<UserEntity> optionalUserEntity = userRepo.findByEmail(userEmail);
        if (optionalUserEntity.isEmpty()) {
            RequestResponse responseObj = new RequestResponse();
            responseObj.setStatus("fail");
            responseObj.setMessage("User with email: " + userEmail + " not found");
            responseObj.setPayload(null);
            logger.error("user with email: {} does no exist", userEmail);
            return responseObj;
        }

        MessageEntity newMessage = new MessageEntity(null, optionalUserEntity.get(), inputContent, Instant.now(),
                quoteService.getQuote(), new ArrayList<>(), new ArrayList<>());
        newMessage = messageRepo.save(newMessage);
        logger.info("Message for user: {} was created", userEmail);

        RequestResponse responseObj = new RequestResponse();
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(new MessagesRequestResponse(newMessage));
        return responseObj;
    }

    public RequestResponse getMessageById(String id) {
        RequestResponse responseObj = new RequestResponse();
        Optional<MessageEntity> optMessage = messageRepo.findById(id);

        if (optMessage.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("Message with id: " + id + " not found");
            responseObj.setPayload(null);
        } else {
            responseObj.setStatus("success");
            responseObj.setMessage("success");
            responseObj.setPayload(new MessagesRequestResponse(optMessage.get()));
        }
        return responseObj;
    }

    public RequestResponse getAllMessages() {
        RequestResponse responseObj = new RequestResponse();
        List<MessageEntity> messages = messageRepo.findAll();

        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(messages.stream().map(MessagesRequestResponse::new).toList());
        return responseObj;
    }

    public RequestResponse updateMessageByComment(CommentRequest inputCommentContent, UserEntity commentUser) {
        RequestResponse responseObj = new RequestResponse();

        Optional<MessageEntity> optMessage = messageRepo.findById(inputCommentContent.getMessageId());
        if (optMessage.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("Message with id: " + inputCommentContent.getMessageId() + " not found");
            responseObj.setPayload(null);
        } else {
            MessageEntity targetMessage = optMessage.get();

            CommentEntity newComment = new CommentEntity(null, commentUser, inputCommentContent.getCommentContent(),
                    Instant.now());
            newComment = commentRepo.save(newComment);

            List<CommentEntity> commentList = targetMessage.getComments();
            if (commentList == null) {
                commentList = new ArrayList<>();
            }
            commentList.add(newComment);
            targetMessage.setComments(commentList);
            messageRepo.save(targetMessage);

            responseObj.setStatus("success");
            responseObj.setMessage("Comment has been added to message");
            responseObj.setPayload(null);
        }
        return responseObj;
    }

    public RequestResponse updateMessageByLike(String userEmail, String messageId) {
        RequestResponse responseObj = new RequestResponse();

        Optional<MessageEntity> optMessage = messageRepo.findById(messageId);
        if (optMessage.isEmpty()) {
            logger.info("Error: cannot find message with id: {}", messageId);
            responseObj.setStatus("fail");
            responseObj.setMessage("Message with id: " + messageId + " not found");
            responseObj.setPayload(null);
            return responseObj;
        } else {
            Optional<UserEntity> optionalUserEntity = userRepo.findByEmail(userEmail);
            if (optionalUserEntity.isEmpty()) {
                logger.info("Error: cannot find user with email: {}", userEmail);
                responseObj = new RequestResponse();
                responseObj.setStatus("fail");
                responseObj.setMessage("User with email: " + userEmail + " not found");
                responseObj.setPayload(null);
                return responseObj;
            } else {
                return updateLikeList(optMessage.get(), optionalUserEntity.get());
            }
        }
    }

    private RequestResponse updateLikeList(MessageEntity messageEntity, UserEntity userEntity) {
        RequestResponse responseObj = new RequestResponse();

        List<UserEntity> likesList = messageEntity.getLikes();
        if (likesList == null) {
            likesList = new ArrayList<>();
        }

        if (!likesList.contains(userEntity)) {
            likesList.add(userEntity);
        } else {
            likesList.remove(userEntity);
        }

        messageEntity.setLikes(likesList);
        messageEntity = messageRepo.save(messageEntity);
        logger.info("Message with id: {} has been updated", messageEntity.getId());

        responseObj.setStatus("success");
        responseObj.setMessage("update likes to the target post id: " + messageEntity.getId());
        responseObj.setPayload(new MessagesRequestResponse(messageEntity));
        return responseObj;
    }
}
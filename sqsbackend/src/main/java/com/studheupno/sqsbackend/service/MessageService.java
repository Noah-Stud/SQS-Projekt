package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.CommentRequestDto;
import com.studheupno.sqsbackend.dto.MessageResponseDto;
import com.studheupno.sqsbackend.dto.RequestResponseDto;
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

/**
 * Service that is responsible for actions involving Messages.
 */
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

    /**
     * Create new Message for a specific User.
     * If the User does not exist, no Message is created.
     *
     * @param userEmail    User who creates the Message
     * @param inputContent Content of Message
     * @return RequestResponseDto containing MessageResponseDto of the new Message or failure-message.
     */
    public RequestResponseDto insertMessage(String userEmail, String inputContent) {
        logger.info("Trying to insert message for user: {}", userEmail);
        Optional<UserEntity> optionalUserEntity = userRepo.findByEmail(userEmail);
        if (optionalUserEntity.isEmpty()) {
            RequestResponseDto responseObj = new RequestResponseDto();
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

        RequestResponseDto responseObj = new RequestResponseDto();
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(new MessageResponseDto(newMessage));
        return responseObj;
    }

    /**
     * Returns Message with the corresponding ID.
     * If the User does not exist, no Message is returned.
     *
     * @param id ID of the searched Message
     * @return RequestResponseDto containing MessageResponseDto of the found Message or failure-message.
     */
    public RequestResponseDto getMessageById(String id) {
        RequestResponseDto responseObj = new RequestResponseDto();
        Optional<MessageEntity> optMessage = messageRepo.findById(id);

        if (optMessage.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("Message with id: " + id + " not found");
            responseObj.setPayload(null);
        } else {
            responseObj.setStatus("success");
            responseObj.setMessage("success");
            responseObj.setPayload(new MessageResponseDto(optMessage.get()));
        }
        return responseObj;
    }

    /**
     * Returns all Messages saved in Database as a List.
     *
     * @return RequestResponseDto containing List<MessageResponseDto> of all Messages.
     */
    public RequestResponseDto getAllMessages() {
        RequestResponseDto responseObj = new RequestResponseDto();
        List<MessageEntity> messages = messageRepo.findAll();

        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(messages.stream().map(MessageResponseDto::new).toList());
        return responseObj;
    }

    /**
     * Creates new Comment for a specific User under a specific Message.
     * If the Message does not exist, nothing is done.
     *
     * @param inputCommentContent Contains Comment and MessageId
     * @param commentUser         User
     * @return RequestResponseDto with success- or failure-message
     */
    public RequestResponseDto updateMessageByComment(CommentRequestDto inputCommentContent, UserEntity commentUser) {
        RequestResponseDto responseObj = new RequestResponseDto();

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

    /**
     * Add or delete Like of a specific User from a specific Message.
     * If the Message or the User does not exist, nothing is done.
     *
     * @param userEmail Email of the User
     * @param messageId ID of the Message
     * @return RequestResponseDto with success- or failure-message
     */
    public RequestResponseDto updateMessageByLike(String userEmail, String messageId) {
        RequestResponseDto responseObj = new RequestResponseDto();

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
                responseObj = new RequestResponseDto();
                responseObj.setStatus("fail");
                responseObj.setMessage("User with email: " + userEmail + " not found");
                responseObj.setPayload(null);
                return responseObj;
            } else {
                return updateLikeList(optMessage.get(), optionalUserEntity.get());
            }
        }
    }

    /**
     * Adds or deletes User from the LikeList of the Message.
     * If the User is not on the List, he is added
     * If he is already on the List, he is deleted from it.
     *
     * @param messageEntity The Message to be updated
     * @param userEntity    The User to add or delete from the LikeList
     * @return RequestResponseDto containing the MessageResponseDto of the updated Message
     */
    private RequestResponseDto updateLikeList(MessageEntity messageEntity, UserEntity userEntity) {
        RequestResponseDto responseObj = new RequestResponseDto();

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
        responseObj.setPayload(new MessageResponseDto(messageEntity));
        return responseObj;
    }
}
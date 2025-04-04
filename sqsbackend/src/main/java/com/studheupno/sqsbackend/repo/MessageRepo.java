package com.studheupno.sqsbackend.repo;


import com.studheupno.sqsbackend.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepo extends MongoRepository<MessageEntity, String> {
}

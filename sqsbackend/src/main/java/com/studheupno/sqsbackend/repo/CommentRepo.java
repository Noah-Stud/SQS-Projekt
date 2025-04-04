package com.studheupno.sqsbackend.repo;

import com.studheupno.sqsbackend.entity.CommentEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends MongoRepository<CommentEntity, String> {

}

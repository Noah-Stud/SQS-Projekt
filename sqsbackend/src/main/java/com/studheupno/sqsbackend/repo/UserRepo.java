package com.studheupno.sqsbackend.repo;

import java.util.Optional;

import com.studheupno.sqsbackend.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
}

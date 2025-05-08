package com.studheupno.sqsbackend.repo;


import com.studheupno.sqsbackend.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepo extends JpaRepository<MessageEntity, String> {
}

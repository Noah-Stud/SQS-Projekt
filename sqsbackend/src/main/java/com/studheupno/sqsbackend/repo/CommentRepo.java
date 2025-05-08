package com.studheupno.sqsbackend.repo;

import com.studheupno.sqsbackend.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<CommentEntity, String> {

}

package com.studheupno.sqsbackend.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MessageEntityTest {

    @Test
    void test() {
        Instant instant = Instant.now();

        MessageEntity message = new MessageEntity();
        message.setId("id1");
        message.setUserId("user1");
        message.setContent("content1");
        message.setCreatedAt(instant);

        assertEquals("id1", message.getId());
        assertEquals("user1", message.getUserId());
        assertEquals("content1", message.getContent());
        assertEquals(instant, message.getCreatedAt());
    }
}
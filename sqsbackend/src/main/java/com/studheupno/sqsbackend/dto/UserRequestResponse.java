package com.studheupno.sqsbackend.dto;

import com.studheupno.sqsbackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestResponse {

    private String email;

    public UserRequestResponse(UserEntity userEntity) {
        this.email = userEntity.getEmail();
    }
}

package com.studheupno.sqsbackend.dto;

import com.studheupno.sqsbackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String email;

    public UserResponseDto(UserEntity userEntity) {
        this.email = userEntity.getEmail();
    }
}

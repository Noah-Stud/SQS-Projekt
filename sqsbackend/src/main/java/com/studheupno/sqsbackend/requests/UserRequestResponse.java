package com.studheupno.sqsbackend.requests;

import com.studheupno.sqsbackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UserRequestResponse")
public class UserRequestResponse {

    private String name;
    private String email;

    public UserRequestResponse(UserEntity userEntity) {
        this.name = userEntity.getName();
        this.email = userEntity.getEmail();
    }
}

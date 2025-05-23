package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.dto.UserRequestResponse;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public RequestResponse findAll() {
        RequestResponse responseObj = new RequestResponse();
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(userRepo.findAll().stream().map(UserRequestResponse::new).toList());
        return responseObj;
    }

    public RequestResponse findByEmail(String email) {
        RequestResponse responseObj = new RequestResponse();
        Optional<UserEntity> optUser = userRepo.findByEmail(email);

        if (optUser.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("User with email: " + email + " not found");
            responseObj.setPayload(null);
        } else {
            responseObj.setStatus("success");
            responseObj.setMessage("success");
            responseObj.setPayload(new UserRequestResponse(optUser.get()));

        }
        return responseObj;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optUser = userRepo.findByEmail(email);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("Cannot find user with email: " + email);
        } else {
            UserEntity foundUser = optUser.get();
            return new User(foundUser.getEmail(), foundUser.getPassword(), foundUser.getAuthorities());
        }
    }
}

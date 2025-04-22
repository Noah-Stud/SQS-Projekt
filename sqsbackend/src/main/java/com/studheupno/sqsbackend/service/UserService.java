package com.studheupno.sqsbackend.service;

import java.util.Optional;

import com.studheupno.sqsbackend.requests.RequestResponse;
import com.studheupno.sqsbackend.requests.UserRequestResponse;
import com.studheupno.sqsbackend.repo.UserRepo;
import com.studheupno.sqsbackend.entity.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public RequestResponse findAll() {
        RequestResponse responseObj = new RequestResponse();
        responseObj.setPayload(userRepo.findAll().stream().map(UserRequestResponse::new).toList());
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        return responseObj;
    }

    public RequestResponse findByEmail(String email) {
        RequestResponse responseObj = new RequestResponse();
        Optional<UserEntity> optUser = userRepo.findByEmail(email);

        if (optUser.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("user id: " + email + " does no exist");
            responseObj.setPayload(null);
        } else {
            responseObj.setPayload(new UserRequestResponse(optUser.get()));
            responseObj.setStatus("success");
            responseObj.setMessage("success");
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
//            String role = foundUser.getRole();
//            Set<GrantedAuthority> ga = new HashSet<>();
//            ga.add(new SimpleGrantedAuthority(role));
            return new User(foundUser.getEmail(), foundUser.getPassword(), foundUser.getAuthorities());
        }
    }
}

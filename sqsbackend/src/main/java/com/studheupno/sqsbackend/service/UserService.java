package com.studheupno.sqsbackend.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.studheupno.sqsbackend.entity.requests.ResponseObjectEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import com.studheupno.sqsbackend.entity.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public ResponseObjectEntity findAll() {
        ResponseObjectEntity responseObj = new ResponseObjectEntity();
        responseObj.setPayload(userRepo.findAll());
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        return responseObj;
    }

    public ResponseObjectEntity findById(String id) {
        ResponseObjectEntity responseObj = new ResponseObjectEntity();
        Optional<UserEntity> optUser = userRepo.findById(id);

        if (optUser.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("user id: " + id + " does no exist");
            responseObj.setPayload(null);
        } else {
            responseObj.setPayload(optUser.get());
            responseObj.setStatus("success");
            responseObj.setMessage("success");
        }
        return responseObj;
    }

    public ResponseObjectEntity saveUser(UserEntity inputUser) {
        ResponseObjectEntity responseObj = new ResponseObjectEntity();
        Optional<UserEntity> optUser = userRepo.findByEmail(inputUser.getEmail());

        if (optUser.isPresent()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("Email address " + inputUser.getEmail() + " already existed");
            responseObj.setPayload(null);
        } else {
            userRepo.save(inputUser);
            responseObj.setStatus("success");
            responseObj.setMessage("success");
            responseObj.setPayload(inputUser);
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
            String role = foundUser.getRole();
            Set<GrantedAuthority> ga = new HashSet<>();
            ga.add(new SimpleGrantedAuthority(role));
            return new User(foundUser.getEmail(), foundUser.getPassword(), ga);
        }
    }
}

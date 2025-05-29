package com.studheupno.sqsbackend.service;

import com.studheupno.sqsbackend.dto.RequestResponseDto;
import com.studheupno.sqsbackend.dto.UserResponseDto;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Service that is responsible for actions involving Users.
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    /**
     * Returns all Users (List<UserRequestResponse>) that are saved in the database as a RequestResponse.
     *
     * @return UserRequestResponse of all Users
     */
    public RequestResponseDto findAll() {
        RequestResponseDto responseObj = new RequestResponseDto();
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(userRepo.findAll().stream().map(UserResponseDto::new).toList());
        return responseObj;
    }

    /**
     * Returns User (UserRequestResponse) belonging to the given Email-Address as a RequestResponse.
     *
     * @param email Email-Address of the User.
     * @return RequestResponse
     */
    public RequestResponseDto findByEmail(String email) {
        RequestResponseDto responseObj = new RequestResponseDto();
        Optional<UserEntity> optUser = userRepo.findByEmail(email);

        if (optUser.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("User with email: " + email + " not found");
            responseObj.setPayload(null);
        } else {
            responseObj.setStatus("success");
            responseObj.setMessage("success");
            responseObj.setPayload(new UserResponseDto(optUser.get()));

        }
        return responseObj;
    }

    /**
     * Returns UserDetails belonging to the given Email-Address.
     *
     * @param email Email-Address of the User.
     * @return UserDetails
     */
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

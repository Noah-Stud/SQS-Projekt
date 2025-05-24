package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.dto.LogInRequestDto;
import com.studheupno.sqsbackend.dto.RegisterRequestDto;
import com.studheupno.sqsbackend.dto.RequestResponse;
import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.repo.UserRepo;
import com.studheupno.sqsbackend.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepo userRepository;

    @PostMapping("/login")
    public ResponseEntity<RequestResponse> authenticateUser(@RequestBody LogInRequestDto loginRequestDto) {
        logger.info(loginRequestDto.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getEmail(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateJwtToken(authentication);
            return new ResponseEntity<>(new RequestResponse("success", "authenticated",
                    jwt), HttpStatus.OK);
        } catch (Exception e) {
            logger.info("{} failed", loginRequestDto.getEmail());
            return new ResponseEntity<>(new RequestResponse("fail", "unauthenticated",
                    null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RequestResponse> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {

        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            logger.info("New user not created: Email {} is already in use!", registerRequestDto.getEmail());
            return new ResponseEntity<>(new RequestResponse("fail", "Error: Email is already in use!",
                    null), HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity(null, registerRequestDto.getEmail(),
                encoder.encode(registerRequestDto.getPassword()), "");
        userRepository.save(user);
        logger.info("New user created: {}", user.getEmail());
        return new ResponseEntity<>(new RequestResponse("success", "User registered successfully!",
                null), HttpStatus.OK);
    }
}

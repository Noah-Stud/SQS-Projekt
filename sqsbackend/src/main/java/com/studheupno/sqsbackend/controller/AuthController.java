package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.entity.UserEntity;
import com.studheupno.sqsbackend.entity.requests.LogInRequest;
import com.studheupno.sqsbackend.entity.requests.RegisterRequest;
import com.studheupno.sqsbackend.repo.UserRepo;
import com.studheupno.sqsbackend.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    JwtService jwtUtils;
    @Autowired
    UserRepo userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LogInRequest loginRequest) {
        logger.info(loginRequest.toString());
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            return ResponseEntity.ok().body(jwt);
        }catch (Exception e) {
            logger.error("SignIn Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        UserEntity user = new UserEntity(null, registerRequest.getName(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()), "");
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}

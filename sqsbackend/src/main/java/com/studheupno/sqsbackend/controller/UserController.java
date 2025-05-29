package com.studheupno.sqsbackend.controller;

import com.studheupno.sqsbackend.dto.RequestResponseDto;
import com.studheupno.sqsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Rest-Controller that is responsable for User-Related-Requests (not for Authentication)
 */
@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get-Request (/api/user/v1/getAll) that returns all user in the database
     *
     * @return RequestResponse containing a list of UserRequestResponses
     */
    @GetMapping("/getAll")
    public ResponseEntity<RequestResponseDto> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
}
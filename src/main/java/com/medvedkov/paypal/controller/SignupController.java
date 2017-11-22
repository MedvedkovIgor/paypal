package com.medvedkov.paypal.controller;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.entity.UserRole;
import com.medvedkov.paypal.service.SignupServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class SignupController {

    private SignupServiceImpl signupService;
    private static final Logger logger = LoggerFactory.getLogger(SignupServiceImpl.class);

    public SignupController(SignupServiceImpl signupService) {
        this.signupService = signupService;
    }

    @RequestMapping(value = "/signuplocal", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody User user) {
        user.setRoles(Arrays.asList(new UserRole("USER")));
        signupService.addUser(user);
        logger.info("Sign up new user " + user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
package com.medvedkov.paypal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.entity.UserRole;
import com.medvedkov.paypal.service.SignupServiceImpl;

import java.util.Arrays;

@RestController
public class SignupController {

    private SignupServiceImpl signupService;

    public SignupController(SignupServiceImpl signupService){
        this.signupService=signupService;
    }

    @RequestMapping(value = "/signuplocal", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody User user) {
        user.setRoles(Arrays.asList(new UserRole("USER")));
        signupService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
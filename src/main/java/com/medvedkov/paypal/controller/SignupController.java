package com.medvedkov.paypal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.entity.UserRole;
import com.medvedkov.paypal.service.SignupService;

import java.util.Arrays;

@RestController
public class SignupController {

    private SignupService signupService;

    public SignupController(SignupService signupService){
        this.signupService=signupService;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody User user) {
        user.setRoles(Arrays.asList(new UserRole("USER")));
        signupService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
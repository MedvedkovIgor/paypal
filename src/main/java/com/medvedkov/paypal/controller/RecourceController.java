package com.medvedkov.paypal.controller;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/resources")
public class RecourceController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/users")
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }
}

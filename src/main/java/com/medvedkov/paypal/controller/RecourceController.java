package com.medvedkov.paypal.controller;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/signin/resources")
public class RecourceController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/users")
    public Iterable<User> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findAll();
    }
}

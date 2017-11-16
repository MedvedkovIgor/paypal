package com.medvedkov.paypal.service;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.entity.UserRole;
import com.medvedkov.paypal.repository.UserRepository;

@Service
@Transactional
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PostConstruct
    private void setupDefaultUser() {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin",
                    passwordEncoder.encode("pass"),
                    Arrays.asList(new UserRole("USER"), new UserRole("ADMIN"))));
        }
    }

}
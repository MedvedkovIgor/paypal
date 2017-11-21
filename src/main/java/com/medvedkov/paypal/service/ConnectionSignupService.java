package com.medvedkov.paypal.service;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.entity.UserRole;
import com.medvedkov.paypal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ConnectionSignupService implements ConnectionSignUp {

    private UserRepository userRepository;
    //FIXME сделать autowired
    private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public ConnectionSignupService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public String execute(Connection<?> connection) {
        System.out.println("signup === ");
        final User user = new User();
        user.setUsername(connection.getDisplayName());

        user.setPassword(passwordEncoder.encode("pass"));
        user.setRoles(Arrays.asList(new UserRole("USER")));
        userRepository.save(user);
        return user.getUsername();
    }
}
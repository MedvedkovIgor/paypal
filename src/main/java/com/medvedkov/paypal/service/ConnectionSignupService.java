package com.medvedkov.paypal.service;

import com.medvedkov.paypal.entity.User;
import com.medvedkov.paypal.entity.UserRole;
import com.medvedkov.paypal.repository.UserRepository;
import com.medvedkov.paypal.security.SignInAdapterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(SignInAdapterImpl.class);

    public ConnectionSignupService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public String execute(Connection<?> connection) {
        logger.info("Sign up new user");
        final User user = new User();
        user.setUsername(connection.getDisplayName());

        user.setPassword(passwordEncoder.encode("pass"));
        user.setRoles(Arrays.asList(new UserRole("USER")));
        userRepository.save(user);
        return user.getUsername();
    }
}
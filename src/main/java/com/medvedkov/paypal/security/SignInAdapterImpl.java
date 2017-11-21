package com.medvedkov.paypal.security;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

@Service
public class SignInAdapterImpl implements SignInAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SignInAdapterImpl.class);

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        logger.info("Sign in as "+connection.getDisplayName());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(connection.getDisplayName(),
                        null, Arrays.asList(new SimpleGrantedAuthority("FACEBOOK_USER"))));
        return null;
    }
}
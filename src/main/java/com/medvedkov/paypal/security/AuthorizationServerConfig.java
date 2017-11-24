package com.medvedkov.paypal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private static final String REALM = "FPC";

    private TokenStore tokenStore;
    private UserDetailsServiceImpl userDetailsService;
    private UserApprovalHandler userApprovalHandler;
    private AuthenticationManager authenticationManager;
    private DataSource dataSource;

    @Autowired
    public AuthorizationServerConfig(TokenStore tokenStore, UserDetailsServiceImpl userDetailsService,
                                     UserApprovalHandler userApprovalHandler,
                                     @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
                                     @Qualifier("dataSource") DataSource dataSource) {
        this.tokenStore = tokenStore;
        this.userDetailsService = userDetailsService;
        this.userApprovalHandler = userApprovalHandler;
        this.authenticationManager = authenticationManager;
        this.dataSource=dataSource;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.realm(REALM);
    }

}
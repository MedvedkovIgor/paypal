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

@Configuration
@EnableAuthorizationServer
@ConfigurationProperties(prefix = "security")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private static final String REALM = "FPC";

    private TokenStore tokenStore;
    private UserDetailsServiceImpl userDetailsService;
    private UserApprovalHandler userApprovalHandler;
    private AuthenticationManager authenticationManager;

    private String clientId;
    private String clientSecret;
    private String passwordGrantType;
    private String scopeRead;
    private String scopeWrite;
    private String scopeTrust;
    private int accessTokenValidityTime;
    private int refreshTokenValidityTime;

    @Autowired
    public AuthorizationServerConfig(TokenStore tokenStore, UserDetailsServiceImpl userDetailsService,
                                     UserApprovalHandler userApprovalHandler,
                                     @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager) {
        this.tokenStore = tokenStore;
        this.userDetailsService = userDetailsService;
        this.userApprovalHandler = userApprovalHandler;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .authorizedGrantTypes(passwordGrantType, "refresh_token")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .scopes(scopeRead, scopeWrite, scopeTrust)
                .accessTokenValiditySeconds(accessTokenValidityTime)
                .refreshTokenValiditySeconds(refreshTokenValidityTime);
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

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setPasswordGrantType(String passwordGrantType) {
        this.passwordGrantType = passwordGrantType;
    }

    public void setScopeRead(String scopeRead) {
        this.scopeRead = scopeRead;
    }

    public void setScopeWrite(String scopeWrite) {
        this.scopeWrite = scopeWrite;
    }

    public void setScopeTrust(String scopeTrust) {
        this.scopeTrust = scopeTrust;
    }

    public void setAccessTokenValidityTime(int accessTokenValidityTime) {
        this.accessTokenValidityTime = accessTokenValidityTime;
    }

    public void setRefreshTokenValidityTime(int refreshTokenValidityTime) {
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }
}
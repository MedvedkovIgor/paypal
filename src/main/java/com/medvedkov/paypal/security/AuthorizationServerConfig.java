package com.medvedkov.paypal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;
    private UserDetailsServiceImpl userDetailsService;
    private UserApprovalHandler userApprovalHandler;
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    private static final String REALM = "FPC";

    @Value("${security.client-id}")
    private String clientId;
    @Value("${security.client-secret}")
    private String clientSecret;
    @Value("${security.password-grant-type}")
    private String passGrant;
    @Value("${security.scope-read}")
    private String scopeRead;
    @Value("${security.scope-write}")
    private String scopeWrite;
    @Value("${security.scope-trust}")
    private String scopeTrust;
    @Value("${security.access-token-validity-seconds}")
    private int accessTokenValidityTime;
    @Value("${security.refresh-token-validity-seconds}")
    private int refreshTokenValidityTime;

    @Autowired
    public AuthorizationServerConfig(TokenStore tokenStore, UserDetailsServiceImpl userDetailsService,
                                     UserApprovalHandler userApprovalHandler,
                                     AuthenticationManager authenticationManager) {
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
                .authorizedGrantTypes(passGrant, "refresh_token")
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
}
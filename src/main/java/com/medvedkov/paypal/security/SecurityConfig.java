package com.medvedkov.paypal.security;

import com.medvedkov.paypal.service.ConnectionSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@ConfigurationProperties(prefix = "spring.social")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("devDataSource")
    private DataSource dataSource;

    private ClientDetailsService clientDetailsService;
    private UserDetailsServiceImpl userDetailsService;
    private UsersConnectionRepository usersConnectionRepository;

    private String facebookAppId;
    private String facebookAppSecret;
    private String googleClientId;
    private String googleClientSecret;

    private ConnectionSignupService connectionSignupService;
    private SignInAdapterImpl signInAdapterImpl;

    public SecurityConfig(ClientDetailsService clientDetailsService, UserDetailsServiceImpl userDetailsService,
                          UsersConnectionRepository usersConnectionRepository,
                          ConnectionSignupService connectionSignupService,
                          SignInAdapterImpl signInAdapterImpl) {
        this.clientDetailsService = clientDetailsService;
        this.userDetailsService = userDetailsService;
        this.usersConnectionRepository = usersConnectionRepository;
        this.connectionSignupService = connectionSignupService;
        this.signInAdapterImpl = signInAdapterImpl;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/**").permitAll()
                .antMatchers("/login*", "/signin/**", "/signup/**").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/signin").permitAll()
                .loginProcessingUrl("/signin/authenticate")
                .failureUrl("/signin?param.error=bad_credentials")
                .and()
                .logout()
                .logoutUrl("/signout")
                .deleteCookies("JSESSIONID")
//                .and().rememberMe()
                .and()
                .httpBasic()
                .realmName("FPC");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore();
//    }
    @Bean
    @Qualifier("devDataSource")
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Primary
    @Bean(name = "devDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //FIXME отключить переопределение ConnectionFactoryLocator в spring boot
    @Bean
    public ProviderSignInController providerSignInController() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new GoogleConnectionFactory(googleClientId, googleClientSecret));
        registry.addConnectionFactory(new FacebookConnectionFactory(facebookAppId, facebookAppSecret));
        return new ProviderSignInController(registry, usersConnectionRepository, signInAdapterImpl);
    }

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    //FIXME У тебя вот этот бин автоваридся UsersConnectionRepository значит он есть в контейнере и его можно просто дополнить как-то
    //работать с дефолтным бином - устанавливать connectionSignupService в конструкторе
    public UsersConnectionRepository usersConnectionRepository() {
        ((InMemoryUsersConnectionRepository) usersConnectionRepository).setConnectionSignUp(connectionSignupService);
        return usersConnectionRepository;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public void setFacebookAppSecret(String facebookAppSecret) {
        this.facebookAppSecret = facebookAppSecret;
    }

    public void setGoogleClientId(String googleClientId) {
        this.googleClientId = googleClientId;
    }

    public void setGoogleClientSecret(String googleClientSecret) {
        this.googleClientSecret = googleClientSecret;
    }
}
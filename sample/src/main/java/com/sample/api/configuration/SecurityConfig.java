package com.sample.api.configuration;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    @Configuration
    @EnableWebSecurity
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
    @ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
    public static class KeycloakConfigurationAdapter extends KeycloakWebSecurityConfigurerAdapter {

        @Autowired
        public SimpleCORSFilter simpleCORSFilter;
        /**
         * Registers the KeycloakAuthenticationProvider with the authentication manager.
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            // simple Authority Mapper to avoid ROLE_
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        @Bean
        public KeycloakConfigResolver KeycloakConfigResolver() {
            return new HeaderBasedConfigResolver();
        }

        /**
         * Defines the session authentication strategy.
         */
        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            // required for bearer-only applications.
            return new NullAuthenticatedSessionStrategy();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterBefore(simpleCORSFilter, ChannelProcessingFilter.class)
                    .sessionManagement()
                    // use previously declared bean
                    .sessionAuthenticationStrategy(sessionAuthenticationStrategy())


                    // keycloak filters for securisation
                    .and()
                    .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                    .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())

                    // add cors options
                    .and().cors()
                    // delegate logout endpoint to spring security

                    .and()
                    .logout()
                    .addLogoutHandler(keycloakLogoutHandler())
                    .logoutUrl("/logout").logoutSuccessHandler(
                    // logout handler for API
                    (HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                            response.setStatus(HttpServletResponse.SC_OK)
            )
                    .and().apply(new CommonSpringKeycloakSecuritAdapter());
        }

    }


    public static class CommonSpringKeycloakSecuritAdapter extends AbstractHttpConfigurer<CommonSpringKeycloakSecuritAdapter, HttpSecurity> {

        @Override
        public void init(HttpSecurity http) throws Exception {
            // any method that adds another configurer
            // must be done in the init method
            http
                    // disable csrf because of API mode
                    .cors().and().csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    .and()
                    // manage routes securisation here
                    .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()

                    // manage routes securisation here
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers(HttpMethod.GET, "/v1/dashboard/attendanceScore/{empCode}").hasAnyRole("user", "admin")
                    .anyRequest().permitAll();

        }

    }
}

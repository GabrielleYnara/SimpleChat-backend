package com.example.simplechatbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    /**
     * Marked with Bean so SpringBoot creates instance. Used to encrypt password when it
     * is passed in from LoginRequest so it is not stored as plain text.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Marked with Bean so SpringBoot creates instance. Used to parse and validate Jwt which
     * is passed from every Http request.
     */
    @Bean
    public JwtRequestFilter authJwtRequestFilter() {
        return new JwtRequestFilter();
    }

    /**
     * Marked with Bean so SpringBoot creates instance. Used to whitelist URL paths for login
     * and register Users. Applies certain security properties. Also applies JwtRequestFiler for every request.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/login/", "/auth/register/").permitAll() // Allow login and registration without authentication
                .antMatchers("/h2-console/**").permitAll() // Allow access to H2 console
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set session creation policy to STATELESS
                .and()
                .csrf().disable() // Disable CSRF protection
                .headers().frameOptions().disable(); // Disable frame options
        http.cors(); // Enable CORS (Cross-Origin Resource Sharing)
        http.addFilterBefore(authJwtRequestFilter(), UsernamePasswordAuthenticationFilter.class); // Add JWT filter
        return http.build();
    }

    /**
     * Marked with Bean so SpringBoot creates instance. Validates that the current user is a valid
     * one in the database.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

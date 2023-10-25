package com.example.simplechatbackend.security;

import com.example.simplechatbackend.model.User;
import com.example.simplechatbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private AuthService authService;

    /**
     * Injects an instance of UserService in MyUserDetailsService
     * @param authService Service used to access a User from the database.
     */
    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Associates a specific User from the database with this instance by using UserService
     * and the User's username, passed in from a Http request.
     * @param username Passed in from a Http request.
     * @return An instance of UserDetails, containing details from the database.
     * @throws UsernameNotFoundException If User with username not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authService.findUserByUsername(username);
        return new MyUserDetails(user);
    }
}

package com.example.simplechatbackend.service;

import com.example.simplechatbackend.exception.InformationAlreadyExistsException;
import com.example.simplechatbackend.model.User;
import com.example.simplechatbackend.model.request.LoginRequest;
import com.example.simplechatbackend.repository.UserRepository;
import com.example.simplechatbackend.security.JWTUtils;
import com.example.simplechatbackend.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    Logger logger  = Logger.getLogger(AuthService.class.getName());

    @Autowired
    public AuthService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public Optional<String> loginUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            return Optional.of(jwtUtils.generateJwtToken(myUserDetails));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> createUser(User user) {
        if (findUserByUsername(user.getUsername()) != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return Optional.of(userRepository.save(user));
        } else {
            throw new InformationAlreadyExistsException("User with username " + user.getUsername() + " already exists.");
        }
    }
}

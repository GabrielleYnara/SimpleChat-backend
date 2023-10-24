package com.example.simplechatbackend.controller;

import com.example.simplechatbackend.model.User;
import com.example.simplechatbackend.model.request.LoginRequest;
import com.example.simplechatbackend.model.response.LoginResponse;
import com.example.simplechatbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "/auth")     //localhost:8081/auth
public class AuthController {

    private AuthService authService;

    static HashMap<String, Object> message = new HashMap<>();

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/login") // localhost:8081/auth/login
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<String> jwtToken = authService.loginUser(loginRequest);
        if (jwtToken.isPresent()) {
            return ResponseEntity.ok(new LoginResponse(jwtToken.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((new LoginResponse("Authentication failed")));
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        Optional<User> newUser = authService.createUser(user);

        if(newUser.isPresent()) {
            message.put("message", "Sucessfully registered new User: " + user.getUsername());
            message.put("data", newUser);
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            message.put("message", "User with username " + user.getUsername() + " already exists.");
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }
}

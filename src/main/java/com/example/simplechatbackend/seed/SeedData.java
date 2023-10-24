package com.example.simplechatbackend.seed;

import com.example.simplechatbackend.model.User;
import com.example.simplechatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class SeedData implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user1 = new User("bobmyers", "password1");
            User user2 = new User("susiemyers", "password2");
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }
}

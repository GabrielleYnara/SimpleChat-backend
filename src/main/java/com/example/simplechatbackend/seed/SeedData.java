package com.example.simplechatbackend.seed;

import com.example.simplechatbackend.model.Room;
import com.example.simplechatbackend.model.User;
import com.example.simplechatbackend.repository.RoomRepository;
import com.example.simplechatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SeedData implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    @Autowired
    public SeedData(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository
    , RoomRepository roomRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("bobmyers");
            user1.setPassword(passwordEncoder.encode("password1"));
            userRepository.save(user1);

            User user2 = new User();
            user2.setUsername("susiemyers");
            user2.setPassword(passwordEncoder.encode("password2"));
            userRepository.save(user2);
        }

        if (roomRepository.count() == 0) {
            Room room1 = new Room();
            room1.setName("Dogs");
            roomRepository.save(room1);

            Room room2 = new Room();
            room2.setName("Cats");
            roomRepository.save(room2);

            Room room3 = new Room();
            room3.setName("Horses");
            roomRepository.save(room3);
        }
    }
}

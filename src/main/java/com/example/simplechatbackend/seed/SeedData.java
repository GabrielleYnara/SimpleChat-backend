package com.example.simplechatbackend.seed;

import com.example.simplechatbackend.model.Chat;
import com.example.simplechatbackend.model.Room;
import com.example.simplechatbackend.model.User;
import com.example.simplechatbackend.repository.ChatRepository;
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

    private final ChatRepository chatRepository;

    @Autowired
    public SeedData(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository,
                    RoomRepository roomRepository, ChatRepository chatRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.chatRepository = chatRepository;
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

            User guest = new User();
            guest.setUsername("Guest");
            guest.setPassword(passwordEncoder.encode("password"));
            userRepository.save(guest);
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

        if (chatRepository.count() == 0) {
            Chat chat1 = new Chat();
            chat1.setRoom(roomRepository.findById(1L).get());
            chat1.setUser(userRepository.findById(1L).get());
            chat1.setMessage("I love dogs!");
            chatRepository.save(chat1);

            Chat chat2 = new Chat();
            chat2.setRoom(roomRepository.findById(1L).get());
            chat2.setUser(userRepository.findById(2L).get());
            chat2.setMessage("Me too!");
            chatRepository.save(chat2);

            Chat chat3 = new Chat();
            chat3.setRoom(roomRepository.findById(1L).get());
            chat3.setUser(userRepository.findById(1L).get());
            chat3.setMessage("My favorites are Samoyeds!");
            chatRepository.save(chat3);
        }
    }
}

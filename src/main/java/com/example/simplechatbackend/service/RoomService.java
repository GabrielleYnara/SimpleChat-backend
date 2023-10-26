package com.example.simplechatbackend.service;

import com.example.simplechatbackend.exception.InformationAlreadyExistsException;
import com.example.simplechatbackend.exception.InformationNotFoundException;
import com.example.simplechatbackend.model.Chat;
import com.example.simplechatbackend.model.Room;
import com.example.simplechatbackend.model.User;
import com.example.simplechatbackend.repository.ChatRepository;
import com.example.simplechatbackend.repository.RoomRepository;
import com.example.simplechatbackend.repository.UserRepository;
import com.example.simplechatbackend.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private UserRepository userRepository;

    private RoomRepository roomRepository;

    private ChatRepository chatRepository;

    private JWTUtils jwtUtils;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Autowired
    public void setChatRepository(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Autowired
    public void setJwtUtils(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public List<Room> getAllBooks() {
        return roomRepository.findAll();
    }

    public Optional<Room> createRoom(Room room) {
        Optional<Room> roomOptional = roomRepository.findByName(room.getName());
        if (roomOptional.isEmpty()) {
            return Optional.of(roomRepository.save(room));
        } else {
            throw new InformationAlreadyExistsException("Room with name " + room.getName() + " already exists.");
        }
    }

    public Optional<Room> getRoomById(Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            return roomOptional;
        } else {
            throw new InformationNotFoundException("Room with id " + roomId + " not found.");
        }
    }

    public Optional<Chat> createChatInRoom(String jwt, Long roomId, Chat chat) {
        String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt.substring(7));
        Optional<User> user = Optional.of(userRepository.findUserByUsername(userNameFromJwtToken));
        Optional<Room> roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isPresent()) {
            chat.setUser(user.get());
            chat.setRoom(roomOptional.get());
            return Optional.of(chatRepository.save(chat));
        } else {
            throw new InformationNotFoundException("Room with id " + roomId + " not found.");
        }
    }

    public Optional<Chat> updateChatById(String jwt, Long roomId, Long chatId, Chat chat) {
        String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt.substring(7));
        Optional<User> user = Optional.of(userRepository.findUserByUsername(userNameFromJwtToken));
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        Optional<Chat> origChat = chatRepository.findById(chatId);

        if (origChat.isPresent()) {
            if (!origChat.get().getUser().getId().equals(user.get().getId())) {
                throw new InformationNotFoundException("Chat with id: " + chatId + " does not belong to current User.");
            }
        } else {
            throw new InformationNotFoundException("Chat with id: " + chatId + " not found.");
        }

        if (roomOptional.isPresent()) {
            if (chat.getMessage().equals(origChat.get().getMessage())) {
                throw new InformationAlreadyExistsException("Chat with id: " + chatId + " already contains this message.");
            } else {
                origChat.get().setMessage(chat.getMessage());
                return Optional.of(chatRepository.save(origChat.get()));
            }
        } else {
            throw new InformationNotFoundException("Room with id " + roomId + " not found.");
        }
    }

    public Optional<Chat> deleteChatById(String jwt, Long roomId, Long chatId) {
        String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt.substring(7));
        Optional<User> user = Optional.of(userRepository.findUserByUsername(userNameFromJwtToken));
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        Optional<Chat> origChat = chatRepository.findById(chatId);

        if (origChat.isPresent()) {
            if (!origChat.get().getUser().getId().equals(user.get().getId())) {
                throw new InformationNotFoundException("Chat with id: " + chatId + " does not belong to current User.");
            }
        } else {
            throw new InformationNotFoundException("Chat with id: " + chatId + " not found.");
        }

        if (roomOptional.isPresent()) {
            chatRepository.delete(origChat.get());
            return origChat;
        } else {
            throw new InformationNotFoundException("Room with id " + roomId + " not found.");
        }
    }
}

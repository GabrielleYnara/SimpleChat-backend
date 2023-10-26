package com.example.simplechatbackend.service;

import com.example.simplechatbackend.exception.InformationAlreadyExistsException;
import com.example.simplechatbackend.exception.InformationNotFoundException;
import com.example.simplechatbackend.model.Chat;
import com.example.simplechatbackend.model.Room;
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

    private RoomRepository roomRepository;

    private ChatRepository chatRepository;

    private JWTUtils jwtUtils;

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
}

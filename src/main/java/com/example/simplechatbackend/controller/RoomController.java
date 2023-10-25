package com.example.simplechatbackend.controller;

import com.example.simplechatbackend.model.Room;
import com.example.simplechatbackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rooms")  //localhost:8081/rooms
public class RoomController {

    private RoomService roomService;

    static HashMap<String, Object> message = new HashMap<>();

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(path = "")  //localhost:8081/rooms
    public ResponseEntity<?> getAllRooms() {
        List<Room> roomList = roomService.getAllBooks();
        if (roomList.isEmpty()) {
            message.put("message", "No Rooms exist in the database");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            message.put("message", "Success!");
            message.put("data", roomList);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createRoom(@RequestBody Room room) {
        Optional<Room> newRoom = roomService.createRoom(room);
        if(newRoom.isPresent()) {
            message.put("message", "Successfully registered new Room: " + room.getName());
            message.put("data", newRoom.get());
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            message.put("message", "Room with name " + room.getName() + " already exists.");
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }
}

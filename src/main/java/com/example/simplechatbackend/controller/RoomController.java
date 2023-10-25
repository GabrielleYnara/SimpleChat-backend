package com.example.simplechatbackend.controller;

import com.example.simplechatbackend.model.Room;
import com.example.simplechatbackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

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
}

package com.example.simplechatbackend.controller;

import com.example.simplechatbackend.model.Chat;
import com.example.simplechatbackend.model.Room;
import com.example.simplechatbackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
            message.put("message", "Successfully created new Room: " + room.getName());
            message.put("data", newRoom.get());
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            message.put("message", "Room with name " + room.getName() + " already exists.");
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(path = "/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable(value = "roomId") Long roomId) {
        Optional<Room> room = roomService.getRoomById(roomId);
        if (room.isPresent()) {
            message.put("message", "Success!");
            message.put("data", room.get());
            message.put("name", room.get().getName());
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            message.put("message", "Room with id " + roomId + " not found.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/{roomId}")
    public ResponseEntity<?> createChatInRoom(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
                                              @PathVariable(value = "roomId") Long roomId, @RequestBody Chat chat) {
        Optional<Chat> newChat = roomService.createChatInRoom(jwt, roomId, chat);
        if (newChat.isPresent()) {
            message.put("message", "Successfully created new Chat: " + chat.getMessage());
            message.put("data", newChat.get());
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            message.put("message", "Room with id " + roomId + " not found.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{roomId}/chats/{chatId}")
    public ResponseEntity<?> updateChatById(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
                                            @PathVariable(value = "roomId") Long roomId,
                                            @PathVariable(value = "chatId") Long chatId, @RequestBody Chat chat) {
        Optional<Chat> updateChat = roomService.updateChatById(jwt, roomId, chatId, chat);
        if (updateChat.isPresent()) {
            message.put("message", "Successfully updated Chat with id: " + updateChat.get().getId());
            message.put("data", updateChat.get());
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            message.put("message", "Room with id " + roomId + " not found or this Chat does not belong to current User.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{roomId}/chats/{chatId}")
    public ResponseEntity<?> deleteChatById(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
                                            @PathVariable(value = "roomId") Long roomId,
                                            @PathVariable(value = "chatId") Long chatId) {
        Optional<Chat> deleteChat = roomService.deleteChatById(jwt, roomId, chatId);
        if (deleteChat.isPresent()) {
            message.put("message", "Successfully deleted Chat with id: " + deleteChat.get().getId());
            message.put("data", deleteChat.get());
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            message.put("message", "Room with id " + roomId + " not found or this Chat does not belong to current User.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{roomId}/chats")
    public ResponseEntity<?> getChatsUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
                                            @PathVariable(value = "roomId") Long roomId) {
        List<String> usernames = roomService.getChatsUsers(jwt, roomId);
        if (!usernames.isEmpty()) {
            message.put("message", "Successfully found Chats for Room with id: " + roomId);
            message.put("data", usernames);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            message.put("message", "This Room has no Chats.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}

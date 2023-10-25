package com.example.simplechatbackend.controller;

import com.example.simplechatbackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/rooms")
public class RoomController {

    private RoomService roomService;

    @Autowired
    public void setUserService(RoomService roomService) {
        this.roomService = roomService;
    }
}

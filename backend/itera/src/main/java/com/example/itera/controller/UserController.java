package com.example.itera.controller;

import com.example.itera.domain.user.UserRepository;
import com.example.itera.domain.user.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @CrossOrigin(origins =  "*", allowedHeaders = "*")
    @GetMapping
    public List<UserResponseDTO> getAll(){
        List<UserResponseDTO> userList = repository.findAll().stream().map(UserResponseDTO::new).toList();
        return userList;
    }
}
package com.example.itera.dto.user;

import com.example.itera.domain.user.User;
import com.example.itera.domain.user.UserRole;

public record UserResponseDTO(String id, String name, String email, String username, String password, Double hourlyRate, Integer dedicatedHours, UserRole userRole) {
    public UserResponseDTO(User user){
        this(user.getId(), user.getName(), user.getEmail(), user.getUsername(), user.getPassword(), user.getHourlyRate(), user.getDedicatedHours(), user.getUserRole());
    }
}
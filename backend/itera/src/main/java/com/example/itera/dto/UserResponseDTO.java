package com.example.itera.dto;

import com.example.itera.domain.user.User;
import com.example.itera.domain.user.UserRole;

public record UserResponseDTO(String id, String nome, String email, String username, String password, Double valorHora, Integer horasDedicadas, UserRole userRole) {
    public UserResponseDTO(User user){
        this(user.getId(), user.getNome(), user.getEmail(), user.getUsername(), user.getPassword(), user.getValorHora(), user.getHorasDedicada(), user.getRole());
    }
}
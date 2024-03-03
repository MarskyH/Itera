package com.example.itera.dto;

import com.example.itera.domain.user.UserRole;

public record RegisterDTO(String nome, String username, String email, String password, Double valorHora,  Integer horasDedicada, UserRole role) {
}

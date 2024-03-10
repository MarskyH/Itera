package com.example.itera.dto.user;

import com.example.itera.domain.user.UserRole;

public record RegisterDTO(String nome, String login, String email, String password, Double valorHora,  Integer horasDedicada, UserRole role) {
}

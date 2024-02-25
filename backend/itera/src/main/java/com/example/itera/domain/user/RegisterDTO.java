package com.example.itera.domain.user;

public record RegisterDTO(String nome, String login, String password, Integer horasDedicada, UserRole role) {
}

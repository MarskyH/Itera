package com.example.itera.domain.user;

public record UserResponseDTO(String id, String nome, String login, String password, Integer horasDedicadas, UserRole userRole) {
    public UserResponseDTO(User user){
        this(user.getId(), user.getNome(), user.getLogin(), user.getPassword(), user.getHorasDedicada(), user.getRole());
    }
}
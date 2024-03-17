package com.example.itera.dto.user;

import com.example.itera.domain.user.UserRole;

public record RegisterDTO(String name, String login, String email, String password, Double hourlyRate,  Integer dedicatedHours, UserRole userRole) {
}

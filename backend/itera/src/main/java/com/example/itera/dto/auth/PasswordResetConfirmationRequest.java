package com.example.itera.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirmationRequest {
    private String token;
    private String newPassword;

}
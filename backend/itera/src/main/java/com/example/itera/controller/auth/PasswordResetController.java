package com.example.itera.controller.auth;

import com.example.itera.dto.auth.PasswordResetConfirmationRequest;
import com.example.itera.dto.auth.PasswordResetRequest;
import com.example.itera.infra.security.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.sendResetPasswordToken(request.getEmail());
            return ResponseEntity.ok("Password reset token sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send reset token");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetConfirmationRequest request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password successfully reset");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token or error resetting password");
        }
    }
}

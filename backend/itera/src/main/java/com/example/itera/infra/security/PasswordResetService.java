package com.example.itera.infra.security;

import com.example.itera.domain.Password.PasswordResetToken;
import com.example.itera.domain.user.User;
import com.example.itera.repository.token.PasswordResetTokenRepository;
import com.example.itera.repository.user.UserRepository;
import com.example.itera.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;  // Supondo que você tenha um serviço de e-mail

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void sendResetPasswordToken(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        // Gerar um token ou código
        String token = UUID.randomUUID().toString();

        // Salvar o token no banco
        PasswordResetToken resetToken = new PasswordResetToken(user, token);
        tokenRepository.save(resetToken);

        // Enviar e-mail com o token (lógica simplificada)
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid or expired token"));

        User user = resetToken.getUser();

        // Atualizar a senha
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Invalidar o token após uso
        tokenRepository.delete(resetToken);
    }
}

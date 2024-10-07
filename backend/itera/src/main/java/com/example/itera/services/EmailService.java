package com.example.itera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Redefinição de Senha";
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;  // URL para o front-end
        String message = "<p>Você solicitou uma redefinição de senha.</p>"
                + "<p>Clique no link abaixo para redefinir sua senha:</p>"
                + "<a href=\"" + resetUrl + "\">Redefinir Senha</a>"
                + "<p>Se você não solicitou isso, ignore este e-mail.</p>";

        sendEmail(to, subject, message);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("seu-email@gmail.com");  // E-mail configurado nas propriedades
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);  // `true` para indicar que o conteúdo é HTML

            mailSender.send(message);
            System.out.println("E-mail de redefinição de senha enviado para: " + to);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar o e-mail", e);
        }
    }
}


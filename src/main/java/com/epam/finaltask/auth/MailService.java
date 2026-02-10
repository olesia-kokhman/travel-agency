package com.epam.finaltask.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetUrl) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Password reset");
        msg.setText("""
                You requested a password reset.

                Reset link:
                %s

                If you didn't request this, ignore this email.
                """.formatted(resetUrl));
        mailSender.send(msg);
    }
}

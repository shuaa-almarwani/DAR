package com.example.DAR.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to,
                          String subject,
                          String message){

        try {
            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mail, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mail);

        } catch (Exception e) {
            throw new RuntimeException("Email not sent");
        }
    }
}
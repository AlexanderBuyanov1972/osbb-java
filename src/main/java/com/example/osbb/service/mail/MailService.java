package com.example.osbb.service.mail;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {
    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${email_to}")
    private String emailTo;

    public void sendActivationMail(String path) {
        try {
            sendEmail(path);
        } catch (Exception error) {
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private void sendEmail(String path) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setSubject(createTitle());
        simpleMailMessage.setText(createText(path));
        try {
            mailSender.send(simpleMailMessage);
          } catch (MailException error) {
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private String createText(String path) {
        return new EmailMessages().createActivationMessageText(path);
    }

    private String createTitle() {
        return new EmailMessages().createActivationMessageTitle();
    }

}


package com.example.osbb.service.mail;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

@Service
public class MailService {
    private static final Logger log = Logger.getLogger(MailService.class);
    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${email_to}")
    private String emailTo;

    public void sendActivationMail(String path) {
        log.info("Method sendActivationMail : enter");
        try {
            sendEmail(path);
            log.info("Method sendActivationMail : exit");
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private void sendEmail(String path) {
        log.info("Method sendEmail : enter");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setSubject(createTitle());
        simpleMailMessage.setText(createText(path));
        try {
            mailSender.send(simpleMailMessage);
            log.info("Method sendEmail : exit");
        } catch (MailException error) {
            log.error("UNEXPECTED SERVER ERROR");
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


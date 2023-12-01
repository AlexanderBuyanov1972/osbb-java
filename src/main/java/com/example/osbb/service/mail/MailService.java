package com.example.osbb.service.mail;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.service.owner.IOwnerService;
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
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${email_to}")
    private String emailTo;

    public void sendActivationMail(String path) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            sendEmail(path);
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private void sendEmail(String path) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setSubject(createTitle());
        simpleMailMessage.setText(createText(path));
        try {
            mailSender.send(simpleMailMessage);
            log.info(messageExit(methodName));
        } catch (MailException error) {
            log.error(ERROR_SERVER);
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

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }
    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}


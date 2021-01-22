package com.asistlab.imagemaker.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private static final Logger logger = LogManager.getLogger();

    private final JavaMailSender javaMail;

    public EmailService(JavaMailSender javaMail) {
        this.javaMail = javaMail;
    }

    public void sendMessage(String email, String subject, String text) {
        try {
            MimeMessage mimeMessage = mimeMessage(email, subject, text);
            javaMail.send(mimeMessage);
        } catch (MessagingException e) {
            logger.info(e.getMessage());
        }
    }

    private MimeMessage mimeMessage(String to, String subject, String text) throws MessagingException {
        MimeMessage message = javaMail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        return message;
    }
}

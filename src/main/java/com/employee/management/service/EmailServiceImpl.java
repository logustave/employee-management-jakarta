package com.employee.management.service;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@RequestScoped
public class EmailServiceImpl implements EmailService {
    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource(name = "mail/JavaMailSession")
    private Session mailSession;

    @Override
    public CompletableFuture<Void> send(String to, String subject, String content) {
        return CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(content);
                Transport.send(message);
            } catch (MessagingException e) {
                log.info("sendEmail :: {}", e.getMessage());
            }
        });
    }
}

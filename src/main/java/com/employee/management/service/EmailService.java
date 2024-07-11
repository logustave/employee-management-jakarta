package com.employee.management.service;

public interface EmailService {
    void send(String to, String subject, String content);
}

package com.employee.management.service;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<Void> send(String to, String subject, String content);
}

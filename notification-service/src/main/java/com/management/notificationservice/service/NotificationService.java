package com.management.notificationservice.service;

import jakarta.mail.MessagingException;

public interface NotificationService {
    void sendEmail(String to, String subject, String body) throws MessagingException;
}

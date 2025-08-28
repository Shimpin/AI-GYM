package com.situ.kdz2026.service;

public interface EmailService {
    void sendVerificationCode(String toEmail, String code);
}

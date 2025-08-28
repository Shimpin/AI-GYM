package com.situ.kdz2026.service;

public interface CodeService {
    String generateCode();
    void saveCode(String email, String code);
    boolean validateCode(String email, String code);
}

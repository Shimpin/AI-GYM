package com.situ.kdz2026.service;

import com.situ.kdz2026.model.User;

public interface UserService {
    User findByPhone(String phone);
    User findByEmail(String email);
}

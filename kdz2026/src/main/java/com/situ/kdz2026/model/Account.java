package com.situ.kdz2026.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Account {
    private String username;
    private String password;
    private String email;
    private String captcha;
    private String captchaId;
}

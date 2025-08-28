package com.situ.kdz2026.service.impl;

import com.situ.kdz2026.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2535665310@qq.com");
        message.setTo(toEmail);
        message.setSubject("您的注册验证码");
        message.setText(
                "【刀羊健身房】\n"+
                "尊敬的管理员\n\n" +
                        "您的验证码是：" + code + "\n\n" +
                        "验证码有效期为5分钟，请尽快使用。\n" +
                        "请妥善保管您的验证码，切勿泄露给他人。\n\n" +
                        "感谢您的使用！"
        );
        mailSender.send(message);
    }
}

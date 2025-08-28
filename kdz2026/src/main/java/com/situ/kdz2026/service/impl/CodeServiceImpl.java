package com.situ.kdz2026.service.impl;

import com.situ.kdz2026.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public String generateCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    @Override
//    public void saveCode(String email, String code) {
//        String key = "code:" + email;
//        // 使用setIfAbsent确保原子性操作
//        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, code, 5, TimeUnit.MINUTES);
//        if (Boolean.FALSE.equals(result)) {
//            // 检查现有键的剩余生存时间
//            Long ttl = redisTemplate.getExpire(key);
//            // 如果剩余时间很短（例如小于10秒），可以考虑允许重新发送
//            if (ttl != null && ttl < 60) { // 假设剩余时间少于60秒时允许重新发送
//                redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
//            } else {
//                throw new RuntimeException("请求过于频繁，请稍后再试");
//            }
//        }
//    }
    public void saveCode(String email, String code) {
        String key = "code:" + email;
        if (redisTemplate.hasKey(key)) {
            throw new RuntimeException("请求过于频繁");
        }
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }
//    public void saveCode(String email, String code) {
//        String key = "code:" + email;
//        // 使用setIfAbsent确保原子性
//        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, code, 5, TimeUnit.MINUTES);
//        if (Boolean.FALSE.equals(result)) {
//            throw new RuntimeException("请求过于频繁，请稍后再试");
//        }
//    }

    @Override
    public boolean validateCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get("code:" + email);
        System.out.println(storedCode);
        return code.equals(storedCode);
    }
}

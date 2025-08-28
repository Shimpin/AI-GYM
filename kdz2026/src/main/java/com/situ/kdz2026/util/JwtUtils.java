package com.situ.kdz2026.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class JwtUtils {
    public static String createJwt(String id,String username, Map<String,Object> payloads, LocalDateTime exprireTime,String secret){
        JWTCreator.Builder builder = JWT.create();
        String jwt = builder.withPayload(payloads)
                .withExpiresAt(exprireTime.toInstant(ZoneOffset.of("+8")))
                .withIssuer("kdz2026")
                .withIssuedAt(Instant.now())
                .withAudience(username)
                .withSubject("身份认证")
                .withJWTId("id")
                .sign(Algorithm.HMAC256(secret));
        return jwt;
    }

}

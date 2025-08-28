package com.situ.kdz2026.common;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.situ.kdz2026.util.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = request.getHeader("Authorization");
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
        try {
            DecodedJWT dj = verifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            String msg = "jwt无效或过期";
            response.setStatus(HttpStatus.UNAUTHORIZED.value());//401
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(JsonResult.fail(401, msg)));
            out.flush();
            return false;
        }
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!response.isCommitted()) {
            response.setHeader("Captcha-Key", "captcha-" + request.getSession().getId());
        }
    }
}

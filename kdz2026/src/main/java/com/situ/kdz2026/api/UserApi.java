package com.situ.kdz2026.api;

import com.situ.kdz2026.model.Account;
import com.situ.kdz2026.model.Admin;
import com.situ.kdz2026.model.User;
import com.situ.kdz2026.service.AdminService;
import com.situ.kdz2026.service.UserService;
import com.situ.kdz2026.util.JsonResult;
import com.situ.kdz2026.util.JwtUtils;
import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApi {
    private RedisTemplate<Object, Object> redisTemplate;
    private UserService userService;
    private AdminService adminService;
    private static final StrongPasswordEncryptor PE = new StrongPasswordEncryptor();
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/captcha")
    public void captcha(String id,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SpecCaptcha captcha = new SpecCaptcha(140, 40, 4);
        resp.setContentType("image/gif");
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);

//        //req.getSession().setAttribute("captcha", captcha.text().toLowerCase());
        redisTemplate.opsForValue().set("captcha-"+id,
                captcha.text().toLowerCase(), Duration.ofMinutes(3));

        captcha.out(resp.getOutputStream());
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResult<?>> login(@RequestBody Account account) {
        String correct = (String) redisTemplate.opsForValue().get("captcha-"+account.getCaptchaId());
        if (!account.getCaptcha().equals(correct)) {
            return ResponseEntity.ok(JsonResult.fail("验证码不正确"));
        }

        User user = userService.findByPhone(account.getUsername());
        if (user == null) {
            return ResponseEntity.ok(JsonResult.fail("用户不存在"));
        }

        //校验密码
        boolean pass = PE.checkPassword(account.getPassword(), user.getPassword());
        if (!pass) {
            return ResponseEntity.ok(JsonResult.fail("密码不正确"));
        }

        //颁发jwt
        String jwt = JwtUtils.createJwt(UUID.randomUUID().toString(),user.getPhone(), Map.of("username",user.getPhone(),"userId",user.getId()), LocalDateTime.now().plusMinutes(30),jwtSecret);
        return ResponseEntity.ok(JsonResult.success(jwt));
    }

    @PostMapping("/login/email")
    public ResponseEntity<JsonResult<?>> loginEmail(@RequestBody Account account) {
        String correct = (String) redisTemplate.opsForValue().get("captcha-"+account.getCaptchaId());
        if (!account.getCaptcha().equals(correct)) {
            return ResponseEntity.ok(JsonResult.fail("验证码不正确"));
        }

        User user = userService.findByEmail(account.getEmail());
        if (user == null) {
            return ResponseEntity.ok(JsonResult.fail("邮箱不存在"));
        }

        //校验密码
        boolean pass = PE.checkPassword(account.getPassword(), user.getPassword());
        if (!pass) {
            return ResponseEntity.ok(JsonResult.fail("密码不正确"));
        }

        //颁发jwt
        String jwt = JwtUtils.createJwt(UUID.randomUUID().toString(),user.getEmail(), Map.of("userEmail",user.getEmail(),"userId",user.getId()), LocalDateTime.now().plusMinutes(30),jwtSecret);
        return ResponseEntity.ok(JsonResult.success(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<JsonResult<?>> save(@RequestBody @Validated Admin member) {
        // 在保存前确保密码被正确加密
        String encryptedPassword = PE.encryptPassword(member.getPassword());
        member.setPassword(encryptedPassword);

        boolean success = this.adminService.save(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("保存管理员失败"));
        }
    }
}

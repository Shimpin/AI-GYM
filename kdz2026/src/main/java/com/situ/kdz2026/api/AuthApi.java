package com.situ.kdz2026.api;

import com.situ.kdz2026.model.Admin;
import com.situ.kdz2026.service.AdminService;
import com.situ.kdz2026.service.CodeService;
import com.situ.kdz2026.service.EmailService;
import com.situ.kdz2026.util.JsonResult;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApi {
    private static final StrongPasswordEncryptor PE = new StrongPasswordEncryptor();
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    private EmailService emailService;

    @Autowired
    private CodeService codeService;

    // 发送验证码接口
    @GetMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestParam String email) {
        String code = codeService.generateCode();
        codeService.saveCode(email, code);
        emailService.sendVerificationCode(email, code);
        return ResponseEntity.ok("验证码已发送");
    }

    // 注册接口（包含验证码验证）
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin request) {
        if (!codeService.validateCode(request.getEmail(), request.getCode())) {
            System.out.println(request.getEmail());
            System.out.println(request.getCode());
            return ResponseEntity.badRequest().body("验证码错误或已过期");
        }
        String encryptedPassword = PE.encryptPassword(request.getPassword());
        request.setPassword(encryptedPassword);

        boolean success = this.adminService.save(request);
        if (success) {
            String msg = "注册成功";
            return ResponseEntity.ok(JsonResult.success(request,msg));
        } else {
            return ResponseEntity.ok(JsonResult.fail("注册失败"));
        }
    }
}

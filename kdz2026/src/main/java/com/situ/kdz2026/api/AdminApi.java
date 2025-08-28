package com.situ.kdz2026.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Admin;
import com.situ.kdz2026.service.AdminService;
import com.situ.kdz2026.service.UploadService;
import com.situ.kdz2026.util.JsonResult;
import jakarta.validation.constraints.Size;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminApi {
    private AdminService adminService;
    private UploadService uploadService;
    private static final StrongPasswordEncryptor PE = new StrongPasswordEncryptor();

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping
    public ResponseEntity<JsonResult<?>> findAll(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            Admin msb) {
        //分页对象
        Page<Admin> page = new Page<>(pageNo, pageSize);
        //查询全部管理员
        page = this.adminService.findAll(page, msb);

        return ResponseEntity.ok(JsonResult.success(page));
    }

    //批量删除
    @DeleteMapping
    public ResponseEntity<JsonResult<?>> deleteByIds(
            @RequestBody
            @Validated
            @Size(min = 1, message = "要删除的管理员ID至少1个") Integer[] ids) {
        int count = this.adminService.deleteByIds(List.of(ids));

        if (count == 0) {
            return ResponseEntity.ok(JsonResult.fail("删除管理员失败"));
        } else {
            return ResponseEntity.ok(JsonResult.success(count));
        }
    }

    //保存管理员信息
    @PostMapping
    public ResponseEntity<JsonResult<?>> save(@RequestBody @Validated Admin member) {
        if(member.getPassword().length()<10){
            String encryptedPassword = PE.encryptPassword(member.getPassword());
            member.setPassword(encryptedPassword);
        }
        boolean success = this.adminService.save(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("保存管理员失败"));
        }
    }

    //上传管理员头像
    @PostMapping("/avatar")
    public ResponseEntity<JsonResult<?>> uploadMemberAvatar(MultipartFile file) {
        String path = this.uploadService.uploadImage(file, "coach_avatar");
        return ResponseEntity.ok(JsonResult.success(path));
    }

    //修改管理员信息
    @PutMapping
    public ResponseEntity<JsonResult<?>> update(@RequestBody @Validated Admin member) {
        if(member.getPassword().length()<10){
            String encryptedPassword = PE.encryptPassword(member.getPassword());
            member.setPassword(encryptedPassword);
        }
        boolean success = this.adminService.update(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改管理员失败"));
        }
    }
    @PutMapping("/password")
    public ResponseEntity<JsonResult<?>> updatePassword(@RequestBody @Validated Admin member) {

        if(member.getPassword().length()<10){
            String encryptedPassword = PE.encryptPassword(member.getPassword());
            member.setPassword(encryptedPassword);
        }
        boolean success = this.adminService.updatePassword(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改管理员失败"));
        }
    }

}

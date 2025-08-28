package com.situ.kdz2026.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Member;
import com.situ.kdz2026.model.search.MemberSearchBean;
import com.situ.kdz2026.service.MemberService;
import com.situ.kdz2026.service.UploadService;
import com.situ.kdz2026.util.JsonResult;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberApi {
    private MemberService memberService;
    private UploadService uploadService;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping
    public ResponseEntity<JsonResult<?>> findAll(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            MemberSearchBean msb) {
        //分页对象
        Page<Member> page = new Page<>(pageNo, pageSize);
        //查询全部会员
        page = this.memberService.findAll(page, msb);

        return ResponseEntity.ok(JsonResult.success(page));
    }

    //批量删除
    @DeleteMapping
    public ResponseEntity<JsonResult<?>> deleteByIds(
            @RequestBody
            @Validated
            @Size(min = 1, message = "要删除的会员ID至少1个") Integer[] ids) {
        int count = this.memberService.deleteByIds(List.of(ids));

        if (count == 0) {
            return ResponseEntity.ok(JsonResult.fail("删除会员失败"));
        } else {
            return ResponseEntity.ok(JsonResult.success(count));
        }
    }

    //保存会员信息
    @PostMapping
    public ResponseEntity<JsonResult<?>> save(@RequestBody @Validated Member member) {
        boolean success = this.memberService.save(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("保存会员失败"));
        }
    }

    //上传会员头像
    @PostMapping("/avatar")
    public ResponseEntity<JsonResult<?>> uploadMemberAvatar(MultipartFile file) {
        String path = this.uploadService.uploadImage(file, "member_avatar");
        return ResponseEntity.ok(JsonResult.success(path));
    }

    //修改会员信息
    @PutMapping
    public ResponseEntity<JsonResult<?>> update(@RequestBody @Validated Member member) {
        boolean success = this.memberService.update(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改会员失败"));
        }
    }
}

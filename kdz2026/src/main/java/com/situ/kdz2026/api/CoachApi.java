package com.situ.kdz2026.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Coach;
import com.situ.kdz2026.model.Course;
import com.situ.kdz2026.model.search.CourseSearchBean;
import com.situ.kdz2026.service.CoachService;
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
@RequestMapping(value = "/api/v1/coach", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoachApi {
    private CoachService coachService;
    private UploadService uploadService;

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Autowired
    public void setCoachService(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    public ResponseEntity<JsonResult<?>> findAll(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            Coach msb) {
        //分页对象
        Page<Coach> page = new Page<>(pageNo, pageSize);
        //查询全部教练
        page = this.coachService.findAll(page, msb);

        return ResponseEntity.ok(JsonResult.success(page));
    }

    //批量删除
    @DeleteMapping
    public ResponseEntity<JsonResult<?>> deleteByIds(
            @RequestBody
            @Validated
            @Size(min = 1, message = "要删除的教练ID至少1个") Integer[] ids) {
        int count = this.coachService.deleteByIds(List.of(ids));

        if (count == 0) {
            return ResponseEntity.ok(JsonResult.fail("删除教练失败"));
        } else {
            return ResponseEntity.ok(JsonResult.success(count));
        }
    }

    //保存教练信息
    @PostMapping
    public ResponseEntity<JsonResult<?>> save(@RequestBody @Validated Coach member) {
        boolean success = this.coachService.save(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("保存教练失败"));
        }
    }

    //上传教练头像
    @PostMapping("/avatar")
    public ResponseEntity<JsonResult<?>> uploadMemberAvatar(MultipartFile file) {
        String path = this.uploadService.uploadImage(file, "coach_avatar");
        return ResponseEntity.ok(JsonResult.success(path));
    }

    //修改教练信息
    @PutMapping
    public ResponseEntity<JsonResult<?>> update(@RequestBody @Validated Coach member) {
        boolean success = this.coachService.update(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改教练失败"));
        }
    }
}

package com.situ.kdz2026.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.dao.CourseMapper;
import com.situ.kdz2026.model.Course;
import com.situ.kdz2026.model.Member;
import com.situ.kdz2026.model.search.CourseSearchBean;
import com.situ.kdz2026.model.search.MemberSearchBean;
import com.situ.kdz2026.service.CourseService;
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
@RequestMapping(value = "/api/v1/course", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseApi {
    private CourseService courseService;
    private UploadService uploadService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping
    public ResponseEntity<JsonResult<?>> findAll(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            CourseSearchBean msb) {
        //分页对象
        Page<Course> page = new Page<>(pageNo, pageSize);
        //查询全部课程
        page = this.courseService.findAll(page, msb);

        return ResponseEntity.ok(JsonResult.success(page));
    }

    //批量删除
    @DeleteMapping
    public ResponseEntity<JsonResult<?>> deleteByIds(
            @RequestBody
            @Validated
            @Size(min = 1, message = "要删除的课程ID至少1个") Integer[] ids) {
        int count = this.courseService.deleteByIds(List.of(ids));

        if (count == 0) {
            return ResponseEntity.ok(JsonResult.fail("删除课程失败"));
        } else {
            return ResponseEntity.ok(JsonResult.success(count));
        }
    }

    //保存课程信息
    @PostMapping
    public ResponseEntity<JsonResult<?>> save(@RequestBody @Validated Course member) {
        boolean success = this.courseService.save(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("保存课程失败"));
        }
    }

    //上传课程头像
    @PostMapping("/avatar")
    public ResponseEntity<JsonResult<?>> uploadMemberAvatar(MultipartFile file) {
        String path = this.uploadService.uploadImage(file, "course_avatar");
        return ResponseEntity.ok(JsonResult.success(path));
    }

    //修改课程信息
    @PutMapping
    public ResponseEntity<JsonResult<?>> update(@RequestBody @Validated Course member) {
        boolean success = this.courseService.update(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改课程失败"));
        }
    }
}

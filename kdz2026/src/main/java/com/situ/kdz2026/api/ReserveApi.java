package com.situ.kdz2026.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Course;
import com.situ.kdz2026.model.Reserve;
import com.situ.kdz2026.model.search.CourseSearchBean;
import com.situ.kdz2026.model.search.ReserveSearchBean;
import com.situ.kdz2026.service.ReserveService;
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
@RequestMapping(value = "/api/v1/reserve", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReserveApi {
    private ReserveService reserveService;
    private UploadService uploadService;

    @Autowired
    public void setReserveService(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping
    public ResponseEntity<JsonResult<?>> findAll(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            ReserveSearchBean msb) {
        //分页对象
        Page<Reserve> page = new Page<>(pageNo, pageSize);
        //查询全部课程
        page = this.reserveService.findAll(page, msb);

        return ResponseEntity.ok(JsonResult.success(page));
    }
    @GetMapping("/status")
    public ResponseEntity<JsonResult<?>> findReserve(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            ReserveSearchBean rs) {
        //分页对象
        Page<Reserve> page = new Page<>(pageNo, pageSize);
        //查询全部课程
        page = this.reserveService.findReserve(page, rs);

        return ResponseEntity.ok(JsonResult.success(page));
    }

    //批量删除
    @DeleteMapping
    public ResponseEntity<JsonResult<?>> deleteByIds(
            @RequestBody
            @Validated
            @Size(min = 1, message = "要删除的课程ID至少1个") Integer[] ids) {
        int count = this.reserveService.deleteByIds(List.of(ids));

        if (count == 0) {
            return ResponseEntity.ok(JsonResult.fail("删除课程失败"));
        } else {
            return ResponseEntity.ok(JsonResult.success(count));
        }
    }

    //保存课程信息
    @PostMapping
    public ResponseEntity<JsonResult<?>> save(@RequestBody @Validated Reserve member) {
        boolean success = this.reserveService.save(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("保存课程失败"));
        }
    }

    //修改课程信息
    @PutMapping
    public ResponseEntity<JsonResult<?>> update(@RequestBody @Validated Reserve member) {
        boolean success = this.reserveService.update(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改课程失败"));
        }
    }
    @PutMapping("/evaluate")
    public ResponseEntity<JsonResult<?>> updateReserve(@RequestBody @Validated Reserve member) {
        boolean success = this.reserveService.update(member);
        if (success) {
            return ResponseEntity.ok(JsonResult.success(member));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改课程失败"));
        }
    }
}

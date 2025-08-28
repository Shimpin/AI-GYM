package com.situ.kdz2026.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Course;
import com.situ.kdz2026.model.search.CourseSearchBean;

import java.util.List;

public interface CourseService {
    Page<Course> findAll(Page<Course> page, CourseSearchBean cs);
    int deleteByIds(List<Integer> ids);
    boolean save(Course course);
    boolean update(Course course);
}

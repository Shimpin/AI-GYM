package com.situ.kdz2026.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.dao.CourseMapper;
import com.situ.kdz2026.model.Course;
import com.situ.kdz2026.model.search.CourseSearchBean;
import com.situ.kdz2026.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@CacheConfig(cacheNames = "com.situ.kdz2026.service.impl.CourseServiceImpl")
@Service
public class CourseServiceImpl implements CourseService {
    private CourseMapper courseMapper;

    @Autowired
    public void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Cacheable(key = "'course-'+#cs")
    @Override
    public Page<Course> findAll(Page<Course> page, CourseSearchBean cs) {
        return courseMapper.findAll(page, cs);
    }

    @CacheEvict(allEntries = true)
    @Override
    public int deleteByIds(List<Integer> ids) {
        return courseMapper.deleteByIds(ids);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean save(Course course) {
        return courseMapper.insert(course) == 1;
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean update(Course course) {
        return courseMapper.updateById(course) == 1;
    }
}

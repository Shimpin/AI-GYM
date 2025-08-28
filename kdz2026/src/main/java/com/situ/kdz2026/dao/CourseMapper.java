package com.situ.kdz2026.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Course;
import com.situ.kdz2026.model.search.CourseSearchBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface CourseMapper extends BaseMapper<Course> {

Page<Course> findAllWithCoachName(Page<Course> page, @Param("csb") CourseSearchBean csb);

    // 修改原有的 findAll 方法，调用关联查询
    default Page<Course> findAll(Page<Course> page, CourseSearchBean msb) {
        return findAllWithCoachName(page, msb);
    }
}

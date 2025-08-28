package com.situ.kdz2026.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Coach;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoachMapper extends BaseMapper<Coach> {
    default Page<Coach> findAll(Page<Coach> page, Coach ca){
        LambdaQueryWrapper<Coach> qw = Wrappers.lambdaQuery(ca);
        return selectPage(page, qw);
    }
}

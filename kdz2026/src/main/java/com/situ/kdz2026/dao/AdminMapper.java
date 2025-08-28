package com.situ.kdz2026.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    default Page<Admin> findAll(Page<Admin> page, Admin admin){
        LambdaQueryWrapper<Admin> qw = Wrappers.lambdaQuery(admin);
        return selectPage(page, qw);
    }

    int updateByPassword(Admin admin);
}

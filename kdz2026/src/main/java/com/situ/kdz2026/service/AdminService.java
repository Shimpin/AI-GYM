package com.situ.kdz2026.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Admin;

import java.util.List;


public interface AdminService {
    Page<Admin> findAll(Page<Admin> page, Admin admin);
    boolean save(Admin admin);
    boolean update(Admin admin);
    int deleteByIds(List<Integer> ids);
    boolean updatePassword(Admin admin);
}

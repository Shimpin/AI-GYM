package com.situ.kdz2026.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.dao.AdminMapper;
import com.situ.kdz2026.model.Admin;
import com.situ.kdz2026.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {
    private AdminMapper adminMapper;

    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public Page<Admin> findAll(Page<Admin> page, Admin admin) {
        return adminMapper.findAll(page, admin);
    }

    @Override
    public boolean save(Admin admin) {
        return adminMapper.insert(admin)==1;
    }

    @Override
    public boolean update(Admin admin) {
        return adminMapper.updateById(admin)==1;
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return adminMapper.deleteByIds(ids);
    }

    @Override
    public boolean updatePassword(Admin admin) {
        return adminMapper.updateByPassword(admin)==1;
    }
}

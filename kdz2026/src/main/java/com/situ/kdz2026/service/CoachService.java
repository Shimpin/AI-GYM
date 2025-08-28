package com.situ.kdz2026.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Coach;

import java.util.List;

public interface CoachService {
    Page<Coach> findAll(Page<Coach> page, Coach cs);
    int deleteByIds(List<Integer> ids);
    boolean save(Coach coach);
    boolean update(Coach coach);
}

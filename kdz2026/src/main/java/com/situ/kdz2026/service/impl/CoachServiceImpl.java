package com.situ.kdz2026.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.dao.CoachMapper;
import com.situ.kdz2026.model.Coach;
import com.situ.kdz2026.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachServiceImpl implements CoachService {
    private CoachMapper coachMapper;

    @Autowired
    public void setCoachMapper(CoachMapper coachMapper) {
        this.coachMapper = coachMapper;
    }

    @Override
    public Page<Coach> findAll(Page<Coach> page, Coach cs) {
        return coachMapper.findAll(page, cs);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return coachMapper.deleteByIds(ids);
    }

    @Override
    public boolean save(Coach coach) {
        return coachMapper.insert(coach) == 1;
    }

    @Override
    public boolean update(Coach coach) {
        return coachMapper.updateById(coach) == 1;
    }
}

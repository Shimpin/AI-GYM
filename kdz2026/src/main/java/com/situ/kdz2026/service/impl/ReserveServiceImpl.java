package com.situ.kdz2026.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.dao.ReserveMapper;
import com.situ.kdz2026.model.Reserve;
import com.situ.kdz2026.model.search.ReserveSearchBean;
import com.situ.kdz2026.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@CacheConfig(cacheNames = "com.situ.kdz2026.service.impl.ReserveServiceImpl")
@Service
public class ReserveServiceImpl implements ReserveService {
    private ReserveMapper reserveMapper;

    @Autowired
    public void setReserveMapper(ReserveMapper reserveMapper) {
        this.reserveMapper = reserveMapper;
    }

    @Override
    public Page<Reserve> findAll(Page<Reserve> page, ReserveSearchBean cs) {
        return reserveMapper.findAll(page, cs);
    }

    @CacheEvict(allEntries = true)
    @Override
    public int deleteByIds(List<Integer> ids) {
        return reserveMapper.deleteByIds(ids);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean save(Reserve reserve) {
        return reserveMapper.insert(reserve)==1;
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean update(Reserve reserve) {
        return reserveMapper.updateById(reserve)==1;
    }

    @Cacheable(key = "'reserve-'+#rs")
    @Override
    public Page<Reserve> findReserve(Page<Reserve> page, ReserveSearchBean rs) {
        return reserveMapper.findReserve(page, rs);
    }
}

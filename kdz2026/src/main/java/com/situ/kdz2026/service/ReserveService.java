package com.situ.kdz2026.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Reserve;
import com.situ.kdz2026.model.search.ReserveSearchBean;

import java.util.List;

public interface ReserveService {
    Page<Reserve> findAll(Page<Reserve> page, ReserveSearchBean cs);
    int deleteByIds(List<Integer> ids);
    boolean save(Reserve course);
    boolean update(Reserve course);
    Page<Reserve> findReserve(Page<Reserve> page, ReserveSearchBean rs);
}

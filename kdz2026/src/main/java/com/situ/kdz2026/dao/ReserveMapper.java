package com.situ.kdz2026.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Reserve;
import com.situ.kdz2026.model.search.ReserveSearchBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReserveMapper extends BaseMapper<Reserve> {

    Page<Reserve> findAllReservation(Page<Reserve> page, @Param("csb") ReserveSearchBean csb);
    Page<Reserve> findReservation(Page<Reserve> page, @Param("asd") ReserveSearchBean asd);
    default Page<Reserve> findAll(Page<Reserve> page, ReserveSearchBean ca){
        return findAllReservation(page, ca);
    }
    default Page<Reserve> findReserve(Page<Reserve> page,ReserveSearchBean rs){
        return findReservation(page, rs);
    }
}

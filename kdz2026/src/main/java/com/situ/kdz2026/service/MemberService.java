package com.situ.kdz2026.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Member;
import com.situ.kdz2026.model.search.MemberSearchBean;

import java.util.List;

public interface MemberService {
    /**
     * 查询全部会员
     *
     * @param page 分页对象
     * @param msb  查询条件
     * @return 结果
     */
    Page<Member> findAll(Page<Member> page, MemberSearchBean msb);

    //批量删除
    int deleteByIds(List<Integer> ids);

    //保存会员
    boolean save(Member member);

    boolean update(Member member);
}

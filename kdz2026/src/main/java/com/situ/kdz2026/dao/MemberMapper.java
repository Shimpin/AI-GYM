package com.situ.kdz2026.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Member;
import com.situ.kdz2026.model.search.MemberSearchBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
    //查询全部
    default Page<Member> findAll(Page<Member> page, MemberSearchBean msb) {
        LambdaQueryWrapper<Member> qw = Wrappers.lambdaQuery(msb);
        if (msb.getCreateFrom() != null) {
            qw.ge(Member::getCreateTime, msb.getCreateFrom());
        }
        if (msb.getCreateTo() != null) {
            qw.lt(Member::getCreateTime, msb.getCreateTo());
        }
        return selectPage(page, qw);
    }
}

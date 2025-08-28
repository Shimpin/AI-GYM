package com.situ.kdz2026.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.dao.MemberMapper;
import com.situ.kdz2026.model.Member;
import com.situ.kdz2026.model.search.MemberSearchBean;
import com.situ.kdz2026.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private MemberMapper memberMapper;

    @Autowired
    public void setMemberMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public Page<Member> findAll(Page<Member> page, MemberSearchBean msb) {
        return memberMapper.findAll(page, msb);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return memberMapper.deleteByIds(ids);
    }

    @Override
    public boolean save(Member member) {
        return memberMapper.insert(member) == 1;
    }

    @Override
    public boolean update(Member member) {
        return memberMapper.updateById(member) == 1;
    }
}

package com.situ.kdz2026;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.api.MemberApi;
import com.situ.kdz2026.dao.MemberMapper;
import com.situ.kdz2026.model.Member;
import com.situ.kdz2026.model.search.MemberSearchBean;
import com.situ.kdz2026.service.MemberService;
import com.situ.kdz2026.util.JsonResult;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest
public class MyTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberApi memberApi;

    @Test
    void contextLoads() {
        //当spring应用上下文准备就绪之后，写的测试代码
        System.out.println("spring context ready");
    }

    @DisplayName("单元测试hello")
    @Test
    void testHello() {
        Integer a = 10;

        //测试断言
        Assertions.assertEquals(10, a);
    }

    @DisplayName("测试获取所有会员")
    @Test
    void testFindAllMembers() {
        Page<Member> page = new Page<>(1, 5);
        MemberSearchBean msb = new MemberSearchBean();
        msb.setName("晓");
        page = this.memberService.findAll(page, msb);

        long total = page.getTotal();//总数
        Assertions.assertEquals(4, total);
        List<Member> members = page.getRecords();//所有查询记录

        //Assertions.assertEquals("张晓晓",members.get(0).getName());
    }

    @DisplayName("测试memberMapper")
    @Test
    void testMemberMapper() {
        Page<Member> page = new Page<>(1, 5);
        MemberSearchBean msb = new MemberSearchBean();
        msb.setName("小");
        page = memberMapper.findAll(page, msb);
        Assertions.assertEquals(3, page.getTotal());
    }

    @DisplayName("测试memberApi")
    @Test
    void testMemberApi() {
        MemberSearchBean msb = new MemberSearchBean();
        msb.setName("强");
        ResponseEntity<JsonResult<?>> result = this.memberApi.findAll(1, 5, msb);
        JsonResult<?> json = result.getBody();
        Page<Member> page = (Page) json.getData();

        long total = page.getTotal();

        Assertions.assertEquals(3, total);
    }


    @Test
    void buildPwd() {
        StrongPasswordEncryptor pe = new StrongPasswordEncryptor();
        String encrypt = pe.encryptPassword("123456");
        System.out.println(encrypt);
    }
}

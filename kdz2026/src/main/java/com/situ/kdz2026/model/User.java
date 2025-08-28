package com.situ.kdz2026.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@TableName("admin")
@Getter
@Setter
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String phone;
    private String email;
    private String password;
}

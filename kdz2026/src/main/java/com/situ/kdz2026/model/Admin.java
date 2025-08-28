package com.situ.kdz2026.model;


import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@TableName("admin")
@Getter
@Setter
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "用户名不可为空")
    @TableField(condition = SqlCondition.LIKE, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @NotBlank(message = "密码不可为空")
    private String password;
    private String avatar;
    @Pattern(regexp = "^\\d{11}$", message = "手机号必须是11位")
    private String phone;
    @Email(message = "邮箱格式不正确")
    @TableField(condition = SqlCondition.LIKE, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String email;
    private String Code;
}

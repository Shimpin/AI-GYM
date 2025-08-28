package com.situ.kdz2026.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@TableName("coach")
@Getter
@Setter
public class Coach {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(condition = SqlCondition.LIKE, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    private String avatar;
    @NotBlank(message = "手机号不可为空")
    @Pattern(regexp = "^\\d{11}$", message = "手机号必须是11位")
    @TableField(condition = SqlCondition.LIKE, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String phone;
    private String recommend;
    @TableField(condition = SqlCondition.LIKE, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String wechat;
}

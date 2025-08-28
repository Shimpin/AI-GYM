package com.situ.kdz2026.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.situ.kdz2026.util.AuditEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员
 */
@TableName("member")
@Getter
@Setter
public class Member {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "手机号不可为空")
    @Pattern(regexp = "^\\d{11}$", message = "手机号必须是11位")
    @TableField(condition = SqlCondition.LIKE, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String phone;

    private String password;

    @NotBlank(message = "会员姓名不可为空")
    @TableField(condition = SqlCondition.LIKE, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //头像
    private String avatar;
}

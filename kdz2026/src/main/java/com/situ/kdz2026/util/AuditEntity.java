package com.situ.kdz2026.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 审计字段
 */
@Getter
@Setter
public abstract class AuditEntity {
    private String createdBy;
    private LocalDate createdTime;
    private String updatedBy;
    private LocalDate updatedTime;
}

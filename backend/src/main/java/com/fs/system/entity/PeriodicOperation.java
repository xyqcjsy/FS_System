package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 定期业务记录实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
public class PeriodicOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 定期业务ID
     */
    private Long id;

    /**
     * 业务类型（计提折旧、摊销、结转等）
     */
    private String operationType;

    /**
     * 业务期间（如：2025-01）
     */
    private String operationPeriod;

    /**
     * 关联凭证ID
     */
    private Long voucherId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 状态（Pending/Executed/Cancelled）
     */
    private String status;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime executedAt;

    /**
     * 说明
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
}


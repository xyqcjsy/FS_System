package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对账记录实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
public class Reconciliation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对账记录ID
     */
    private Long id;

    /**
     * 对账科目ID
     */
    private Long subjectId;

    /**
     * 科目名称（冗余字段，用于展示）
     */
    private String subjectName;

    /**
     * 对账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate reconciliationDate;

    /**
     * 账面余额
     */
    private BigDecimal bookBalance;

    /**
     * 银行余额
     */
    private BigDecimal bankBalance;

    /**
     * 差异金额
     */
    private BigDecimal differenceAmount;

    /**
     * 对账状态（Pending/Reconciled/Discrepancy）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对账人
     */
    private String reconciledBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
}


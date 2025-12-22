package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fs.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 凭证实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Voucher extends BaseEntity {

    /**
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 凭证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate voucherDate;

    /**
     * 凭证类型（Manual/Auto）
     */
    private String voucherType;

    /**
     * 来源类型（Manual/Purchase/Sales/Expense）
     */
    private String sourceType;

    /**
     * 来源单据ID
     */
    private Long sourceId;

    /**
     * 借方总额
     */
    private BigDecimal totalDebit;

    /**
     * 贷方总额
     */
    private BigDecimal totalCredit;

    /**
     * 状态（Draft/Posted/Cancelled）
     */
    private String status;

    /**
     * 记账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime postedAt;

    /**
     * 摘要
     */
    private String abstractText;

    /**
     * 备注
     */
    private String remark;

    /**
     * 制单人
     */
    private String createdBy;
    
    /**
     * 凭证分录列表
     */
    private java.util.List<VoucherEntry> entries;
}


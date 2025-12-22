package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 费用报销明细实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
public class ExpenseClaimItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    private Long id;

    /**
     * 报销单ID
     */
    private Long claimId;

    /**
     * 费用类型
     */
    private String expenseType;

    /**
     * 费用发生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expenseDate;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 费用说明
     */
    private String description;

    /**
     * 附件URL
     */
    private String attachmentUrl;

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


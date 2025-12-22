package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 税务处理记录实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
public class TaxRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 税务记录ID
     */
    private Long id;

    /**
     * 税期（如：2025-01）
     */
    private String taxPeriod;

    /**
     * 税种
     */
    private String taxType;

    /**
     * 应税金额
     */
    private BigDecimal taxableAmount;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 状态（Pending/Declared/Paid）
     */
    private String status;

    /**
     * 申报日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate declareDate;

    /**
     * 缴纳日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate paymentDate;

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


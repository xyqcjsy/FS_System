package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fs.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 费用报销实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExpenseClaim extends BaseEntity {

    /**
     * 报销单编号
     */
    private String claimNo;

    /**
     * 报销员工ID
     */
    private Long employeeId;

    /**
     * 报销日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate claimDate;

    /**
     * 报销总额
     */
    private BigDecimal totalAmount;

    /**
     * 状态（Draft/Submitted/Approved/Paid/Posted/Rejected）
     */
    private String status;

    /**
     * 付款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate paymentDate;

    /**
     * 是否已过账
     */
    private Boolean isPosted;

    /**
     * 过账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime postedAt;

    /**
     * 备注
     */
    private String remark;
    
    /**
     * 员工姓名（查询时关联）
     */
    private String employeeName;
    
    /**
     * 报销明细列表
     */
    private java.util.List<ExpenseClaimItem> items;
}


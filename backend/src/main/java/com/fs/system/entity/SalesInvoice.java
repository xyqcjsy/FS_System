package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fs.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售订单/发票实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesInvoice extends BaseEntity {

    /**
     * 发票编号
     */
    private String invoiceNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 开票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate invoiceDate;

    /**
     * 销售总额
     */
    private BigDecimal totalAmount;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 最终金额
     */
    private BigDecimal finalAmount;

    /**
     * 状态（Draft/Confirmed/Delivered/Received/Posted/Cancelled）
     */
    private String status;

    /**
     * 收款方式
     */
    private String paymentMethod;

    /**
     * 收款日期
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
     * 客户名称（查询时关联）
     */
    private String customerName;
    
    /**
     * 发票明细列表
     */
    private java.util.List<SalesInvoiceItem> items;
}


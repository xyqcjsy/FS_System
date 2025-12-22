package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fs.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购订单实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrder extends BaseEntity {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 订单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate orderDate;

    /**
     * 订单总额
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
     * 状态（Draft/Confirmed/Received/Paid/Posted/Cancelled）
     */
    private String status;

    /**
     * 付款方式
     */
    private String paymentMethod;

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
     * 供应商名称（查询时关联）
     */
    private String supplierName;
    
    /**
     * 订单明细列表
     */
    private java.util.List<PurchaseOrderItem> items;
}


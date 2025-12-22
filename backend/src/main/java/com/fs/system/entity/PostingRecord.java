package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 过账记录实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
public class PostingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 过账记录ID
     */
    private Long id;

    /**
     * 来源类型（Purchase/Sales/Expense）
     */
    private String sourceType;

    /**
     * 来源单据ID
     */
    private Long sourceId;

    /**
     * 来源单据编号
     */
    private String sourceNo;

    /**
     * 生成的凭证ID
     */
    private Long voucherId;

    /**
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 过账金额
     */
    private BigDecimal postedAmount;

    /**
     * 过账人
     */
    private String postedBy;

    /**
     * 过账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime postedAt;

    /**
     * 备注
     */
    private String remark;
}


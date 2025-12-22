package com.fs.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 凭证分录实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
public class VoucherEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分录ID
     */
    private Long id;

    /**
     * 凭证ID
     */
    private Long voucherId;

    /**
     * 分录行号
     */
    private Integer lineNumber;

    /**
     * 会计科目ID
     */
    private Long subjectId;

    /**
     * 借方金额
     */
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    private BigDecimal creditAmount;

    /**
     * 摘要
     */
    private String abstractText;

    /**
     * 辅助信息
     */
    private String auxiliaryInfo;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
    
    /**
     * 科目编码（查询时关联）
     */
    private String subjectCode;
    
    /**
     * 科目名称（查询时关联）
     */
    private String subjectName;
}


package com.fs.system.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 现金流量项目实体
 */
@Data
public class CashFlowItem {
    
    /**
     * 现金流量项目ID
     */
    private Long id;
    
    /**
     * 项目编码
     */
    private String itemCode;
    
    /**
     * 项目名称
     */
    private String itemName;
    
    /**
     * 活动类别（经营活动、投资活动、筹资活动）
     */
    private String category;
    
    /**
     * 流向（流入、流出）
     */
    private String direction;
    
    /**
     * 父项目ID
     */
    private Long parentId;
    
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    
    /**
     * 是否启用
     */
    private Boolean isEnabled;
    
    /**
     * 说明
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}


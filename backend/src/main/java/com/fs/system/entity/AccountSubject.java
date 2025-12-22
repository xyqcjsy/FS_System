package com.fs.system.entity;

import com.fs.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会计科目实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountSubject extends BaseEntity {

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 科目类型（Asset/Liability/Equity/Income/Expense）
     */
    private String subjectType;

    /**
     * 父科目ID，0表示顶级科目
     */
    private Long parentId;

    /**
     * 科目层级
     */
    private Integer level;

    /**
     * 是否叶子节点（1-是，0-否）
     */
    private Boolean isLeaf;

    /**
     * 余额方向（Debit/Credit）
     */
    private String balanceDirection;

    /**
     * 是否启用（1-启用，0-停用）
     */
    private Boolean isEnabled;

    /**
     * 科目说明
     */
    private String description;
}


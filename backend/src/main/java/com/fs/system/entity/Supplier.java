package com.fs.system.entity;

import com.fs.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Supplier extends BaseEntity {

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 税号
     */
    private String taxNumber;

    /**
     * 开户银行
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 信用等级
     */
    private String creditLevel;

    /**
     * 状态（Active/Inactive）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}


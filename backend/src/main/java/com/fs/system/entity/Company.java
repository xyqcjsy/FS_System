package com.fs.system.entity;

import com.fs.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 企业信息实体类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseEntity {

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 企业规模（小型/中型/大型）
     */
    private String companyScale;

    /**
     * 注册资金
     */
    private BigDecimal registeredCapital;

    /**
     * 营业执照号
     */
    private String businessLicense;

    /**
     * 法人代表
     */
    private String legalRepresentative;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 税号
     */
    private String taxNumber;

    /**
     * 会计年度起始月份（1-12）
     */
    private Integer fiscalYearStart;
}


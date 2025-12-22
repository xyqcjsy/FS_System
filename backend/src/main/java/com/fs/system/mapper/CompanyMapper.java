package com.fs.system.mapper;

import com.fs.system.entity.Company;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 企业信息 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface CompanyMapper {

    /**
     * 查询所有企业信息
     */
    @Select("SELECT * FROM sys_company ORDER BY id DESC")
    List<Company> selectAll();

    /**
     * 根据ID查询企业信息
     */
    @Select("SELECT * FROM sys_company WHERE id = #{id}")
    Company selectById(Long id);

    /**
     * 插入企业信息
     */
    @Insert("INSERT INTO sys_company (company_name, company_scale, registered_capital, business_license, " +
            "legal_representative, contact_phone, address, tax_number, fiscal_year_start) " +
            "VALUES (#{companyName}, #{companyScale}, #{registeredCapital}, #{businessLicense}, " +
            "#{legalRepresentative}, #{contactPhone}, #{address}, #{taxNumber}, #{fiscalYearStart})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Company company);

    /**
     * 更新企业信息
     */
    @Update("UPDATE sys_company SET company_name = #{companyName}, company_scale = #{companyScale}, " +
            "registered_capital = #{registeredCapital}, business_license = #{businessLicense}, " +
            "legal_representative = #{legalRepresentative}, contact_phone = #{contactPhone}, " +
            "address = #{address}, tax_number = #{taxNumber}, fiscal_year_start = #{fiscalYearStart} " +
            "WHERE id = #{id}")
    int update(Company company);

    /**
     * 删除企业信息
     */
    @Delete("DELETE FROM sys_company WHERE id = #{id}")
    int delete(Long id);
}


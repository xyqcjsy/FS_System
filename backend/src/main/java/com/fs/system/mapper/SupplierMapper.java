package com.fs.system.mapper;

import com.fs.system.entity.Supplier;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 供应商 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface SupplierMapper {

    /**
     * 查询所有供应商
     */
    @Select("SELECT * FROM base_supplier ORDER BY id DESC")
    List<Supplier> selectAll();

    /**
     * 根据ID查询供应商
     */
    @Select("SELECT * FROM base_supplier WHERE id = #{id}")
    Supplier selectById(Long id);

    /**
     * 根据供应商编号查询
     */
    @Select("SELECT * FROM base_supplier WHERE supplier_code = #{supplierCode}")
    Supplier selectByCode(String supplierCode);

    /**
     * 根据关键字搜索供应商（支持编号、名称、联系人模糊查询）
     */
    @Select("<script>" +
            "SELECT * FROM base_supplier " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (supplier_code LIKE CONCAT('%', #{keyword}, '%') " +
            "OR supplier_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR contact_person LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "</where>" +
            "ORDER BY id DESC" +
            "</script>")
    List<Supplier> selectByKeyword(String keyword);

    /**
     * 根据状态查询供应商
     */
    @Select("SELECT * FROM base_supplier WHERE status = #{status} ORDER BY id DESC")
    List<Supplier> selectByStatus(String status);

    /**
     * 插入供应商
     */
    @Insert("INSERT INTO base_supplier (supplier_code, supplier_name, contact_person, contact_phone, " +
            "email, address, tax_number, bank_name, bank_account, credit_level, status, remark) " +
            "VALUES (#{supplierCode}, #{supplierName}, #{contactPerson}, #{contactPhone}, " +
            "#{email}, #{address}, #{taxNumber}, #{bankName}, #{bankAccount}, #{creditLevel}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Supplier supplier);

    /**
     * 更新供应商
     */
    @Update("UPDATE base_supplier SET supplier_code = #{supplierCode}, supplier_name = #{supplierName}, " +
            "contact_person = #{contactPerson}, contact_phone = #{contactPhone}, email = #{email}, " +
            "address = #{address}, tax_number = #{taxNumber}, bank_name = #{bankName}, " +
            "bank_account = #{bankAccount}, credit_level = #{creditLevel}, status = #{status}, remark = #{remark} " +
            "WHERE id = #{id}")
    int update(Supplier supplier);

    /**
     * 删除供应商
     */
    @Delete("DELETE FROM base_supplier WHERE id = #{id}")
    int delete(Long id);

    /**
     * 检查供应商编号是否存在
     */
    @Select("SELECT COUNT(*) FROM base_supplier WHERE supplier_code = #{supplierCode}")
    int checkCodeExists(String supplierCode);
}


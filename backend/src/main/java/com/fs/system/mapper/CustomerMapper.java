package com.fs.system.mapper;

import com.fs.system.entity.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 客户 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface CustomerMapper {

    /**
     * 查询所有客户
     */
    @Select("SELECT * FROM base_customer ORDER BY id DESC")
    List<Customer> selectAll();

    /**
     * 根据ID查询客户
     */
    @Select("SELECT * FROM base_customer WHERE id = #{id}")
    Customer selectById(Long id);

    /**
     * 根据客户编号查询
     */
    @Select("SELECT * FROM base_customer WHERE customer_code = #{customerCode}")
    Customer selectByCode(String customerCode);

    /**
     * 根据关键字搜索客户（支持编号、名称、联系人模糊查询）
     */
    @Select("<script>" +
            "SELECT * FROM base_customer " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (customer_code LIKE CONCAT('%', #{keyword}, '%') " +
            "OR customer_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR contact_person LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "</where>" +
            "ORDER BY id DESC" +
            "</script>")
    List<Customer> selectByKeyword(String keyword);

    /**
     * 根据状态查询客户
     */
    @Select("SELECT * FROM base_customer WHERE status = #{status} ORDER BY id DESC")
    List<Customer> selectByStatus(String status);

    /**
     * 插入客户
     */
    @Insert("INSERT INTO base_customer (customer_code, customer_name, contact_person, contact_phone, " +
            "email, address, tax_number, bank_name, bank_account, credit_limit, status, remark) " +
            "VALUES (#{customerCode}, #{customerName}, #{contactPerson}, #{contactPhone}, " +
            "#{email}, #{address}, #{taxNumber}, #{bankName}, #{bankAccount}, #{creditLimit}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Customer customer);

    /**
     * 更新客户
     */
    @Update("UPDATE base_customer SET customer_code = #{customerCode}, customer_name = #{customerName}, " +
            "contact_person = #{contactPerson}, contact_phone = #{contactPhone}, email = #{email}, " +
            "address = #{address}, tax_number = #{taxNumber}, bank_name = #{bankName}, " +
            "bank_account = #{bankAccount}, credit_limit = #{creditLimit}, status = #{status}, remark = #{remark} " +
            "WHERE id = #{id}")
    int update(Customer customer);

    /**
     * 删除客户
     */
    @Delete("DELETE FROM base_customer WHERE id = #{id}")
    int delete(Long id);

    /**
     * 检查客户编号是否存在
     */
    @Select("SELECT COUNT(*) FROM base_customer WHERE customer_code = #{customerCode}")
    int checkCodeExists(String customerCode);
}


package com.fs.system.mapper;

import com.fs.system.entity.Voucher;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 凭证 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface VoucherMapper {

    /**
     * 查询所有凭证（在VoucherMapper.xml中定义）
     */
    List<Voucher> selectAll();

    /**
     * 根据ID查询凭证（在VoucherMapper.xml中定义）
     */
    Voucher selectById(Long id);

    /**
     * 根据来源查询凭证（在VoucherMapper.xml中定义）
     */
    Voucher selectBySource(@Param("sourceType") String sourceType, @Param("sourceId") Long sourceId);

    /**
     * 根据条件查询凭证（在VoucherMapper.xml中定义）
     */
    List<Voucher> selectByCondition(@Param("keyword") String keyword, @Param("status") String status,
                                      @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 插入凭证
     */
    @Insert("INSERT INTO acct_voucher (voucher_no, voucher_date, voucher_type, source_type, source_id, " +
            "total_debit, total_credit, status, abstract, remark, created_by) " +
            "VALUES (#{voucherNo}, #{voucherDate}, #{voucherType}, #{sourceType}, #{sourceId}, " +
            "#{totalDebit}, #{totalCredit}, #{status}, #{abstractText}, #{remark}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Voucher voucher);

    /**
     * 更新凭证（在VoucherMapper.xml中定义）
     */
    int update(Voucher voucher);

    /**
     * 标记为已记账
     */
    @Update("UPDATE acct_voucher SET status = 'Posted', posted_at = NOW() WHERE id = #{id}")
    int markAsPosted(Long id);

    /**
     * 删除凭证
     */
    @Delete("DELETE FROM acct_voucher WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 生成凭证编号
     */
    @Select("SELECT CONCAT('记-', LPAD(IFNULL(MAX(CAST(SUBSTRING(voucher_no, 3) AS UNSIGNED)), 0) + 1, 3, '0')) " +
            "FROM acct_voucher WHERE voucher_no LIKE '记-%'")
    String generateVoucherNo();
}


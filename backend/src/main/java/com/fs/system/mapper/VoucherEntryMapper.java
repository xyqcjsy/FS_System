package com.fs.system.mapper;

import com.fs.system.entity.VoucherEntry;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 凭证分录 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface VoucherEntryMapper {

    /**
     * 根据凭证ID查询分录列表（关联科目信息）
     */
    @Select("SELECT e.*, s.subject_code, s.subject_name " +
            "FROM acct_voucher_entry e " +
            "LEFT JOIN acct_subject s ON e.subject_id = s.id " +
            "WHERE e.voucher_id = #{voucherId} " +
            "ORDER BY e.line_number")
    @Results(id = "entryResultMap", value = {
            @Result(property = "abstractText", column = "abstract"),
            @Result(property = "subjectCode", column = "subject_code"),
            @Result(property = "subjectName", column = "subject_name")
    })
    List<VoucherEntry> selectByVoucherId(Long voucherId);

    /**
     * 插入分录
     */
    @Insert("INSERT INTO acct_voucher_entry (voucher_id, line_number, subject_id, debit_amount, " +
            "credit_amount, abstract, auxiliary_info) " +
            "VALUES (#{voucherId}, #{lineNumber}, #{subjectId}, #{debitAmount}, " +
            "#{creditAmount}, #{abstractText}, #{auxiliaryInfo})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VoucherEntry entry);

    /**
     * 批量插入分录
     */
    int batchInsert(List<VoucherEntry> entries);

    /**
     * 根据凭证ID删除所有分录
     */
    @Delete("DELETE FROM acct_voucher_entry WHERE voucher_id = #{voucherId}")
    int deleteByVoucherId(Long voucherId);
    
    /**
     * 计算科目余额（用于报表，考虑余额方向）
     * 借方余额科目（资产类）：余额 = 借方 - 贷方
     * 贷方余额科目（负债、权益）：余额 = 贷方 - 借方
     */
    @Select("<script>" +
            "SELECT COALESCE(SUM(" +
            "  CASE WHEN s.balance_direction = 'Debit' " +
            "  THEN e.debit_amount - e.credit_amount " +
            "  ELSE e.credit_amount - e.debit_amount END" +
            "), 0) " +
            "FROM acct_voucher_entry e " +
            "JOIN acct_voucher v ON e.voucher_id = v.id " +
            "JOIN acct_subject s ON e.subject_id = s.id " +
            "WHERE e.subject_id = #{subjectId} " +
            "AND v.status = 'Posted' " +
            "<if test='startDate != null'> AND v.voucher_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND v.voucher_date &lt;= #{endDate} </if>" +
            "</script>")
    BigDecimal calculateBalance(@Param("subjectId") Long subjectId, 
                               @Param("startDate") String startDate, 
                               @Param("endDate") String endDate);
    
    /**
     * 计算科目本期发生额（用于利润表）
     */
    @Select("<script>" +
            "SELECT COALESCE(SUM(e.debit_amount - e.credit_amount), 0) " +
            "FROM acct_voucher_entry e " +
            "JOIN acct_voucher v ON e.voucher_id = v.id " +
            "WHERE e.subject_id = #{subjectId} " +
            "AND v.status = 'Posted' " +
            "AND v.voucher_date &gt;= #{startDate} " +
            "AND v.voucher_date &lt;= #{endDate}" +
            "</script>")
    BigDecimal calculateOccurrence(@Param("subjectId") Long subjectId,
                                  @Param("startDate") String startDate,
                                  @Param("endDate") String endDate);
    
    /**
     * 检查科目是否被凭证分录使用
     */
    @Select("SELECT COUNT(*) FROM acct_voucher_entry WHERE subject_id = #{subjectId}")
    int countBySubjectId(Long subjectId);
    
    /**
     * 汇总科目的借贷发生额（截至指定日期）
     */
    @Select("<script>" +
            "SELECT " +
            "COALESCE(SUM(e.debit_amount), 0) as totalDebit, " +
            "COALESCE(SUM(e.credit_amount), 0) as totalCredit " +
            "FROM acct_voucher_entry e " +
            "JOIN acct_voucher v ON e.voucher_id = v.id " +
            "WHERE e.subject_id = #{subjectId} " +
            "AND v.status = 'Posted' " +
            "<if test='endDate != null'> AND v.voucher_date &lt;= #{endDate} </if>" +
            "</script>")
    Map<String, BigDecimal> sumAmountsBySubjectAndDate(@Param("subjectId") Long subjectId, 
                                                        @Param("endDate") LocalDate endDate);
}


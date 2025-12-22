package com.fs.system.mapper;

import com.fs.system.entity.CashFlowItem;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 现金流量Mapper
 */
@Mapper
public interface CashFlowMapper {
    
    /**
     * 查询所有现金流量项目
     */
    @Select("SELECT * FROM acct_cash_flow_item WHERE is_enabled = 1 ORDER BY display_order")
    List<CashFlowItem> selectAllItems();
    
    /**
     * 根据活动类别查询现金流量项目
     */
    @Select("SELECT * FROM acct_cash_flow_item WHERE category = #{category} AND is_enabled = 1 ORDER BY display_order")
    List<CashFlowItem> selectItemsByCategory(String category);
    
    /**
     * 统计某期间内各现金流量项目的金额
     * 基于货币资金科目（1001、1002）的实际流动
     */
    @Select("<script>" +
            "SELECT " +
            "  cfi.id AS itemId, " +
            "  cfi.item_code AS itemCode, " +
            "  cfi.item_name AS itemName, " +
            "  cfi.category AS category, " +
            "  cfi.direction AS direction, " +
            "  COALESCE(SUM(ecf.amount), 0) AS amount " +
            "FROM acct_cash_flow_item cfi " +
            "LEFT JOIN acct_entry_cash_flow ecf ON cfi.id = ecf.cash_flow_item_id " +
            "LEFT JOIN acct_voucher_entry e ON ecf.entry_id = e.id " +
            "LEFT JOIN acct_voucher v ON e.voucher_id = v.id " +
            "WHERE cfi.is_enabled = 1 " +
            "  AND v.status = 'Posted' " +
            "<if test='startDate != null'> AND v.voucher_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND v.voucher_date &lt;= #{endDate} </if>" +
            "GROUP BY cfi.id, cfi.item_code, cfi.item_name, cfi.category, cfi.direction, cfi.display_order " +
            "ORDER BY cfi.display_order" +
            "</script>")
    List<Map<String, Object>> calculateCashFlowByPeriod(@Param("startDate") String startDate, 
                                                         @Param("endDate") String endDate);
    
    /**
     * 按活动类别汇总现金流量
     */
    @Select("<script>" +
            "SELECT " +
            "  cfi.category AS category, " +
            "  cfi.direction AS direction, " +
            "  COALESCE(SUM(ecf.amount), 0) AS amount " +
            "FROM acct_cash_flow_item cfi " +
            "LEFT JOIN acct_entry_cash_flow ecf ON cfi.id = ecf.cash_flow_item_id " +
            "LEFT JOIN acct_voucher_entry e ON ecf.entry_id = e.id " +
            "LEFT JOIN acct_voucher v ON e.voucher_id = v.id " +
            "WHERE cfi.is_enabled = 1 " +
            "  AND v.status = 'Posted' " +
            "<if test='startDate != null'> AND v.voucher_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND v.voucher_date &lt;= #{endDate} </if>" +
            "GROUP BY cfi.category, cfi.direction " +
            "ORDER BY cfi.category" +
            "</script>")
    List<Map<String, Object>> summaryCashFlowByCategory(@Param("startDate") String startDate, 
                                                         @Param("endDate") String endDate);
    
    /**
     * 插入凭证分录的现金流量分类
     */
    @Insert("INSERT INTO acct_entry_cash_flow (entry_id, cash_flow_item_id, amount) " +
            "VALUES (#{entryId}, #{cashFlowItemId}, #{amount})")
    int insertEntryCashFlow(@Param("entryId") Long entryId, 
                           @Param("cashFlowItemId") Long cashFlowItemId, 
                           @Param("amount") BigDecimal amount);
    
    /**
     * 删除凭证分录的现金流量分类
     */
    @Delete("DELETE FROM acct_entry_cash_flow WHERE entry_id = #{entryId}")
    int deleteEntryCashFlow(Long entryId);
    
    /**
     * 查询货币资金相关的分录（用于辅助现金流量分类）
     */
    @Select("<script>" +
            "SELECT " +
            "  e.id AS entryId, " +
            "  v.id AS voucherId, " +
            "  v.voucher_no AS voucherNo, " +
            "  v.voucher_date AS voucherDate, " +
            "  v.abstract AS abstract, " +
            "  e.debit_amount AS debitAmount, " +
            "  e.credit_amount AS creditAmount, " +
            "  s.subject_code AS subjectCode, " +
            "  s.subject_name AS subjectName " +
            "FROM acct_voucher_entry e " +
            "JOIN acct_voucher v ON e.voucher_id = v.id " +
            "JOIN acct_subject s ON e.subject_id = s.id " +
            "WHERE s.subject_code IN ('1001', '1002') " +
            "  AND v.status = 'Posted' " +
            "  AND (e.debit_amount > 0 OR e.credit_amount > 0) " +
            "<if test='startDate != null'> AND v.voucher_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND v.voucher_date &lt;= #{endDate} </if>" +
            "ORDER BY v.voucher_date, v.id, e.line_number" +
            "</script>")
    List<Map<String, Object>> selectCashEntries(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate);
}


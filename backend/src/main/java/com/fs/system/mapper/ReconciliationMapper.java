package com.fs.system.mapper;

import com.fs.system.entity.Reconciliation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 对账记录Mapper
 */
@Mapper
public interface ReconciliationMapper {
    
    @Select("<script>" +
            "SELECT r.*, s.subject_name FROM acct_reconciliation r " +
            "LEFT JOIN acct_subject s ON r.subject_id = s.id " +
            "<where>" +
            "  <if test='period != null and period != \"\"'>" +
            "    AND DATE_FORMAT(r.reconciliation_date, '%Y-%m') = #{period}" +
            "  </if>" +
            "</where>" +
            "ORDER BY r.reconciliation_date DESC, r.id DESC" +
            "</script>")
    List<Reconciliation> selectByPeriod(@Param("period") String period);
    
    @Select("SELECT r.*, s.subject_name FROM acct_reconciliation r " +
            "LEFT JOIN acct_subject s ON r.subject_id = s.id " +
            "WHERE r.id = #{id}")
    Reconciliation selectById(Long id);
    
    @Insert("INSERT INTO acct_reconciliation (subject_id, reconciliation_date, book_balance, bank_balance, " +
            "difference_amount, status, remark, reconciled_by) " +
            "VALUES (#{subjectId}, #{reconciliationDate}, #{bookBalance}, #{bankBalance}, " +
            "#{differenceAmount}, #{status}, #{remark}, #{reconciledBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reconciliation reconciliation);
    
    @Update("UPDATE acct_reconciliation SET subject_id=#{subjectId}, reconciliation_date=#{reconciliationDate}, " +
            "book_balance=#{bookBalance}, bank_balance=#{bankBalance}, difference_amount=#{differenceAmount}, " +
            "status=#{status}, remark=#{remark}, reconciled_by=#{reconciledBy} WHERE id=#{id}")
    int update(Reconciliation reconciliation);
    
    @Delete("DELETE FROM acct_reconciliation WHERE id=#{id}")
    int delete(Long id);
}


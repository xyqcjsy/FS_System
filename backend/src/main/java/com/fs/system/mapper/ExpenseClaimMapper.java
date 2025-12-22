package com.fs.system.mapper;

import com.fs.system.entity.ExpenseClaim;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 费用报销Mapper
 */
@Mapper
public interface ExpenseClaimMapper {
    
    @Select("<script>" +
            "SELECT ec.id, ec.claim_no, ec.employee_id, ec.claim_date, ec.total_amount, " +
            "ec.status, ec.payment_date, ec.is_posted, ec.posted_at, ec.remark, " +
            "ec.created_at, ec.updated_at, e.employee_name " +
            "FROM biz_expense_claim ec " +
            "LEFT JOIN base_employee e ON ec.employee_id = e.id " +
            "<where>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (ec.claim_no LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR e.employee_name LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "  <if test='status != null and status != \"\"'>" +
            "    AND ec.status = #{status}" +
            "  </if>" +
            "</where>" +
            "ORDER BY ec.claim_date DESC, ec.id DESC" +
            "</script>")
    List<ExpenseClaim> selectByCondition(@Param("keyword") String keyword, @Param("status") String status);
    
    @Select("SELECT ec.id, ec.claim_no, ec.employee_id, ec.claim_date, ec.total_amount, " +
            "ec.status, ec.payment_date, ec.is_posted, ec.posted_at, ec.remark, " +
            "ec.created_at, ec.updated_at, e.employee_name " +
            "FROM biz_expense_claim ec " +
            "LEFT JOIN base_employee e ON ec.employee_id = e.id " +
            "WHERE ec.id = #{id}")
    ExpenseClaim selectById(Long id);
    
    @Insert("INSERT INTO biz_expense_claim (claim_no, employee_id, claim_date, total_amount, status, " +
            "payment_date, remark) " +
            "VALUES (#{claimNo}, #{employeeId}, #{claimDate}, #{totalAmount}, #{status}, " +
            "#{paymentDate}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ExpenseClaim expenseClaim);
    
    @Update("UPDATE biz_expense_claim SET employee_id=#{employeeId}, claim_date=#{claimDate}, " +
            "total_amount=#{totalAmount}, status=#{status}, payment_date=#{paymentDate}, " +
            "remark=#{remark} WHERE id=#{id}")
    int update(ExpenseClaim expenseClaim);
    
    @Update("UPDATE biz_expense_claim SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Update("UPDATE biz_expense_claim SET is_posted=1, posted_at=NOW() WHERE id=#{id}")
    int markAsPosted(Long id);
    
    @Delete("DELETE FROM biz_expense_claim WHERE id=#{id}")
    int delete(Long id);
}


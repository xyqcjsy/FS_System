package com.fs.system.mapper;

import com.fs.system.entity.ExpenseClaimItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 费用报销明细Mapper
 */
@Mapper
public interface ExpenseClaimItemMapper {
    
    @Select("SELECT * FROM biz_expense_claim_item WHERE claim_id = #{claimId}")
    List<ExpenseClaimItem> selectByClaimId(Long claimId);
    
    @Insert("INSERT INTO biz_expense_claim_item (claim_id, expense_type, expense_date, amount, " +
            "description, attachment_url, remark) " +
            "VALUES (#{claimId}, #{expenseType}, #{expenseDate}, #{amount}, #{description}, " +
            "#{attachmentUrl}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ExpenseClaimItem item);
    
    @Delete("DELETE FROM biz_expense_claim_item WHERE claim_id = #{claimId}")
    int deleteByClaimId(Long claimId);
}


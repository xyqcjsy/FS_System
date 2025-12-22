package com.fs.system.mapper;

import com.fs.system.entity.TaxRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 税务记录Mapper
 */
@Mapper
public interface TaxRecordMapper {
    
    @Select("<script>" +
            "SELECT * FROM acct_tax_record " +
            "<where>" +
            "  <if test='period != null and period != \"\"'>" +
            "    AND tax_period = #{period}" +
            "  </if>" +
            "</where>" +
            "ORDER BY tax_period DESC, id DESC" +
            "</script>")
    List<TaxRecord> selectByPeriod(@Param("period") String period);
    
    @Select("SELECT * FROM acct_tax_record WHERE id = #{id}")
    TaxRecord selectById(Long id);
    
    @Insert("INSERT INTO acct_tax_record (tax_period, tax_type, taxable_amount, tax_rate, tax_amount, " +
            "status, declare_date, payment_date, remark) " +
            "VALUES (#{taxPeriod}, #{taxType}, #{taxableAmount}, #{taxRate}, #{taxAmount}, " +
            "#{status}, #{declareDate}, #{paymentDate}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TaxRecord taxRecord);
    
    @Update("UPDATE acct_tax_record SET tax_period=#{taxPeriod}, tax_type=#{taxType}, " +
            "taxable_amount=#{taxableAmount}, tax_rate=#{taxRate}, tax_amount=#{taxAmount}, " +
            "status=#{status}, declare_date=#{declareDate}, payment_date=#{paymentDate}, " +
            "remark=#{remark} WHERE id=#{id}")
    int update(TaxRecord taxRecord);
    
    @Update("UPDATE acct_tax_record SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Delete("DELETE FROM acct_tax_record WHERE id=#{id}")
    int delete(Long id);
}


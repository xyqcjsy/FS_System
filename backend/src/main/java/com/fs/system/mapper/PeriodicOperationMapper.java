package com.fs.system.mapper;

import com.fs.system.entity.PeriodicOperation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 定期业务Mapper
 */
@Mapper
public interface PeriodicOperationMapper {
    
    @Select("<script>" +
            "SELECT * FROM acct_periodic_operation " +
            "<where>" +
            "  <if test='period != null and period != \"\"'>" +
            "    AND operation_period = #{period}" +
            "  </if>" +
            "  <if test='operationType != null and operationType != \"\"'>" +
            "    AND operation_type = #{operationType}" +
            "  </if>" +
            "  <if test='status != null and status != \"\"'>" +
            "    AND status = #{status}" +
            "  </if>" +
            "</where>" +
            "ORDER BY operation_period DESC, id DESC" +
            "</script>")
    List<PeriodicOperation> selectByCondition(@Param("period") String period,
                                              @Param("operationType") String operationType,
                                              @Param("status") String status);
    
    @Select("SELECT * FROM acct_periodic_operation WHERE id = #{id}")
    PeriodicOperation selectById(Long id);
    
    @Insert("INSERT INTO acct_periodic_operation (operation_type, operation_period, voucher_id, amount, " +
            "status, executed_at, description, remark) " +
            "VALUES (#{operationType}, #{operationPeriod}, #{voucherId}, #{amount}, " +
            "#{status}, #{executedAt}, #{description}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PeriodicOperation operation);
    
    @Update("UPDATE acct_periodic_operation SET operation_type=#{operationType}, " +
            "operation_period=#{operationPeriod}, voucher_id=#{voucherId}, amount=#{amount}, " +
            "status=#{status}, executed_at=#{executedAt}, description=#{description}, " +
            "remark=#{remark} WHERE id=#{id}")
    int update(PeriodicOperation operation);
    
    @Delete("DELETE FROM acct_periodic_operation WHERE id=#{id}")
    int delete(Long id);
}


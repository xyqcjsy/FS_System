package com.fs.system.mapper;

import com.fs.system.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购订单 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface PurchaseOrderMapper {

    /**
     * 查询所有采购订单
     */
    List<PurchaseOrder> selectAll();

    /**
     * 根据ID查询采购订单
     */
    PurchaseOrder selectById(Long id);

    /**
     * 查询未过账的采购订单
     */
    List<PurchaseOrder> selectUnposted();
    
    /**
     * 按条件查询采购订单
     */
    List<PurchaseOrder> selectByCondition(@Param("keyword") String keyword, @Param("status") String status);

    /**
     * 插入采购订单
     */
    int insert(PurchaseOrder order);

    /**
     * 更新采购订单
     */
    int update(PurchaseOrder order);
    
    /**
     * 更新订单状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 标记为已过账
     */
    int markAsPosted(Long id);

    /**
     * 删除采购订单
     */
    int deleteById(Long id);
}


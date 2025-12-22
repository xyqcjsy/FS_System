package com.fs.system.mapper;

import com.fs.system.entity.PurchaseOrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 采购订单明细 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface PurchaseOrderItemMapper {

    /**
     * 根据订单ID查询明细列表
     */
    @Select("SELECT * FROM biz_purchase_order_item WHERE order_id = #{orderId} ORDER BY id")
    List<PurchaseOrderItem> selectByOrderId(Long orderId);

    /**
     * 插入明细
     */
    @Insert("INSERT INTO biz_purchase_order_item (order_id, item_name, specification, unit, quantity, " +
            "unit_price, amount, tax_rate, remark) " +
            "VALUES (#{orderId}, #{itemName}, #{specification}, #{unit}, #{quantity}, " +
            "#{unitPrice}, #{amount}, #{taxRate}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PurchaseOrderItem item);

    /**
     * 批量插入明细
     */
    int batchInsert(List<PurchaseOrderItem> items);

    /**
     * 根据订单ID删除所有明细
     */
    @Delete("DELETE FROM biz_purchase_order_item WHERE order_id = #{orderId}")
    int deleteByOrderId(Long orderId);
}


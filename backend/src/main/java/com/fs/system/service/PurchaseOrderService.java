package com.fs.system.service;

import com.fs.system.entity.PurchaseOrder;
import com.fs.system.entity.PurchaseOrderItem;
import com.fs.system.exception.BusinessException;
import com.fs.system.mapper.PurchaseOrderItemMapper;
import com.fs.system.mapper.PurchaseOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购订单服务类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderMapper orderMapper;
    private final PurchaseOrderItemMapper itemMapper;

    /**
     * 查询所有采购订单
     */
    public List<PurchaseOrder> getAllOrders() {
        return orderMapper.selectAll();
    }

    /**
     * 根据ID查询采购订单（包含明细）
     */
    public PurchaseOrder getOrderById(Long id) {
        PurchaseOrder order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("采购订单不存在");
        }
        // 加载订单明细
        List<PurchaseOrderItem> items = itemMapper.selectByOrderId(id);
        order.setItems(items);
        return order;
    }

    /**
     * 查询订单明细
     */
    public List<PurchaseOrderItem> getOrderItems(Long orderId) {
        return itemMapper.selectByOrderId(orderId);
    }

    /**
     * 查询未过账的采购订单
     */
    public List<PurchaseOrder> getUnpostedOrders() {
        return orderMapper.selectUnposted();
    }

    /**
     * 新增采购订单
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(PurchaseOrder order, List<PurchaseOrderItem> items) {
        // 生成订单编号
        if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
            order.setOrderNo(generateOrderNo());
        }

        // 设置默认状态
        if (order.getStatus() == null) {
            order.setStatus("Draft");
        }

        // 设置默认过账状态
        if (order.getIsPosted() == null) {
            order.setIsPosted(false);
        }

        // 计算金额
        calculateOrderAmount(order, items);

        // 插入订单
        orderMapper.insert(order);

        // 插入明细
        if (items != null && !items.isEmpty()) {
            for (PurchaseOrderItem item : items) {
                item.setOrderId(order.getId());
            }
            itemMapper.batchInsert(items);
        }

        return order.getId();
    }

    /**
     * 更新采购订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(PurchaseOrder order, List<PurchaseOrderItem> items) {
        PurchaseOrder existing = orderMapper.selectById(order.getId());
        if (existing == null) {
            throw new BusinessException("采购订单不存在");
        }

        // 如果已过账，不允许修改
        if (existing.getIsPosted()) {
            throw new BusinessException("订单已过账，无法修改");
        }

        // 计算金额
        calculateOrderAmount(order, items);

        // 更新订单
        orderMapper.update(order);

        // 更新明细：先删除再插入
        itemMapper.deleteByOrderId(order.getId());
        if (items != null && !items.isEmpty()) {
            for (PurchaseOrderItem item : items) {
                item.setOrderId(order.getId());
            }
            itemMapper.batchInsert(items);
        }
    }

    /**
     * 删除采购订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Long id) {
        PurchaseOrder order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("采购订单不存在");
        }

        // 如果已过账，不允许删除
        if (order.getIsPosted()) {
            throw new BusinessException("订单已过账，无法删除");
        }

        // 删除明细
        itemMapper.deleteByOrderId(id);

        // 删除订单
        orderMapper.deleteById(id);
    }

    /**
     * 确认采购订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder(Long id) {
        PurchaseOrder order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("采购订单不存在");
        }

        if (!"Draft".equals(order.getStatus())) {
            throw new BusinessException("只有草稿状态的订单才能确认");
        }

        // 更新状态为已确认
        orderMapper.updateStatus(id, "Confirmed");
    }

    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orderMapper.selectAll().size() + 1;
        return "PO" + date + String.format("%04d", count);
    }

    /**
     * 计算订单金额
     */
    private void calculateOrderAmount(PurchaseOrder order, List<PurchaseOrderItem> items) {
        if (items == null || items.isEmpty()) {
            order.setTotalAmount(BigDecimal.ZERO);
            order.setFinalAmount(BigDecimal.ZERO);
            return;
        }

        // 计算总额
        BigDecimal totalAmount = items.stream()
                .map(PurchaseOrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        // 计算税额（如果设置了税率）
        if (order.getTaxAmount() == null) {
            order.setTaxAmount(BigDecimal.ZERO);
        }

        // 计算折扣（如果有）
        if (order.getDiscountAmount() == null) {
            order.setDiscountAmount(BigDecimal.ZERO);
        }

        // 最终金额 = 总额 + 税额 - 折扣
        BigDecimal finalAmount = totalAmount
                .add(order.getTaxAmount())
                .subtract(order.getDiscountAmount());

        order.setFinalAmount(finalAmount);
    }
}


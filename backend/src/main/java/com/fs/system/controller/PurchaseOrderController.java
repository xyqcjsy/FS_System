package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.entity.PurchaseOrder;
import com.fs.system.entity.PurchaseOrderItem;
import com.fs.system.service.PurchaseOrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购订单控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/purchase-order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService orderService;

    /**
     * 查询所有采购订单
     */
    @GetMapping("/list")
    public Result<List<PurchaseOrder>> list() {
        List<PurchaseOrder> list = orderService.getAllOrders();
        return Result.successData(list);
    }

    /**
     * 根据ID查询采购订单
     */
    @GetMapping("/{id}")
    public Result<PurchaseOrder> getById(@PathVariable Long id) {
        PurchaseOrder order = orderService.getOrderById(id);
        return Result.successData(order);
    }

    /**
     * 查询订单明细
     */
    @GetMapping("/{id}/items")
    public Result<List<PurchaseOrderItem>> getItems(@PathVariable Long id) {
        List<PurchaseOrderItem> items = orderService.getOrderItems(id);
        return Result.successData(items);
    }

    /**
     * 查询未过账的采购订单
     */
    @GetMapping("/unposted")
    public Result<List<PurchaseOrder>> getUnposted() {
        List<PurchaseOrder> list = orderService.getUnpostedOrders();
        return Result.successData(list);
    }

    /**
     * 新增采购订单
     */
    @PostMapping
    public Result<Long> create(@RequestBody PurchaseOrderRequest request) {
        Long id = orderService.createOrder(request.getOrder(), request.getItems());
        return Result.success("采购订单创建成功", id);
    }

    /**
     * 更新采购订单
     */
    @PutMapping
    public Result<Void> update(@RequestBody PurchaseOrderRequest request) {
        orderService.updateOrder(request.getOrder(), request.getItems());
        return Result.success("采购订单更新成功");
    }

    /**
     * 删除采购订单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success("采购订单删除成功");
    }

    /**
     * 确认采购订单
     */
    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        orderService.confirmOrder(id);
        return Result.success("采购订单确认成功");
    }

    /**
     * 采购订单请求对象
     */
    @Data
    public static class PurchaseOrderRequest {
        private PurchaseOrder order;
        private List<PurchaseOrderItem> items;
    }
}


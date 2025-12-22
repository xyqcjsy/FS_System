package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.service.PostingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 过账控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/posting")
@RequiredArgsConstructor
public class PostingController {

    private final PostingService postingService;

    /**
     * 采购订单过账
     */
    @PostMapping("/purchase/{orderId}")
    public Result<Long> postPurchaseOrder(@PathVariable Long orderId) {
        Long voucherId = postingService.postPurchaseOrder(orderId);
        return Result.success("采购订单过账成功", voucherId);
    }

    /**
     * 批量过账采购订单
     */
    @PostMapping("/purchase/batch")
    public Result<List<Long>> batchPostPurchaseOrders(@RequestBody BatchPostingRequest request) {
        List<Long> voucherIds = postingService.batchPostPurchaseOrders(request.getOrderIds());
        return Result.success("批量过账成功", voucherIds);
    }

    /**
     * 销售发票过账
     */
    @PostMapping("/sales/{invoiceId}")
    public Result<Long> postSalesInvoice(@PathVariable Long invoiceId) {
        Long voucherId = postingService.postSalesInvoice(invoiceId);
        return Result.success("销售发票过账成功", voucherId);
    }

    /**
     * 批量过账销售发票
     */
    @PostMapping("/sales/batch")
    public Result<List<Long>> batchPostSalesInvoices(@RequestBody BatchPostingRequest request) {
        List<Long> voucherIds = postingService.batchPostSalesInvoices(request.getInvoiceIds());
        return Result.success("批量过账成功", voucherIds);
    }

    /**
     * 费用报销过账
     */
    @PostMapping("/expense/{claimId}")
    public Result<Long> postExpenseClaim(@PathVariable Long claimId) {
        Long voucherId = postingService.postExpenseClaim(claimId);
        return Result.success("费用报销过账成功", voucherId);
    }

    /**
     * 批量过账费用报销
     */
    @PostMapping("/expense/batch")
    public Result<List<Long>> batchPostExpenseClaims(@RequestBody BatchPostingRequest request) {
        List<Long> voucherIds = postingService.batchPostExpenseClaims(request.getClaimIds());
        return Result.success("批量过账成功", voucherIds);
    }

    /**
     * 批量过账请求对象
     */
    @Data
    public static class BatchPostingRequest {
        private List<Long> orderIds;      // 采购订单ID列表
        private List<Long> invoiceIds;    // 销售发票ID列表
        private List<Long> claimIds;      // 费用报销ID列表
    }
}


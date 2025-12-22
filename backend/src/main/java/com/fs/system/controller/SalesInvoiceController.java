package com.fs.system.controller;

import com.fs.system.common.PageResult;
import com.fs.system.common.Result;
import com.fs.system.entity.SalesInvoice;
import com.fs.system.service.SalesInvoiceService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 销售发票Controller
 */
@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesInvoiceController {
    
    private final SalesInvoiceService salesInvoiceService;
    
    /**
     * 分页查询销售发票列表
     */
    @GetMapping("/list")
    public Result<PageResult<SalesInvoice>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        PageInfo<SalesInvoice> pageInfo = salesInvoiceService.listByPage(pageNum, pageSize, keyword, status);
        return Result.successData(PageResult.of(pageInfo));
    }
    
    /**
     * 根据ID查询销售发票
     */
    @GetMapping("/{id}")
    public Result<SalesInvoice> getById(@PathVariable Long id) {
        SalesInvoice invoice = salesInvoiceService.getById(id);
        return Result.successData(invoice);
    }
    
    /**
     * 新增销售发票
     */
    @PostMapping
    public Result<String> add(@RequestBody SalesInvoice invoice) {
        salesInvoiceService.add(invoice);
        return Result.success("新增成功");
    }
    
    /**
     * 更新销售发票
     */
    @PutMapping
    public Result<String> update(@RequestBody SalesInvoice invoice) {
        salesInvoiceService.update(invoice);
        return Result.success("更新成功");
    }
    
    /**
     * 删除销售发票
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        salesInvoiceService.delete(id);
        return Result.success("删除成功");
    }
    
    /**
     * 确认销售发票
     */
    @PutMapping("/{id}/confirm")
    public Result<String> confirm(@PathVariable Long id) {
        salesInvoiceService.confirm(id);
        return Result.success("确认成功");
    }
}


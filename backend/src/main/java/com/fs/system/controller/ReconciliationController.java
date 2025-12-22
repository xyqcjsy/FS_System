package com.fs.system.controller;

import com.fs.system.common.PageResult;
import com.fs.system.common.Result;
import com.fs.system.entity.Reconciliation;
import com.fs.system.service.ReconciliationService;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 对账Controller
 */
@RestController
@RequestMapping("/reconciliation")
@RequiredArgsConstructor
public class ReconciliationController {
    
    private final ReconciliationService reconciliationService;
    
    /**
     * 分页查询对账记录
     */
    @GetMapping("/list")
    public Result<PageResult<Reconciliation>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String period) {
        PageInfo<Reconciliation> pageInfo = reconciliationService.listByPage(pageNum, pageSize, period);
        return Result.successData(PageResult.of(pageInfo));
    }
    
    /**
     * 根据ID查询对账记录
     */
    @GetMapping("/{id}")
    public Result<Reconciliation> getById(@PathVariable Long id) {
        Reconciliation reconciliation = reconciliationService.getById(id);
        return Result.successData(reconciliation);
    }
    
    /**
     * 新增对账记录
     */
    @PostMapping
    public Result<String> add(@RequestBody Reconciliation reconciliation) {
        reconciliationService.add(reconciliation);
        return Result.success("新增成功");
    }
    
    /**
     * 更新对账记录
     */
    @PutMapping
    public Result<String> update(@RequestBody Reconciliation reconciliation) {
        reconciliationService.update(reconciliation);
        return Result.success("更新成功");
    }
    
    /**
     * 删除对账记录
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        reconciliationService.delete(id);
        return Result.success("删除成功");
    }
    
    /**
     * 自动对账
     */
    @PostMapping("/auto")
    public Result<Integer> autoReconcile(@RequestBody AutoReconcileRequest request) {
        int count = reconciliationService.autoReconcile(request.getReconciliationDate());
        return Result.success("自动对账成功", count);
    }
    
    /**
     * 获取科目余额（用于对账时显示账面余额）
     * 这个余额是基于所有已过账凭证自动计算的，关联了采购、销售、费用等所有业务
     */
    @GetMapping("/subject-balance/{subjectId}")
    public Result<java.math.BigDecimal> getSubjectBalance(
            @PathVariable Long subjectId,
            @RequestParam(required = false) String date) {
        LocalDate reconciliationDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        java.math.BigDecimal balance = reconciliationService.calculateSubjectBalance(subjectId, reconciliationDate);
        return Result.successData(balance);
    }
    
    /**
     * 自动对账请求对象
     */
    @Data
    public static class AutoReconcileRequest {
        private LocalDate reconciliationDate;
    }
}

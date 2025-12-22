package com.fs.system.controller;

import com.fs.system.common.PageResult;
import com.fs.system.common.Result;
import com.fs.system.entity.ExpenseClaim;
import com.fs.system.service.ExpenseClaimService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 费用报销Controller
 */
@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseClaimController {
    
    private final ExpenseClaimService expenseClaimService;
    
    /**
     * 分页查询费用报销列表
     */
    @GetMapping("/list")
    public Result<PageResult<ExpenseClaim>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        PageInfo<ExpenseClaim> pageInfo = expenseClaimService.listByPage(pageNum, pageSize, keyword, status);
        return Result.successData(PageResult.of(pageInfo));
    }
    
    /**
     * 根据ID查询费用报销
     */
    @GetMapping("/{id}")
    public Result<ExpenseClaim> getById(@PathVariable Long id) {
        ExpenseClaim claim = expenseClaimService.getById(id);
        return Result.successData(claim);
    }
    
    /**
     * 新增费用报销
     */
    @PostMapping
    public Result<String> add(@RequestBody ExpenseClaim claim) {
        expenseClaimService.add(claim);
        return Result.success("新增成功");
    }
    
    /**
     * 更新费用报销
     */
    @PutMapping
    public Result<String> update(@RequestBody ExpenseClaim claim) {
        expenseClaimService.update(claim);
        return Result.success("更新成功");
    }
    
    /**
     * 删除费用报销
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        expenseClaimService.delete(id);
        return Result.success("删除成功");
    }
    
    /**
     * 提交报销单
     */
    @PutMapping("/{id}/submit")
    public Result<String> submit(@PathVariable Long id) {
        expenseClaimService.submit(id);
        return Result.success("提交成功");
    }
    
    /**
     * 审批报销单
     */
    @PutMapping("/{id}/approve")
    public Result<String> approve(@PathVariable Long id) {
        expenseClaimService.approve(id);
        return Result.success("审批通过");
    }
    
    /**
     * 拒绝报销单
     */
    @PutMapping("/{id}/reject")
    public Result<String> reject(@PathVariable Long id, @RequestBody(required = false) java.util.Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        expenseClaimService.reject(id, reason);
        return Result.success("已拒绝");
    }
}


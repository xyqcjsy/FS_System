package com.fs.system.controller;

import com.fs.system.common.PageResult;
import com.fs.system.common.Result;
import com.fs.system.entity.PeriodicOperation;
import com.fs.system.service.PeriodicOperationService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 定期业务Controller
 */
@RestController
@RequestMapping("/periodic")
@RequiredArgsConstructor
public class PeriodicOperationController {
    
    private final PeriodicOperationService periodicOperationService;
    
    /**
     * 分页查询定期业务记录
     */
    @GetMapping("/list")
    public Result<PageResult<PeriodicOperation>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String status) {
        PageInfo<PeriodicOperation> pageInfo = periodicOperationService.listByPage(
            pageNum, pageSize, period, operationType, status);
        return Result.successData(PageResult.of(pageInfo));
    }
    
    /**
     * 根据ID查询定期业务
     */
    @GetMapping("/{id}")
    public Result<PeriodicOperation> getById(@PathVariable Long id) {
        PeriodicOperation operation = periodicOperationService.getById(id);
        return Result.successData(operation);
    }
    
    /**
     * 新增定期业务
     */
    @PostMapping
    public Result<String> add(@RequestBody PeriodicOperation operation) {
        periodicOperationService.add(operation);
        return Result.success("新增成功");
    }
    
    /**
     * 更新定期业务
     */
    @PutMapping
    public Result<String> update(@RequestBody PeriodicOperation operation) {
        periodicOperationService.update(operation);
        return Result.success("更新成功");
    }
    
    /**
     * 删除定期业务
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        periodicOperationService.delete(id);
        return Result.success("删除成功");
    }
    
    /**
     * 执行定期业务
     */
    @PutMapping("/{id}/execute")
    public Result<Long> execute(@PathVariable Long id) {
        Long voucherId = periodicOperationService.execute(id);
        return Result.success("执行成功，已生成凭证", voucherId);
    }
}


package com.fs.system.controller;

import com.fs.system.common.PageResult;
import com.fs.system.common.Result;
import com.fs.system.entity.TaxRecord;
import com.fs.system.service.TaxRecordService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 税务处理Controller
 */
@RestController
@RequestMapping("/tax")
@RequiredArgsConstructor
public class TaxRecordController {
    
    private final TaxRecordService taxRecordService;
    
    /**
     * 分页查询税务记录
     */
    @GetMapping("/list")
    public Result<PageResult<TaxRecord>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String period) {
        PageInfo<TaxRecord> pageInfo = taxRecordService.listByPage(pageNum, pageSize, period);
        return Result.successData(PageResult.of(pageInfo));
    }
    
    /**
     * 根据ID查询税务记录
     */
    @GetMapping("/{id}")
    public Result<TaxRecord> getById(@PathVariable Long id) {
        TaxRecord taxRecord = taxRecordService.getById(id);
        return Result.successData(taxRecord);
    }
    
    /**
     * 新增税务记录
     */
    @PostMapping
    public Result<String> add(@RequestBody TaxRecord taxRecord) {
        taxRecordService.add(taxRecord);
        return Result.success("新增成功");
    }
    
    /**
     * 更新税务记录
     */
    @PutMapping
    public Result<String> update(@RequestBody TaxRecord taxRecord) {
        taxRecordService.update(taxRecord);
        return Result.success("更新成功");
    }
    
    /**
     * 删除税务记录
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        taxRecordService.delete(id);
        return Result.success("删除成功");
    }
    
    /**
     * 申报税务
     */
    @PutMapping("/{id}/declare")
    public Result<String> declare(@PathVariable Long id) {
        taxRecordService.updateStatus(id, "Declared");
        return Result.success("申报成功");
    }
    
    /**
     * 标记已缴纳
     */
    @PutMapping("/{id}/pay")
    public Result<String> pay(@PathVariable Long id) {
        taxRecordService.updateStatus(id, "Paid");
        return Result.success("已标记为已缴纳");
    }
}


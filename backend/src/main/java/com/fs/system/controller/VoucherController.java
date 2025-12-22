package com.fs.system.controller;

import com.fs.system.common.PageResult;
import com.fs.system.common.Result;
import com.fs.system.entity.Voucher;
import com.fs.system.entity.VoucherEntry;
import com.fs.system.service.VoucherService;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 凭证控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    /**
     * 分页查询凭证
     */
    @GetMapping("/list")
    public Result<PageResult<Voucher>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        PageInfo<Voucher> pageInfo = voucherService.listByPage(pageNum, pageSize, keyword, status, startDate, endDate);
        return Result.successData(PageResult.of(pageInfo));
    }

    /**
     * 根据ID查询凭证
     */
    @GetMapping("/{id}")
    public Result<Voucher> getById(@PathVariable Long id) {
        Voucher voucher = voucherService.getVoucherById(id);
        return Result.successData(voucher);
    }

    /**
     * 查询凭证分录
     */
    @GetMapping("/{id}/entries")
    public Result<List<VoucherEntry>> getEntries(@PathVariable Long id) {
        List<VoucherEntry> entries = voucherService.getVoucherEntries(id);
        return Result.successData(entries);
    }

    /**
     * 新增凭证
     */
    @PostMapping
    public Result<Long> create(@RequestBody VoucherRequest request) {
        Long id = voucherService.createVoucher(request.getVoucher(), request.getEntries());
        return Result.success("凭证创建成功", id);
    }

    /**
     * 更新凭证
     */
    @PutMapping
    public Result<Void> update(@RequestBody VoucherRequest request) {
        voucherService.updateVoucher(request.getVoucher(), request.getEntries());
        return Result.success("凭证更新成功");
    }

    /**
     * 记账
     */
    @PostMapping("/{id}/post")
    public Result<Void> post(@PathVariable Long id) {
        voucherService.postVoucher(id);
        return Result.success("凭证记账成功");
    }

    /**
     * 删除凭证
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return Result.success("凭证删除成功");
    }

    /**
     * 凭证请求对象
     */
    @Data
    public static class VoucherRequest {
        private Voucher voucher;
        private List<VoucherEntry> entries;
    }
}


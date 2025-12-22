package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.entity.Supplier;
import com.fs.system.mapper.SupplierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierMapper supplierMapper;

    /**
     * 查询所有供应商
     */
    @GetMapping("/list")
    public Result<List<Supplier>> list() {
        List<Supplier> list = supplierMapper.selectAll();
        return Result.successData(list);
    }

    /**
     * 根据ID查询供应商
     */
    @GetMapping("/{id}")
    public Result<Supplier> getById(@PathVariable Long id) {
        Supplier supplier = supplierMapper.selectById(id);
        return Result.successData(supplier);
    }

    /**
     * 新增供应商
     */
    @PostMapping
    public Result<Void> create(@RequestBody Supplier supplier) {
        if (supplierMapper.checkCodeExists(supplier.getSupplierCode()) > 0) {
            return Result.error("供应商编号已存在");
        }
        supplierMapper.insert(supplier);
        return Result.success("供应商创建成功");
    }

    /**
     * 更新供应商
     */
    @PutMapping
    public Result<Void> update(@RequestBody Supplier supplier) {
        supplierMapper.update(supplier);
        return Result.success("供应商更新成功");
    }

    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        supplierMapper.delete(id);
        return Result.success("供应商删除成功");
    }
}


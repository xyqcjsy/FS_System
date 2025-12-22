package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.entity.Company;
import com.fs.system.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业信息控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyMapper companyMapper;

    /**
     * 查询所有企业信息
     */
    @GetMapping("/list")
    public Result<List<Company>> list() {
        List<Company> list = companyMapper.selectAll();
        return Result.successData(list);
    }

    /**
     * 根据ID查询企业信息
     */
    @GetMapping("/{id}")
    public Result<Company> getById(@PathVariable Long id) {
        Company company = companyMapper.selectById(id);
        return Result.successData(company);
    }

    /**
     * 新增企业信息
     */
    @PostMapping
    public Result<Void> create(@RequestBody Company company) {
        companyMapper.insert(company);
        return Result.success("企业信息创建成功");
    }

    /**
     * 更新企业信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody Company company) {
        companyMapper.update(company);
        return Result.success("企业信息更新成功");
    }

    /**
     * 删除企业信息
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        companyMapper.delete(id);
        return Result.success("企业信息删除成功");
    }
}


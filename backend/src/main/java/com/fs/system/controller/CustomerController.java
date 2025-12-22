package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.entity.Customer;
import com.fs.system.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerMapper customerMapper;

    /**
     * 查询所有客户
     */
    @GetMapping("/list")
    public Result<List<Customer>> list() {
        List<Customer> list = customerMapper.selectAll();
        return Result.successData(list);
    }

    /**
     * 根据ID查询客户
     */
    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        Customer customer = customerMapper.selectById(id);
        return Result.successData(customer);
    }

    /**
     * 新增客户
     */
    @PostMapping
    public Result<Void> create(@RequestBody Customer customer) {
        if (customerMapper.checkCodeExists(customer.getCustomerCode()) > 0) {
            return Result.error("客户编号已存在");
        }
        customerMapper.insert(customer);
        return Result.success("客户创建成功");
    }

    /**
     * 更新客户
     */
    @PutMapping
    public Result<Void> update(@RequestBody Customer customer) {
        customerMapper.update(customer);
        return Result.success("客户更新成功");
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerMapper.delete(id);
        return Result.success("客户删除成功");
    }
}


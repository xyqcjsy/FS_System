package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.entity.Employee;
import com.fs.system.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeMapper employeeMapper;

    /**
     * 查询所有员工
     */
    @GetMapping("/list")
    public Result<List<Employee>> list() {
        List<Employee> list = employeeMapper.selectAll();
        return Result.successData(list);
    }

    /**
     * 根据ID查询员工
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeMapper.selectById(id);
        return Result.successData(employee);
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result<Void> create(@RequestBody Employee employee) {
        if (employeeMapper.checkCodeExists(employee.getEmployeeCode()) > 0) {
            return Result.error("员工编号已存在");
        }
        employeeMapper.insert(employee);
        return Result.success("员工创建成功");
    }

    /**
     * 更新员工
     */
    @PutMapping
    public Result<Void> update(@RequestBody Employee employee) {
        employeeMapper.update(employee);
        return Result.success("员工更新成功");
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        employeeMapper.delete(id);
        return Result.success("员工删除成功");
    }
}


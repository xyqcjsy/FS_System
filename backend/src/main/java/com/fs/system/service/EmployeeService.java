package com.fs.system.service;

import com.fs.system.entity.Employee;
import com.fs.system.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 员工管理Service
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeMapper employeeMapper;
    
    /**
     * 分页查询员工列表
     */
    public PageInfo<Employee> listByPage(int pageNum, int pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectByKeyword(keyword);
        return new PageInfo<>(list);
    }
    
    /**
     * 查询所有员工（用于下拉选择）
     */
    public List<Employee> listAll() {
        return employeeMapper.selectAll();
    }
    
    /**
     * 根据ID查询员工
     */
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }
    
    /**
     * 根据员工编号查询
     */
    public Employee getByCode(String employeeCode) {
        return employeeMapper.selectByCode(employeeCode);
    }
    
    /**
     * 新增员工
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(Employee employee) {
        return employeeMapper.insert(employee);
    }
    
    /**
     * 更新员工
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Employee employee) {
        return employeeMapper.update(employee);
    }
    
    /**
     * 删除员工
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return employeeMapper.delete(id);
    }
}


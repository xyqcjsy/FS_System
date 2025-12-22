package com.fs.system.service;

import com.fs.system.entity.Customer;
import com.fs.system.mapper.CustomerMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户管理Service
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerMapper customerMapper;
    
    /**
     * 分页查询客户列表
     */
    public PageInfo<Customer> listByPage(int pageNum, int pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> list = customerMapper.selectByKeyword(keyword);
        return new PageInfo<>(list);
    }
    
    /**
     * 查询所有客户（用于下拉选择）
     */
    public List<Customer> listAll() {
        return customerMapper.selectAll();
    }
    
    /**
     * 根据ID查询客户
     */
    public Customer getById(Long id) {
        return customerMapper.selectById(id);
    }
    
    /**
     * 根据客户编号查询
     */
    public Customer getByCode(String customerCode) {
        return customerMapper.selectByCode(customerCode);
    }
    
    /**
     * 新增客户
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(Customer customer) {
        return customerMapper.insert(customer);
    }
    
    /**
     * 更新客户
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Customer customer) {
        return customerMapper.update(customer);
    }
    
    /**
     * 删除客户
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return customerMapper.delete(id);
    }
}


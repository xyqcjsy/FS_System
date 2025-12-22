package com.fs.system.service;

import com.fs.system.entity.Supplier;
import com.fs.system.mapper.SupplierMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 供应商管理Service
 */
@Service
@RequiredArgsConstructor
public class SupplierService {
    
    private final SupplierMapper supplierMapper;
    
    /**
     * 分页查询供应商列表
     */
    public PageInfo<Supplier> listByPage(int pageNum, int pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<Supplier> list = supplierMapper.selectByKeyword(keyword);
        return new PageInfo<>(list);
    }
    
    /**
     * 查询所有供应商（用于下拉选择）
     */
    public List<Supplier> listAll() {
        return supplierMapper.selectAll();
    }
    
    /**
     * 根据ID查询供应商
     */
    public Supplier getById(Long id) {
        return supplierMapper.selectById(id);
    }
    
    /**
     * 根据供应商编号查询
     */
    public Supplier getByCode(String supplierCode) {
        return supplierMapper.selectByCode(supplierCode);
    }
    
    /**
     * 新增供应商
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(Supplier supplier) {
        return supplierMapper.insert(supplier);
    }
    
    /**
     * 更新供应商
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Supplier supplier) {
        return supplierMapper.update(supplier);
    }
    
    /**
     * 删除供应商
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return supplierMapper.delete(id);
    }
}


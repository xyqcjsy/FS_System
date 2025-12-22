package com.fs.system.service;

import com.fs.system.entity.TaxRecord;
import com.fs.system.mapper.TaxRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 税务处理Service
 */
@Service
@RequiredArgsConstructor
public class TaxRecordService {
    
    private final TaxRecordMapper taxRecordMapper;
    
    /**
     * 分页查询税务记录
     */
    public PageInfo<TaxRecord> listByPage(int pageNum, int pageSize, String period) {
        PageHelper.startPage(pageNum, pageSize);
        List<TaxRecord> list = taxRecordMapper.selectByPeriod(period);
        return new PageInfo<>(list);
    }
    
    /**
     * 根据ID查询税务记录
     */
    public TaxRecord getById(Long id) {
        return taxRecordMapper.selectById(id);
    }
    
    /**
     * 新增税务记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(TaxRecord taxRecord) {
        // 计算税额
        taxRecord.setTaxAmount(
            taxRecord.getTaxableAmount()
                .multiply(taxRecord.getTaxRate())
                .divide(new java.math.BigDecimal("100"))
        );
        return taxRecordMapper.insert(taxRecord);
    }
    
    /**
     * 更新税务记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(TaxRecord taxRecord) {
        // 重新计算税额
        taxRecord.setTaxAmount(
            taxRecord.getTaxableAmount()
                .multiply(taxRecord.getTaxRate())
                .divide(new java.math.BigDecimal("100"))
        );
        return taxRecordMapper.update(taxRecord);
    }
    
    /**
     * 删除税务记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return taxRecordMapper.delete(id);
    }
    
    /**
     * 更新税务记录状态
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateStatus(Long id, String status) {
        return taxRecordMapper.updateStatus(id, status);
    }
}


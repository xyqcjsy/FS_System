package com.fs.system.service;

import com.fs.system.entity.SalesInvoice;
import com.fs.system.entity.SalesInvoiceItem;
import com.fs.system.mapper.SalesInvoiceMapper;
import com.fs.system.mapper.SalesInvoiceItemMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 销售发票Service
 */
@Service
@RequiredArgsConstructor
public class SalesInvoiceService {
    
    private final SalesInvoiceMapper salesInvoiceMapper;
    private final SalesInvoiceItemMapper salesInvoiceItemMapper;
    
    /**
     * 分页查询销售发票列表
     */
    public PageInfo<SalesInvoice> listByPage(int pageNum, int pageSize, String keyword, String status) {
        PageHelper.startPage(pageNum, pageSize);
        List<SalesInvoice> list = salesInvoiceMapper.selectByCondition(keyword, status);
        return new PageInfo<>(list);
    }
    
    /**
     * 根据ID查询销售发票（含明细）
     */
    public SalesInvoice getById(Long id) {
        SalesInvoice invoice = salesInvoiceMapper.selectById(id);
        if (invoice != null) {
            List<SalesInvoiceItem> items = salesInvoiceItemMapper.selectByInvoiceId(id);
            invoice.setItems(items);
        }
        return invoice;
    }
    
    /**
     * 新增销售发票
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(SalesInvoice invoice) {
        // 设置默认状态
        if (invoice.getStatus() == null || invoice.getStatus().isEmpty()) {
            invoice.setStatus("Draft");
        }
        
        // 计算总额
        calculateAmount(invoice);
        
        // 插入主表
        salesInvoiceMapper.insert(invoice);
        
        // 插入明细
        if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            for (SalesInvoiceItem item : invoice.getItems()) {
                item.setInvoiceId(invoice.getId());
                // 计算明细金额
                item.setAmount(item.getQuantity().multiply(item.getUnitPrice()));
                salesInvoiceItemMapper.insert(item);
            }
        }
        
        return 1;
    }
    
    /**
     * 更新销售发票
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(SalesInvoice invoice) {
        // 计算总额
        calculateAmount(invoice);
        
        // 更新主表
        salesInvoiceMapper.update(invoice);
        
        // 删除旧明细
        salesInvoiceItemMapper.deleteByInvoiceId(invoice.getId());
        
        // 插入新明细
        if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            for (SalesInvoiceItem item : invoice.getItems()) {
                item.setInvoiceId(invoice.getId());
                item.setAmount(item.getQuantity().multiply(item.getUnitPrice()));
                salesInvoiceItemMapper.insert(item);
            }
        }
        
        return 1;
    }
    
    /**
     * 删除销售发票
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        // 删除明细
        salesInvoiceItemMapper.deleteByInvoiceId(id);
        // 删除主表
        return salesInvoiceMapper.delete(id);
    }
    
    /**
     * 确认销售发票
     */
    @Transactional(rollbackFor = Exception.class)
    public int confirm(Long id) {
        return salesInvoiceMapper.updateStatus(id, "Confirmed");
    }
    
    /**
     * 计算金额
     */
    private void calculateAmount(SalesInvoice invoice) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            for (SalesInvoiceItem item : invoice.getItems()) {
                BigDecimal itemAmount = item.getQuantity().multiply(item.getUnitPrice());
                totalAmount = totalAmount.add(itemAmount);
            }
        }
        
        invoice.setTotalAmount(totalAmount);
        
        // 计算最终金额 = 总额 + 税额 - 折扣
        BigDecimal taxAmount = invoice.getTaxAmount() != null ? invoice.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discountAmount = invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : BigDecimal.ZERO;
        invoice.setFinalAmount(totalAmount.add(taxAmount).subtract(discountAmount));
    }
}


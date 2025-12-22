package com.fs.system.service;

import com.fs.system.entity.ExpenseClaim;
import com.fs.system.entity.ExpenseClaimItem;
import com.fs.system.mapper.ExpenseClaimMapper;
import com.fs.system.mapper.ExpenseClaimItemMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用报销Service
 */
@Service
@RequiredArgsConstructor
public class ExpenseClaimService {
    
    private final ExpenseClaimMapper expenseClaimMapper;
    private final ExpenseClaimItemMapper expenseClaimItemMapper;
    
    /**
     * 分页查询费用报销列表
     */
    public PageInfo<ExpenseClaim> listByPage(int pageNum, int pageSize, String keyword, String status) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExpenseClaim> list = expenseClaimMapper.selectByCondition(keyword, status);
        return new PageInfo<>(list);
    }
    
    /**
     * 根据ID查询费用报销（含明细）
     */
    public ExpenseClaim getById(Long id) {
        ExpenseClaim claim = expenseClaimMapper.selectById(id);
        if (claim != null) {
            List<ExpenseClaimItem> items = expenseClaimItemMapper.selectByClaimId(id);
            claim.setItems(items);
        }
        return claim;
    }
    
    /**
     * 新增费用报销
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(ExpenseClaim claim) {
        // 设置默认状态
        if (claim.getStatus() == null || claim.getStatus().isEmpty()) {
            claim.setStatus("Draft");
        }
        
        // 计算总额
        calculateAmount(claim);
        
        // 插入主表
        expenseClaimMapper.insert(claim);
        
        // 插入明细
        if (claim.getItems() != null && !claim.getItems().isEmpty()) {
            for (ExpenseClaimItem item : claim.getItems()) {
                item.setClaimId(claim.getId());
                expenseClaimItemMapper.insert(item);
            }
        }
        
        return 1;
    }
    
    /**
     * 更新费用报销
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(ExpenseClaim claim) {
        // 计算总额
        calculateAmount(claim);
        
        // 更新主表
        expenseClaimMapper.update(claim);
        
        // 删除旧明细
        expenseClaimItemMapper.deleteByClaimId(claim.getId());
        
        // 插入新明细
        if (claim.getItems() != null && !claim.getItems().isEmpty()) {
            for (ExpenseClaimItem item : claim.getItems()) {
                item.setClaimId(claim.getId());
                expenseClaimItemMapper.insert(item);
            }
        }
        
        return 1;
    }
    
    /**
     * 删除费用报销
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        // 删除明细
        expenseClaimItemMapper.deleteByClaimId(id);
        // 删除主表
        return expenseClaimMapper.delete(id);
    }
    
    /**
     * 提交报销单
     */
    @Transactional(rollbackFor = Exception.class)
    public int submit(Long id) {
        return expenseClaimMapper.updateStatus(id, "Submitted");
    }
    
    /**
     * 审批报销单
     */
    @Transactional(rollbackFor = Exception.class)
    public int approve(Long id) {
        return expenseClaimMapper.updateStatus(id, "Approved");
    }
    
    /**
     * 拒绝报销单
     */
    @Transactional(rollbackFor = Exception.class)
    public int reject(Long id, String reason) {
        return expenseClaimMapper.updateStatus(id, "Rejected");
    }
    
    /**
     * 计算总额
     */
    private void calculateAmount(ExpenseClaim claim) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        if (claim.getItems() != null && !claim.getItems().isEmpty()) {
            for (ExpenseClaimItem item : claim.getItems()) {
                totalAmount = totalAmount.add(item.getAmount());
            }
        }
        
        claim.setTotalAmount(totalAmount);
    }
}


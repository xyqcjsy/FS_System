package com.fs.system.service;

import com.fs.system.entity.AccountSubject;
import com.fs.system.entity.Reconciliation;
import com.fs.system.mapper.AccountSubjectMapper;
import com.fs.system.mapper.ReconciliationMapper;
import com.fs.system.mapper.VoucherEntryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 对账Service
 */
@Service
@RequiredArgsConstructor
public class ReconciliationService {
    
    private final ReconciliationMapper reconciliationMapper;
    private final AccountSubjectMapper accountSubjectMapper;
    private final VoucherEntryMapper voucherEntryMapper;
    
    /**
     * 分页查询对账记录
     */
    public PageInfo<Reconciliation> listByPage(int pageNum, int pageSize, String period) {
        PageHelper.startPage(pageNum, pageSize);
        List<Reconciliation> list = reconciliationMapper.selectByPeriod(period);
        return new PageInfo<>(list);
    }
    
    /**
     * 根据ID查询对账记录
     */
    public Reconciliation getById(Long id) {
        return reconciliationMapper.selectById(id);
    }
    
    /**
     * 新增对账记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(Reconciliation reconciliation) {
        // 计算差异金额
        BigDecimal bookBalance = reconciliation.getBookBalance();
        BigDecimal bankBalance = reconciliation.getBankBalance() != null ? 
                reconciliation.getBankBalance() : BigDecimal.ZERO;
        reconciliation.setDifferenceAmount(bookBalance.subtract(bankBalance));
        
        // 判断对账状态
        if (reconciliation.getDifferenceAmount().compareTo(BigDecimal.ZERO) == 0) {
            reconciliation.setStatus("Reconciled");
        } else {
            reconciliation.setStatus("Discrepancy");
        }
        
        return reconciliationMapper.insert(reconciliation);
    }
    
    /**
     * 更新对账记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Reconciliation reconciliation) {
        // 重新计算差异
        BigDecimal bookBalance = reconciliation.getBookBalance();
        BigDecimal bankBalance = reconciliation.getBankBalance() != null ? 
                reconciliation.getBankBalance() : BigDecimal.ZERO;
        reconciliation.setDifferenceAmount(bookBalance.subtract(bankBalance));
        
        if (reconciliation.getDifferenceAmount().compareTo(BigDecimal.ZERO) == 0) {
            reconciliation.setStatus("Reconciled");
        } else {
            reconciliation.setStatus("Discrepancy");
        }
        
        return reconciliationMapper.update(reconciliation);
    }
    
    /**
     * 删除对账记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return reconciliationMapper.delete(id);
    }
    
    /**
     * 自动对账 - 汇总货币资金科目余额
     */
    @Transactional(rollbackFor = Exception.class)
    public int autoReconcile(LocalDate reconciliationDate) {
        // 1. 获取货币资金科目（库存现金1001、银行存款1002、其他货币资金1012）
        List<String> moneySubjectCodes = List.of("1001", "1002", "1012");
        List<AccountSubject> subjects = accountSubjectMapper.selectByCodesIn(moneySubjectCodes);
        
        if (subjects == null || subjects.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        
        // 2. 对每个科目进行自动对账
        for (AccountSubject subject : subjects) {
            // 计算该科目截至对账日期的账面余额
            BigDecimal bookBalance = calculateSubjectBalance(subject.getId(), reconciliationDate);
            
            // 创建对账记录（银行余额需要手动输入，这里先设置为0）
            Reconciliation reconciliation = new Reconciliation();
            reconciliation.setSubjectId(subject.getId());
            reconciliation.setReconciliationDate(reconciliationDate);
            reconciliation.setBookBalance(bookBalance);
            reconciliation.setBankBalance(BigDecimal.ZERO); // 待手动输入
            reconciliation.setDifferenceAmount(bookBalance); // 暂时差异等于账面余额
            reconciliation.setStatus("Pending"); // 待对账状态
            reconciliation.setReconciledBy("System");
            reconciliation.setRemark("请手动输入银行余额");
            
            reconciliationMapper.insert(reconciliation);
            count++;
        }
        
        return count;
    }
    
    /**
     * 计算科目余额（截至指定日期）
     * 关联所有业务：采购、销售、费用报销等生成的凭证都会影响科目余额
     */
    public BigDecimal calculateSubjectBalance(Long subjectId, LocalDate endDate) {
        // 使用更简单的方法直接计算余额
        BigDecimal balance = voucherEntryMapper.calculateBalance(
            subjectId, 
            null,  // 从最开始计算
            endDate != null ? endDate.toString() : null
        );
        
        return balance != null ? balance : BigDecimal.ZERO;
    }
    
    /**
     * 获取科目当前余额（用于前端显示）
     */
    public BigDecimal getSubjectCurrentBalance(Long subjectId) {
        return calculateSubjectBalance(subjectId, LocalDate.now());
    }
}


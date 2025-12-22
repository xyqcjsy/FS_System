package com.fs.system.service;

import com.fs.system.entity.PeriodicOperation;
import com.fs.system.entity.Voucher;
import com.fs.system.entity.VoucherEntry;
import com.fs.system.mapper.PeriodicOperationMapper;
import com.fs.system.mapper.VoucherMapper;
import com.fs.system.mapper.VoucherEntryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 定期业务Service
 */
@Service
@RequiredArgsConstructor
public class PeriodicOperationService {
    
    private final PeriodicOperationMapper periodicOperationMapper;
    private final VoucherMapper voucherMapper;
    private final VoucherEntryMapper voucherEntryMapper;
    
    /**
     * 分页查询定期业务记录
     */
    public PageInfo<PeriodicOperation> listByPage(int pageNum, int pageSize, String period, 
                                                  String operationType, String status) {
        PageHelper.startPage(pageNum, pageSize);
        List<PeriodicOperation> list = periodicOperationMapper.selectByCondition(period, operationType, status);
        return new PageInfo<>(list);
    }
    
    /**
     * 根据ID查询定期业务
     */
    public PeriodicOperation getById(Long id) {
        return periodicOperationMapper.selectById(id);
    }
    
    /**
     * 新增定期业务
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(PeriodicOperation operation) {
        // 新增时默认设置为待执行状态
        if (operation.getStatus() == null || operation.getStatus().isEmpty()) {
            operation.setStatus("Pending");
        }
        return periodicOperationMapper.insert(operation);
    }
    
    /**
     * 更新定期业务
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(PeriodicOperation operation) {
        return periodicOperationMapper.update(operation);
    }
    
    /**
     * 删除定期业务
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return periodicOperationMapper.delete(id);
    }
    
    /**
     * 执行定期业务
     */
    @Transactional(rollbackFor = Exception.class)
    public Long execute(Long id) {
        PeriodicOperation operation = periodicOperationMapper.selectById(id);
        if (operation == null) {
            throw new RuntimeException("定期业务不存在");
        }
        
        if ("Executed".equals(operation.getStatus())) {
            throw new RuntimeException("该定期业务已执行，不能重复执行");
        }
        
        // 生成凭证
        Long voucherId = generateVoucherForPeriodicOperation(operation);
        
        // 更新状态为已执行，关联凭证ID
        operation.setStatus("Executed");
        operation.setExecutedAt(LocalDateTime.now());
        operation.setVoucherId(voucherId);
        periodicOperationMapper.update(operation);
        
        return voucherId;
    }
    
    /**
     * 为定期业务生成凭证
     */
    private Long generateVoucherForPeriodicOperation(PeriodicOperation operation) {
        // 创建凭证
        Voucher voucher = new Voucher();
        voucher.setVoucherNo(voucherMapper.generateVoucherNo());
        
        // 凭证日期为业务期间的最后一天
        String period = operation.getOperationPeriod(); // 格式：2025-01
        LocalDate voucherDate = LocalDate.parse(period + "-01").plusMonths(1).minusDays(1);
        voucher.setVoucherDate(voucherDate);
        
        voucher.setVoucherType("Auto");
        voucher.setSourceType("Manual");
        voucher.setAbstractText(operation.getDescription());
        voucher.setRemark("定期业务自动生成：" + operation.getOperationType());
        voucher.setCreatedBy("System");
        
        // 根据业务类型生成不同的凭证分录
        List<VoucherEntry> entries = new ArrayList<>();
        String operationType = operation.getOperationType();
        BigDecimal amount = operation.getAmount();
        
        if ("计提折旧".equals(operationType)) {
            // 借：管理费用-折旧费  贷：累计折旧
            VoucherEntry debitEntry = new VoucherEntry();
            debitEntry.setLineNumber(1);
            debitEntry.setSubjectId(40L); // 管理费用 6602
            debitEntry.setDebitAmount(amount);
            debitEntry.setCreditAmount(BigDecimal.ZERO);
            debitEntry.setAbstractText("计提本月固定资产折旧");
            entries.add(debitEntry);
            
            VoucherEntry creditEntry = new VoucherEntry();
            creditEntry.setLineNumber(2);
            creditEntry.setSubjectId(14L); // 累计折旧 1602
            creditEntry.setDebitAmount(BigDecimal.ZERO);
            creditEntry.setCreditAmount(amount);
            creditEntry.setAbstractText("计提本月固定资产折旧");
            entries.add(creditEntry);
            
        } else if ("费用摊销".equals(operationType)) {
            // 借：管理费用  贷：长期待摊费用
            VoucherEntry debitEntry = new VoucherEntry();
            debitEntry.setLineNumber(1);
            debitEntry.setSubjectId(40L); // 管理费用 6602
            debitEntry.setDebitAmount(amount);
            debitEntry.setCreditAmount(BigDecimal.ZERO);
            debitEntry.setAbstractText("本月费用摊销");
            entries.add(debitEntry);
            
            VoucherEntry creditEntry = new VoucherEntry();
            creditEntry.setLineNumber(2);
            creditEntry.setSubjectId(16L); // 长期待摊费用 1801
            creditEntry.setDebitAmount(BigDecimal.ZERO);
            creditEntry.setCreditAmount(amount);
            creditEntry.setAbstractText("本月费用摊销");
            entries.add(creditEntry);
            
        } else if ("工资计提".equals(operationType)) {
            // 借：管理费用-工资  贷：应付职工薪酬
            VoucherEntry debitEntry = new VoucherEntry();
            debitEntry.setLineNumber(1);
            debitEntry.setSubjectId(40L); // 管理费用 6602
            debitEntry.setDebitAmount(amount);
            debitEntry.setCreditAmount(BigDecimal.ZERO);
            debitEntry.setAbstractText("计提本月工资");
            entries.add(debitEntry);
            
            VoucherEntry creditEntry = new VoucherEntry();
            creditEntry.setLineNumber(2);
            creditEntry.setSubjectId(21L); // 应付职工薪酬 2211
            creditEntry.setDebitAmount(BigDecimal.ZERO);
            creditEntry.setCreditAmount(amount);
            creditEntry.setAbstractText("计提本月工资");
            entries.add(creditEntry);
            
        } else if ("利息计提".equals(operationType)) {
            // 借：财务费用  贷：应付利息
            VoucherEntry debitEntry = new VoucherEntry();
            debitEntry.setLineNumber(1);
            debitEntry.setSubjectId(41L); // 财务费用 6603
            debitEntry.setDebitAmount(amount);
            debitEntry.setCreditAmount(BigDecimal.ZERO);
            debitEntry.setAbstractText("计提本月借款利息");
            entries.add(debitEntry);
            
            VoucherEntry creditEntry = new VoucherEntry();
            creditEntry.setLineNumber(2);
            creditEntry.setSubjectId(23L); // 应付利息 2231
            creditEntry.setDebitAmount(BigDecimal.ZERO);
            creditEntry.setCreditAmount(amount);
            creditEntry.setAbstractText("计提本月借款利息");
            entries.add(creditEntry);
            
        } else if ("期末结转".equals(operationType)) {
            // 期末结转不生成具体分录，由系统自动计算
            // 这里简化处理，实际应该计算所有损益类科目余额
            VoucherEntry entry = new VoucherEntry();
            entry.setLineNumber(1);
            entry.setSubjectId(30L); // 本年利润 4103
            entry.setDebitAmount(BigDecimal.ZERO);
            entry.setCreditAmount(BigDecimal.ZERO);
            entry.setAbstractText("期末结转损益");
            entries.add(entry);
            
        } else {
            // 其他类型：借：管理费用  贷：银行存款
            VoucherEntry debitEntry = new VoucherEntry();
            debitEntry.setLineNumber(1);
            debitEntry.setSubjectId(40L); // 管理费用 6602
            debitEntry.setDebitAmount(amount);
            debitEntry.setCreditAmount(BigDecimal.ZERO);
            debitEntry.setAbstractText(operation.getDescription());
            entries.add(debitEntry);
            
            VoucherEntry creditEntry = new VoucherEntry();
            creditEntry.setLineNumber(2);
            creditEntry.setSubjectId(2L); // 银行存款 1002
            creditEntry.setDebitAmount(BigDecimal.ZERO);
            creditEntry.setCreditAmount(amount);
            creditEntry.setAbstractText(operation.getDescription());
            entries.add(creditEntry);
        }
        
        // 计算借贷合计
        BigDecimal totalDebit = entries.stream()
                .map(VoucherEntry::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = entries.stream()
                .map(VoucherEntry::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        voucher.setTotalDebit(totalDebit);
        voucher.setTotalCredit(totalCredit);
        voucher.setStatus("Draft"); // 草稿状态
        
        // 保存凭证及分录
        voucherMapper.insert(voucher);
        
        for (VoucherEntry entry : entries) {
            entry.setVoucherId(voucher.getId());
        }
        
        voucherEntryMapper.batchInsert(entries);
        
        return voucher.getId();
    }
}


package com.fs.system.service;

import com.fs.system.entity.Voucher;
import com.fs.system.entity.VoucherEntry;
import com.fs.system.exception.BusinessException;
import com.fs.system.mapper.VoucherEntryMapper;
import com.fs.system.mapper.VoucherMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 凭证服务类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherMapper voucherMapper;
    private final VoucherEntryMapper entryMapper;

    /**
     * 查询所有凭证
     */
    public List<Voucher> getAllVouchers() {
        return voucherMapper.selectAll();
    }

    /**
     * 分页查询凭证
     */
    public PageInfo<Voucher> listByPage(int pageNum, int pageSize, String keyword, String status, String startDate, String endDate) {
        PageHelper.startPage(pageNum, pageSize);
        List<Voucher> list = voucherMapper.selectByCondition(keyword, status, startDate, endDate);
        return new PageInfo<>(list);
    }

    /**
     * 根据ID查询凭证
     */
    public Voucher getVoucherById(Long id) {
        Voucher voucher = voucherMapper.selectById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }
        return voucher;
    }

    /**
     * 查询凭证分录
     */
    public List<VoucherEntry> getVoucherEntries(Long voucherId) {
        return entryMapper.selectByVoucherId(voucherId);
    }

    /**
     * 新增凭证
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createVoucher(Voucher voucher, List<VoucherEntry> entries) {
        // 生成凭证编号
        if (voucher.getVoucherNo() == null || voucher.getVoucherNo().isEmpty()) {
            voucher.setVoucherNo(voucherMapper.generateVoucherNo());
        }

        // 设置默认状态
        if (voucher.getStatus() == null) {
            voucher.setStatus("Draft");
        }

        // 设置默认凭证类型
        if (voucher.getVoucherType() == null) {
            voucher.setVoucherType("Manual");
        }

        // 设置默认来源类型
        if (voucher.getSourceType() == null) {
            voucher.setSourceType("Manual");
        }

        // 计算借贷总额
        calculateVoucherAmount(voucher, entries);

        // 验证借贷平衡
        if (voucher.getTotalDebit().compareTo(voucher.getTotalCredit()) != 0) {
            throw new BusinessException("借贷不平衡，无法保存凭证");
        }

        // 插入凭证
        voucherMapper.insert(voucher);

        // 插入分录
        if (entries != null && !entries.isEmpty()) {
            int lineNumber = 1;
            for (VoucherEntry entry : entries) {
                entry.setVoucherId(voucher.getId());
                entry.setLineNumber(lineNumber++);
            }
            entryMapper.batchInsert(entries);
        }

        return voucher.getId();
    }

    /**
     * 更新凭证
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateVoucher(Voucher voucher, List<VoucherEntry> entries) {
        Voucher existing = voucherMapper.selectById(voucher.getId());
        if (existing == null) {
            throw new BusinessException("凭证不存在");
        }

        // 如果已记账，不允许修改
        if ("Posted".equals(existing.getStatus())) {
            throw new BusinessException("凭证已记账，无法修改");
        }

        // 计算借贷总额
        calculateVoucherAmount(voucher, entries);

        // 验证借贷平衡
        if (voucher.getTotalDebit().compareTo(voucher.getTotalCredit()) != 0) {
            throw new BusinessException("借贷不平衡，无法保存凭证");
        }

        // 更新凭证
        voucherMapper.update(voucher);

        // 更新分录：先删除再插入
        entryMapper.deleteByVoucherId(voucher.getId());
        if (entries != null && !entries.isEmpty()) {
            int lineNumber = 1;
            for (VoucherEntry entry : entries) {
                entry.setVoucherId(voucher.getId());
                entry.setLineNumber(lineNumber++);
            }
            entryMapper.batchInsert(entries);
        }
    }

    /**
     * 记账
     */
    @Transactional(rollbackFor = Exception.class)
    public void postVoucher(Long id) {
        Voucher voucher = voucherMapper.selectById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        if ("Posted".equals(voucher.getStatus())) {
            throw new BusinessException("凭证已记账");
        }

        // 验证借贷平衡
        if (voucher.getTotalDebit().compareTo(voucher.getTotalCredit()) != 0) {
            throw new BusinessException("借贷不平衡，无法记账");
        }

        voucherMapper.markAsPosted(id);
    }

    /**
     * 删除凭证
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteVoucher(Long id) {
        Voucher voucher = voucherMapper.selectById(id);
        if (voucher == null) {
            throw new BusinessException("凭证不存在");
        }

        // 如果已记账，不允许删除
        if ("Posted".equals(voucher.getStatus())) {
            throw new BusinessException("凭证已记账，无法删除");
        }

        // 删除分录
        entryMapper.deleteByVoucherId(id);

        // 删除凭证
        voucherMapper.deleteById(id);
    }

    /**
     * 计算凭证借贷总额
     */
    private void calculateVoucherAmount(Voucher voucher, List<VoucherEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            voucher.setTotalDebit(BigDecimal.ZERO);
            voucher.setTotalCredit(BigDecimal.ZERO);
            return;
        }

        BigDecimal totalDebit = entries.stream()
                .map(VoucherEntry::getDebitAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = entries.stream()
                .map(VoucherEntry::getCreditAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        voucher.setTotalDebit(totalDebit);
        voucher.setTotalCredit(totalCredit);
    }
}


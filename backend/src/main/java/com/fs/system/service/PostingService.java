package com.fs.system.service;

import com.fs.system.entity.*;
import com.fs.system.exception.BusinessException;
import com.fs.system.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 过账服务类
 * 负责将业务单据自动生成会计凭证
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostingService {

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final SalesInvoiceMapper salesInvoiceMapper;
    private final ExpenseClaimMapper expenseClaimMapper;
    private final VoucherMapper voucherMapper;
    private final VoucherEntryMapper entryMapper;
    private final AccountSubjectMapper subjectMapper;

    /**
     * 采购订单过账
     * 会计分录：
     * 借：原材料/库存商品
     * 借：应交税费-应交增值税(进项税额)
     *     贷：应付账款
     */
    @Transactional(rollbackFor = Exception.class)
    public Long postPurchaseOrder(Long orderId) {
        // 查询采购订单
        PurchaseOrder order = purchaseOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("采购订单不存在");
        }

        // 检查是否已过账
        if (order.getIsPosted()) {
            throw new BusinessException("该订单已过账，请勿重复操作");
        }

        // 检查订单状态
        if (!"Confirmed".equals(order.getStatus()) && !"Received".equals(order.getStatus()) 
                && !"Paid".equals(order.getStatus())) {
            throw new BusinessException("只有已确认、已收货或已付款的订单才能过账");
        }

        // 创建凭证
        Voucher voucher = new Voucher();
        voucher.setVoucherNo(voucherMapper.generateVoucherNo());
        voucher.setVoucherDate(order.getOrderDate());
        voucher.setVoucherType("Auto");
        voucher.setSourceType("Purchase");
        voucher.setSourceId(order.getId());
        voucher.setStatus("Posted");
        voucher.setAbstractText("采购订单过账：" + order.getOrderNo());
        voucher.setCreatedBy("Admin");

        // 创建凭证分录
        List<VoucherEntry> entries = new ArrayList<>();

        // 1. 借方：原材料/库存商品（科目编码：1403 或 1405）
        AccountSubject materialSubject = getSubjectByCode("1403"); // 原材料
        if (materialSubject == null) {
            materialSubject = getSubjectByCode("1405"); // 库存商品
        }
        if (materialSubject == null) {
            throw new BusinessException("未找到原材料或库存商品科目，请先在会计科目中设置");
        }

        VoucherEntry debitMaterial = new VoucherEntry();
        debitMaterial.setLineNumber(1);
        debitMaterial.setSubjectId(materialSubject.getId());
        debitMaterial.setDebitAmount(order.getTotalAmount());
        debitMaterial.setCreditAmount(BigDecimal.ZERO);
        debitMaterial.setAbstractText("采购商品");
        entries.add(debitMaterial);

        // 2. 借方：应交税费（如果有税额）
        if (order.getTaxAmount() != null && order.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
            AccountSubject taxSubject = getSubjectByCode("2221"); // 应交税费
            if (taxSubject != null) {
                VoucherEntry debitTax = new VoucherEntry();
                debitTax.setLineNumber(2);
                debitTax.setSubjectId(taxSubject.getId());
                debitTax.setDebitAmount(order.getTaxAmount());
                debitTax.setCreditAmount(BigDecimal.ZERO);
                debitTax.setAbstractText("进项税额");
                entries.add(debitTax);
            }
        }

        // 3. 贷方：应付账款（科目编码：2202）
        AccountSubject payableSubject = getSubjectByCode("2202"); // 应付账款
        if (payableSubject == null) {
            throw new BusinessException("未找到应付账款科目，请先在会计科目中设置");
        }

        VoucherEntry creditPayable = new VoucherEntry();
        creditPayable.setLineNumber(entries.size() + 1);
        creditPayable.setSubjectId(payableSubject.getId());
        creditPayable.setDebitAmount(BigDecimal.ZERO);
        creditPayable.setCreditAmount(order.getFinalAmount());
        creditPayable.setAbstractText("应付" + order.getOrderNo());
        entries.add(creditPayable);

        // 计算借贷总额
        BigDecimal totalDebit = entries.stream()
                .map(VoucherEntry::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = entries.stream()
                .map(VoucherEntry::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        voucher.setTotalDebit(totalDebit);
        voucher.setTotalCredit(totalCredit);
        voucher.setPostedAt(LocalDateTime.now());

        // 插入凭证
        voucherMapper.insert(voucher);

        // 插入分录
        for (VoucherEntry entry : entries) {
            entry.setVoucherId(voucher.getId());
        }
        entryMapper.batchInsert(entries);

        // 标记订单为已过账
        purchaseOrderMapper.markAsPosted(orderId);

        log.info("采购订单过账成功：订单={}, 凭证={}", order.getOrderNo(), voucher.getVoucherNo());

        return voucher.getId();
    }

    /**
     * 根据科目编码查询科目
     */
    private AccountSubject getSubjectByCode(String code) {
        return subjectMapper.selectAll().stream()
                .filter(s -> code.equals(s.getSubjectCode()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 批量过账采购订单
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Long> batchPostPurchaseOrders(List<Long> orderIds) {
        List<Long> voucherIds = new ArrayList<>();
        for (Long orderId : orderIds) {
            try {
                Long voucherId = postPurchaseOrder(orderId);
                voucherIds.add(voucherId);
            } catch (Exception e) {
                log.error("采购订单过账失败：orderId={}, error={}", orderId, e.getMessage());
                throw new BusinessException("订单ID=" + orderId + " 过账失败：" + e.getMessage());
            }
        }
        return voucherIds;
    }

    /**
     * 销售订单过账
     * 会计分录：
     * 借：应收账款
     *     贷：主营业务收入
     *     贷：应交税费-应交增值税(销项税额)
     */
    @Transactional(rollbackFor = Exception.class)
    public Long postSalesInvoice(Long invoiceId) {
        // 查询销售发票
        SalesInvoice invoice = salesInvoiceMapper.selectById(invoiceId);
        if (invoice == null) {
            throw new BusinessException("销售发票不存在");
        }

        // 检查是否已过账
        if (invoice.getIsPosted()) {
            throw new BusinessException("该发票已过账，请勿重复操作");
        }

        // 检查发票状态
        if (!"Confirmed".equals(invoice.getStatus()) && !"Delivered".equals(invoice.getStatus()) 
                && !"Received".equals(invoice.getStatus())) {
            throw new BusinessException("只有已确认、已发货或已收款的发票才能过账");
        }

        // 创建凭证
        Voucher voucher = new Voucher();
        voucher.setVoucherNo(voucherMapper.generateVoucherNo());
        voucher.setVoucherDate(invoice.getInvoiceDate());
        voucher.setVoucherType("Auto");
        voucher.setSourceType("Sales");
        voucher.setSourceId(invoice.getId());
        voucher.setStatus("Posted");
        voucher.setAbstractText("销售发票过账：" + invoice.getInvoiceNo());
        voucher.setCreatedBy("Admin");

        // 创建凭证分录
        List<VoucherEntry> entries = new ArrayList<>();

        // 1. 借方：应收账款（科目编码：1122）
        AccountSubject receivableSubject = getSubjectByCode("1122"); // 应收账款
        if (receivableSubject == null) {
            throw new BusinessException("未找到应收账款科目，请先在会计科目中设置");
        }

        VoucherEntry debitReceivable = new VoucherEntry();
        debitReceivable.setLineNumber(1);
        debitReceivable.setSubjectId(receivableSubject.getId());
        debitReceivable.setDebitAmount(invoice.getFinalAmount());
        debitReceivable.setCreditAmount(BigDecimal.ZERO);
        debitReceivable.setAbstractText("销售商品");
        entries.add(debitReceivable);

        // 2. 贷方：主营业务收入（科目编码：6001）
        AccountSubject revenueSubject = getSubjectByCode("6001"); // 主营业务收入
        if (revenueSubject == null) {
            throw new BusinessException("未找到主营业务收入科目，请先在会计科目中设置");
        }

        VoucherEntry creditRevenue = new VoucherEntry();
        creditRevenue.setLineNumber(2);
        creditRevenue.setSubjectId(revenueSubject.getId());
        creditRevenue.setDebitAmount(BigDecimal.ZERO);
        creditRevenue.setCreditAmount(invoice.getTotalAmount());
        creditRevenue.setAbstractText("销售收入");
        entries.add(creditRevenue);

        // 3. 贷方：应交税费（如果有税额）
        if (invoice.getTaxAmount() != null && invoice.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
            AccountSubject taxSubject = getSubjectByCode("2221"); // 应交税费
            if (taxSubject != null) {
                VoucherEntry creditTax = new VoucherEntry();
                creditTax.setLineNumber(3);
                creditTax.setSubjectId(taxSubject.getId());
                creditTax.setDebitAmount(BigDecimal.ZERO);
                creditTax.setCreditAmount(invoice.getTaxAmount());
                creditTax.setAbstractText("销项税额");
                entries.add(creditTax);
            }
        }

        // 计算借贷总额
        BigDecimal totalDebit = entries.stream()
                .map(VoucherEntry::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = entries.stream()
                .map(VoucherEntry::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        voucher.setTotalDebit(totalDebit);
        voucher.setTotalCredit(totalCredit);
        voucher.setPostedAt(LocalDateTime.now());

        // 插入凭证
        voucherMapper.insert(voucher);

        // 插入分录
        for (VoucherEntry entry : entries) {
            entry.setVoucherId(voucher.getId());
        }
        entryMapper.batchInsert(entries);

        // 标记发票为已过账
        salesInvoiceMapper.markAsPosted(invoiceId);

        log.info("销售发票过账成功：发票={}, 凭证={}", invoice.getInvoiceNo(), voucher.getVoucherNo());

        return voucher.getId();
    }

    /**
     * 费用报销过账
     * 会计分录：
     * 借：管理费用/销售费用等（根据费用类型）
     *     贷：其他应付款/库存现金
     */
    @Transactional(rollbackFor = Exception.class)
    public Long postExpenseClaim(Long claimId) {
        // 查询费用报销
        ExpenseClaim claim = expenseClaimMapper.selectById(claimId);
        if (claim == null) {
            throw new BusinessException("费用报销不存在");
        }

        // 检查是否已过账
        if (claim.getIsPosted()) {
            throw new BusinessException("该报销单已过账，请勿重复操作");
        }

        // 检查报销状态
        if (!"Approved".equals(claim.getStatus()) && !"Paid".equals(claim.getStatus())) {
            throw new BusinessException("只有已审批或已付款的报销单才能过账");
        }

        // 创建凭证
        Voucher voucher = new Voucher();
        voucher.setVoucherNo(voucherMapper.generateVoucherNo());
        voucher.setVoucherDate(claim.getClaimDate());
        voucher.setVoucherType("Auto");
        voucher.setSourceType("Expense");
        voucher.setSourceId(claim.getId());
        voucher.setStatus("Posted");
        voucher.setAbstractText("费用报销过账：" + claim.getClaimNo());
        voucher.setCreatedBy("Admin");

        // 创建凭证分录
        List<VoucherEntry> entries = new ArrayList<>();

        // 1. 借方：管理费用（科目编码：6601）
        // 注：实际应用中可根据费用类型选择不同科目（管理费用/销售费用等）
        AccountSubject expenseSubject = getSubjectByCode("6601"); // 管理费用
        if (expenseSubject == null) {
            expenseSubject = getSubjectByCode("6602"); // 销售费用
        }
        if (expenseSubject == null) {
            throw new BusinessException("未找到费用科目，请先在会计科目中设置");
        }

        VoucherEntry debitExpense = new VoucherEntry();
        debitExpense.setLineNumber(1);
        debitExpense.setSubjectId(expenseSubject.getId());
        debitExpense.setDebitAmount(claim.getTotalAmount());
        debitExpense.setCreditAmount(BigDecimal.ZERO);
        debitExpense.setAbstractText("费用报销");
        entries.add(debitExpense);

        // 2. 贷方：其他应付款（科目编码：2241）或库存现金（科目编码：1001）
        AccountSubject payableSubject = getSubjectByCode("2241"); // 其他应付款
        if (payableSubject == null) {
            payableSubject = getSubjectByCode("1001"); // 库存现金
        }
        if (payableSubject == null) {
            throw new BusinessException("未找到其他应付款或库存现金科目，请先在会计科目中设置");
        }

        VoucherEntry creditPayable = new VoucherEntry();
        creditPayable.setLineNumber(2);
        creditPayable.setSubjectId(payableSubject.getId());
        creditPayable.setDebitAmount(BigDecimal.ZERO);
        creditPayable.setCreditAmount(claim.getTotalAmount());
        creditPayable.setAbstractText("应付报销款");
        entries.add(creditPayable);

        // 计算借贷总额
        BigDecimal totalDebit = entries.stream()
                .map(VoucherEntry::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = entries.stream()
                .map(VoucherEntry::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        voucher.setTotalDebit(totalDebit);
        voucher.setTotalCredit(totalCredit);
        voucher.setPostedAt(LocalDateTime.now());

        // 插入凭证
        voucherMapper.insert(voucher);

        // 插入分录
        for (VoucherEntry entry : entries) {
            entry.setVoucherId(voucher.getId());
        }
        entryMapper.batchInsert(entries);

        // 标记报销单为已过账
        expenseClaimMapper.markAsPosted(claimId);

        log.info("费用报销过账成功：报销单={}, 凭证={}", claim.getClaimNo(), voucher.getVoucherNo());

        return voucher.getId();
    }

    /**
     * 批量过账销售发票
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Long> batchPostSalesInvoices(List<Long> invoiceIds) {
        List<Long> voucherIds = new ArrayList<>();
        for (Long invoiceId : invoiceIds) {
            try {
                Long voucherId = postSalesInvoice(invoiceId);
                voucherIds.add(voucherId);
            } catch (Exception e) {
                log.error("销售发票过账失败：invoiceId={}, error={}", invoiceId, e.getMessage());
                throw new BusinessException("发票ID=" + invoiceId + " 过账失败：" + e.getMessage());
            }
        }
        return voucherIds;
    }

    /**
     * 批量过账费用报销
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Long> batchPostExpenseClaims(List<Long> claimIds) {
        List<Long> voucherIds = new ArrayList<>();
        for (Long claimId : claimIds) {
            try {
                Long voucherId = postExpenseClaim(claimId);
                voucherIds.add(voucherId);
            } catch (Exception e) {
                log.error("费用报销过账失败：claimId={}, error={}", claimId, e.getMessage());
                throw new BusinessException("报销单ID=" + claimId + " 过账失败：" + e.getMessage());
            }
        }
        return voucherIds;
    }
}


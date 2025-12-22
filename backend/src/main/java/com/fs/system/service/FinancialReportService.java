package com.fs.system.service;

import com.fs.system.entity.AccountSubject;
import com.fs.system.mapper.AccountSubjectMapper;
import com.fs.system.mapper.VoucherEntryMapper;
import com.fs.system.mapper.CashFlowMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 财务报表Service
 */
@Service
@RequiredArgsConstructor
public class FinancialReportService {
    
    private final AccountSubjectMapper accountSubjectMapper;
    private final VoucherEntryMapper voucherEntryMapper;
    private final CashFlowMapper cashFlowMapper;
    
    /**
     * 生成资产负债表
     */
    public Map<String, Object> generateBalanceSheet(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有科目
        List<AccountSubject> subjects = accountSubjectMapper.selectAll();
        
        // 计算各科目余额
        Map<String, BigDecimal> balances = new HashMap<>();
        for (AccountSubject subject : subjects) {
            BigDecimal balance = voucherEntryMapper.calculateBalance(
                subject.getId(), startDate, endDate
            );
            balances.put(subject.getSubjectCode(), balance != null ? balance : BigDecimal.ZERO);
        }
        
        // 资产类
        Map<String, BigDecimal> currentAssets = calculateAssetItems(subjects, balances, "流动");
        Map<String, BigDecimal> nonCurrentAssets = calculateAssetItems(subjects, balances, "非流动");
        
        // 计算资产总计（已在calculateTypeTotal中正确处理累计折旧）
        BigDecimal totalAssets = calculateTypeTotal(subjects, balances, "Asset");
        
        Map<String, Object> assets = new HashMap<>();
        
        // 移除合计项后的流动资产明细
        Map<String, BigDecimal> currentAssetsDetail = new LinkedHashMap<>(currentAssets);
        BigDecimal currentAssetTotal = currentAssetsDetail.remove("流动资产合计");
        
        // 移除合计项后的非流动资产明细
        Map<String, BigDecimal> nonCurrentAssetsDetail = new LinkedHashMap<>(nonCurrentAssets);
        BigDecimal nonCurrentAssetTotal = nonCurrentAssetsDetail.remove("非流动资产合计");
        
        assets.put("流动资产", currentAssetsDetail);
        assets.put("流动资产合计", currentAssetTotal != null ? currentAssetTotal : BigDecimal.ZERO);
        assets.put("非流动资产", nonCurrentAssetsDetail);
        assets.put("非流动资产合计", nonCurrentAssetTotal != null ? nonCurrentAssetTotal : BigDecimal.ZERO);
        assets.put("资产总计", totalAssets);
        
        // 负债类
        Map<String, BigDecimal> currentLiabilities = calculateLiabilityItems(subjects, balances, "流动");
        Map<String, BigDecimal> nonCurrentLiabilities = calculateLiabilityItems(subjects, balances, "非流动");
        
        BigDecimal totalLiabilities = calculateTypeTotal(subjects, balances, "Liability");
        
        Map<String, Object> liabilities = new HashMap<>();
        
        // 移除合计项后的流动负债明细
        Map<String, BigDecimal> currentLiabilitiesDetail = new LinkedHashMap<>(currentLiabilities);
        BigDecimal currentLiabilityTotal = currentLiabilitiesDetail.remove("流动负债合计");
        
        // 移除合计项后的非流动负债明细
        Map<String, BigDecimal> nonCurrentLiabilitiesDetail = new LinkedHashMap<>(nonCurrentLiabilities);
        BigDecimal nonCurrentLiabilityTotal = nonCurrentLiabilitiesDetail.remove("非流动负债合计");
        
        liabilities.put("流动负债", currentLiabilitiesDetail);
        liabilities.put("流动负债合计", currentLiabilityTotal != null ? currentLiabilityTotal : BigDecimal.ZERO);
        liabilities.put("非流动负债", nonCurrentLiabilitiesDetail);
        liabilities.put("非流动负债合计", nonCurrentLiabilityTotal != null ? nonCurrentLiabilityTotal : BigDecimal.ZERO);
        liabilities.put("负债合计", totalLiabilities);
        
        // 所有者权益
        // 计算本年利润：如果损益科目未结转，则自动计算收入-费用
        BigDecimal incomeTotal = calculateTypeTotal(subjects, balances, "Income");
        BigDecimal expenseTotal = calculateTypeTotal(subjects, balances, "Expense");
        BigDecimal currentYearProfit = balances.getOrDefault("4103", BigDecimal.ZERO);
        
        // 如果本年利润科目为0，但存在未结转的收入或费用，则自动计算
        if (currentYearProfit.compareTo(BigDecimal.ZERO) == 0 && 
            (incomeTotal.compareTo(BigDecimal.ZERO) != 0 || expenseTotal.compareTo(BigDecimal.ZERO) != 0)) {
            currentYearProfit = incomeTotal.subtract(expenseTotal);
        }
        
        BigDecimal totalEquity = calculateTypeTotal(subjects, balances, "Equity")
                .add(incomeTotal).subtract(expenseTotal);
        
        Map<String, Object> equity = new HashMap<>();
        equity.put("实收资本", balances.getOrDefault("4001", BigDecimal.ZERO));
        equity.put("资本公积", balances.getOrDefault("4002", BigDecimal.ZERO));
        equity.put("盈余公积", balances.getOrDefault("4101", BigDecimal.ZERO));
        equity.put("本年利润", currentYearProfit);
        equity.put("未分配利润", balances.getOrDefault("4104", BigDecimal.ZERO));
        equity.put("所有者权益合计", totalEquity);
        
        result.put("资产", assets);
        result.put("负债", liabilities);
        result.put("所有者权益", equity);
        result.put("负债及所有者权益总计", totalLiabilities.add(totalEquity));
        result.put("报表日期", endDate);
        result.put("平衡校验", totalAssets.compareTo(totalLiabilities.add(totalEquity)) == 0);
        
        return result;
    }
    
    /**
     * 生成利润表
     */
    public Map<String, Object> generateIncomeStatement(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有科目
        List<AccountSubject> subjects = accountSubjectMapper.selectAll();
        
        // 计算各科目本期发生额
        Map<String, BigDecimal> amounts = new HashMap<>();
        for (AccountSubject subject : subjects) {
            BigDecimal amount = voucherEntryMapper.calculateOccurrence(
                subject.getId(), startDate, endDate
            );
            amounts.put(subject.getSubjectCode(), amount != null ? amount : BigDecimal.ZERO);
        }
        
        // 营业收入
        BigDecimal revenue = amounts.getOrDefault("6001", BigDecimal.ZERO)
                .add(amounts.getOrDefault("6051", BigDecimal.ZERO));
        
        // 营业成本
        BigDecimal cost = amounts.getOrDefault("6401", BigDecimal.ZERO)
                .add(amounts.getOrDefault("6402", BigDecimal.ZERO));
        
        // 期间费用
        BigDecimal salesExpense = amounts.getOrDefault("6601", BigDecimal.ZERO);
        BigDecimal adminExpense = amounts.getOrDefault("6602", BigDecimal.ZERO);
        BigDecimal financeExpense = amounts.getOrDefault("6603", BigDecimal.ZERO);
        BigDecimal totalExpense = salesExpense.add(adminExpense).add(financeExpense);
        
        // 营业利润
        BigDecimal operatingProfit = revenue.subtract(cost).subtract(totalExpense);
        
        // 营业外收支
        BigDecimal nonOperatingIncome = amounts.getOrDefault("6301", BigDecimal.ZERO);
        BigDecimal nonOperatingExpense = amounts.getOrDefault("6701", BigDecimal.ZERO);
        
        // 利润总额
        BigDecimal totalProfit = operatingProfit.add(nonOperatingIncome).subtract(nonOperatingExpense);
        
        // 所得税费用
        BigDecimal incomeTax = amounts.getOrDefault("6711", BigDecimal.ZERO);
        
        // 净利润
        BigDecimal netProfit = totalProfit.subtract(incomeTax);
        
        result.put("营业收入", revenue);
        result.put("营业成本", cost);
        result.put("销售费用", salesExpense);
        result.put("管理费用", adminExpense);
        result.put("财务费用", financeExpense);
        result.put("营业利润", operatingProfit);
        result.put("营业外收入", nonOperatingIncome);
        result.put("营业外支出", nonOperatingExpense);
        result.put("利润总额", totalProfit);
        result.put("所得税费用", incomeTax);
        result.put("净利润", netProfit);
        result.put("报表期间", startDate + " 至 " + endDate);
        
        return result;
    }
    
    /**
     * 生成现金流量表（基于现金流量项目配置）
     */
    public Map<String, Object> generateCashFlowStatement(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 计算期初和期末现金余额
        AccountSubject cashSubject = accountSubjectMapper.selectByCode("1001");
        AccountSubject bankSubject = accountSubjectMapper.selectByCode("1002");
        
        BigDecimal cashBegin = BigDecimal.ZERO;
        BigDecimal cashEnd = BigDecimal.ZERO;
        
        if (cashSubject != null) {
            BigDecimal cashBalance = voucherEntryMapper.calculateBalance(cashSubject.getId(), null, startDate);
            cashBegin = cashBegin.add(cashBalance != null ? cashBalance : BigDecimal.ZERO);
            
            BigDecimal cashEndBalance = voucherEntryMapper.calculateBalance(cashSubject.getId(), null, endDate);
            cashEnd = cashEnd.add(cashEndBalance != null ? cashEndBalance : BigDecimal.ZERO);
        }
        if (bankSubject != null) {
            BigDecimal bankBalance = voucherEntryMapper.calculateBalance(bankSubject.getId(), null, startDate);
            cashBegin = cashBegin.add(bankBalance != null ? bankBalance : BigDecimal.ZERO);
            
            BigDecimal bankEndBalance = voucherEntryMapper.calculateBalance(bankSubject.getId(), null, endDate);
            cashEnd = cashEnd.add(bankEndBalance != null ? bankEndBalance : BigDecimal.ZERO);
        }
        
        // 查询现金流量明细数据
        List<Map<String, Object>> cashFlowDetails = cashFlowMapper.calculateCashFlowByPeriod(startDate, endDate);
        
        // 按活动类别分组
        Map<String, BigDecimal> operating = new LinkedHashMap<>();
        Map<String, BigDecimal> investing = new LinkedHashMap<>();
        Map<String, BigDecimal> financing = new LinkedHashMap<>();
        
        BigDecimal operatingInflow = BigDecimal.ZERO;
        BigDecimal operatingOutflow = BigDecimal.ZERO;
        BigDecimal investingInflow = BigDecimal.ZERO;
        BigDecimal investingOutflow = BigDecimal.ZERO;
        BigDecimal financingInflow = BigDecimal.ZERO;
        BigDecimal financingOutflow = BigDecimal.ZERO;
        
        for (Map<String, Object> item : cashFlowDetails) {
            String category = (String) item.get("category");
            String direction = (String) item.get("direction");
            String itemName = (String) item.get("itemName");
            BigDecimal amount = (BigDecimal) item.get("amount");
            
            if (amount == null) amount = BigDecimal.ZERO;
            
            if ("经营活动".equals(category)) {
                operating.put(itemName, amount);
                if ("流入".equals(direction)) {
                    operatingInflow = operatingInflow.add(amount);
                } else {
                    operatingOutflow = operatingOutflow.add(amount);
                }
            } else if ("投资活动".equals(category)) {
                investing.put(itemName, amount);
                if ("流入".equals(direction)) {
                    investingInflow = investingInflow.add(amount);
                } else {
                    investingOutflow = investingOutflow.add(amount);
                }
            } else if ("筹资活动".equals(category)) {
                financing.put(itemName, amount);
                if ("流入".equals(direction)) {
                    financingInflow = financingInflow.add(amount);
                } else {
                    financingOutflow = financingOutflow.add(amount);
                }
            }
        }
        
        // 计算各活动的净额
        BigDecimal operatingNet = operatingInflow.subtract(operatingOutflow);
        BigDecimal investingNet = investingInflow.subtract(investingOutflow);
        BigDecimal financingNet = financingInflow.subtract(financingOutflow);
        
        operating.put("经营活动产生的现金流量净额", operatingNet);
        investing.put("投资活动产生的现金流量净额", investingNet);
        financing.put("筹资活动产生的现金流量净额", financingNet);
        
        // 汇总
        BigDecimal netIncrease = operatingNet.add(investingNet).add(financingNet);
        
        result.put("经营活动", operating);
        result.put("投资活动", investing);
        result.put("筹资活动", financing);
        result.put("现金及现金等价物净增加额", netIncrease);
        result.put("期初现金及现金等价物余额", cashBegin);
        result.put("期末现金及现金等价物余额", cashEnd);
        result.put("报表期间", startDate + " 至 " + endDate);
        
        return result;
    }
    
    // 辅助方法
    private BigDecimal calculateTypeTotal(List<AccountSubject> subjects, 
                                         Map<String, BigDecimal> balances, 
                                         String type) {
        return subjects.stream()
            .filter(s -> s.getSubjectType().equals(type))
            .map(s -> {
                BigDecimal balance = balances.getOrDefault(s.getSubjectCode(), BigDecimal.ZERO);
                // 累计折旧(1602)是资产的抵减科目，需要从资产总计中减去
                if ("Asset".equals(type) && "1602".equals(s.getSubjectCode())) {
                    return balance.negate();
                }
                return balance;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private Map<String, BigDecimal> calculateAssetItems(List<AccountSubject> subjects,
                                                        Map<String, BigDecimal> balances,
                                                        String category) {
        Map<String, BigDecimal> items = new LinkedHashMap<>();
        // 流动资产：现金、银行存款、应收账款等（1001-1299）
        // 非流动资产：固定资产等（1601-）
        if ("流动".equals(category)) {
            BigDecimal cash = balances.getOrDefault("1001", BigDecimal.ZERO);
            BigDecimal bank = balances.getOrDefault("1002", BigDecimal.ZERO);
            BigDecimal money = cash.add(bank);
            if (money.compareTo(BigDecimal.ZERO) != 0) {
                items.put("货币资金", money);
            }
            
            BigDecimal receivable = balances.getOrDefault("1122", BigDecimal.ZERO);
            if (receivable.compareTo(BigDecimal.ZERO) != 0) {
                items.put("应收账款", receivable);
            }
            
            BigDecimal materials = balances.getOrDefault("1403", BigDecimal.ZERO);
            if (materials.compareTo(BigDecimal.ZERO) != 0) {
                items.put("原材料", materials);
            }
            
            BigDecimal inventory = balances.getOrDefault("1405", BigDecimal.ZERO);
            if (inventory.compareTo(BigDecimal.ZERO) != 0) {
                items.put("库存商品", inventory);
            }
            
            // 计算流动资产合计
            BigDecimal currentAssetTotal = items.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            items.put("流动资产合计", currentAssetTotal);
        } else {
            // 非流动资产
            BigDecimal fixedAsset = balances.getOrDefault("1601", BigDecimal.ZERO);
            BigDecimal accumulated = balances.getOrDefault("1602", BigDecimal.ZERO);
            BigDecimal fixedAssetNet = fixedAsset.subtract(accumulated);
            if (fixedAssetNet.compareTo(BigDecimal.ZERO) != 0) {
                items.put("固定资产原值", fixedAsset);
                items.put("减：累计折旧", accumulated);
                items.put("固定资产净值", fixedAssetNet);
            }
            
            BigDecimal prepaid = balances.getOrDefault("1801", BigDecimal.ZERO);
            if (prepaid.compareTo(BigDecimal.ZERO) != 0) {
                items.put("长期待摊费用", prepaid);
            }
            
            // 计算非流动资产合计
            BigDecimal nonCurrentAssetTotal = fixedAssetNet.add(prepaid);
            items.put("非流动资产合计", nonCurrentAssetTotal);
        }
        return items;
    }
    
    private Map<String, BigDecimal> calculateLiabilityItems(List<AccountSubject> subjects,
                                                            Map<String, BigDecimal> balances,
                                                            String category) {
        Map<String, BigDecimal> items = new LinkedHashMap<>();
        if ("流动".equals(category)) {
            BigDecimal shortLoan = balances.getOrDefault("2001", BigDecimal.ZERO);
            if (shortLoan.compareTo(BigDecimal.ZERO) != 0) {
                items.put("短期借款", shortLoan);
            }
            
            BigDecimal payable = balances.getOrDefault("2202", BigDecimal.ZERO);
            if (payable.compareTo(BigDecimal.ZERO) != 0) {
                items.put("应付账款", payable);
            }
            
            BigDecimal salaryPayable = balances.getOrDefault("2211", BigDecimal.ZERO);
            if (salaryPayable.compareTo(BigDecimal.ZERO) != 0) {
                items.put("应付职工薪酬", salaryPayable);
            }
            
            BigDecimal tax = balances.getOrDefault("2221", BigDecimal.ZERO);
            if (tax.compareTo(BigDecimal.ZERO) != 0) {
                items.put("应交税费", tax);
            }
            
            BigDecimal interestPayable = balances.getOrDefault("2231", BigDecimal.ZERO);
            if (interestPayable.compareTo(BigDecimal.ZERO) != 0) {
                items.put("应付利息", interestPayable);
            }
            
            // 计算流动负债合计
            BigDecimal currentLiabilityTotal = items.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            items.put("流动负债合计", currentLiabilityTotal);
        } else {
            BigDecimal longLoan = balances.getOrDefault("2501", BigDecimal.ZERO);
            if (longLoan.compareTo(BigDecimal.ZERO) != 0) {
                items.put("长期借款", longLoan);
            }
            
            // 计算非流动负债合计
            BigDecimal nonCurrentLiabilityTotal = items.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            items.put("非流动负债合计", nonCurrentLiabilityTotal);
        }
        return items;
    }
    
    /**
     * 导出资产负债表为Excel
     */
    public byte[] exportBalanceSheetToExcel(String startDate, String endDate) throws IOException {
        Map<String, Object> data = generateBalanceSheet(startDate, endDate);
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("资产负债表");
            
            // 创建样式
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle amountStyle = createAmountStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);
            
            int rowNum = 0;
            
            // 标题
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("资产负债表");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            
            // 日期
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("报表日期：" + data.get("报表日期"));
            dateCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            
            // 空行
            rowNum++;
            
            // 表头
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("资产");
            headerRow.createCell(1).setCellValue("金额(元)");
            headerRow.createCell(2).setCellValue("负债及所有者权益");
            headerRow.createCell(3).setCellValue("金额(元)");
            for (int i = 0; i < 4; i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }
            
            // 数据行
            @SuppressWarnings("unchecked")
            Map<String, Object> assets = (Map<String, Object>) data.get("资产");
            @SuppressWarnings("unchecked")
            Map<String, Object> liabilities = (Map<String, Object>) data.get("负债");
            @SuppressWarnings("unchecked")
            Map<String, Object> equity = (Map<String, Object>) data.get("所有者权益");
            
            // 资产项目
            List<String[]> assetItems = new ArrayList<>();
            assetItems.add(new String[]{"流动资产", ""});
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> currentAssets = (Map<String, BigDecimal>) assets.get("流动资产");
            for (Map.Entry<String, BigDecimal> entry : currentAssets.entrySet()) {
                assetItems.add(new String[]{"  " + entry.getKey(), formatAmount(entry.getValue())});
            }
            assetItems.add(new String[]{"非流动资产", ""});
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> nonCurrentAssets = (Map<String, BigDecimal>) assets.get("非流动资产");
            for (Map.Entry<String, BigDecimal> entry : nonCurrentAssets.entrySet()) {
                assetItems.add(new String[]{"  " + entry.getKey(), formatAmount(entry.getValue())});
            }
            assetItems.add(new String[]{"资产总计", formatAmount((BigDecimal) assets.get("资产总计"))});
            
            // 负债及权益项目
            List<String[]> liabilityItems = new ArrayList<>();
            liabilityItems.add(new String[]{"流动负债", ""});
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> currentLiabilities = (Map<String, BigDecimal>) liabilities.get("流动负债");
            for (Map.Entry<String, BigDecimal> entry : currentLiabilities.entrySet()) {
                liabilityItems.add(new String[]{"  " + entry.getKey(), formatAmount(entry.getValue())});
            }
            liabilityItems.add(new String[]{"非流动负债", ""});
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> nonCurrentLiabilities = (Map<String, BigDecimal>) liabilities.get("非流动负债");
            for (Map.Entry<String, BigDecimal> entry : nonCurrentLiabilities.entrySet()) {
                liabilityItems.add(new String[]{"  " + entry.getKey(), formatAmount(entry.getValue())});
            }
            liabilityItems.add(new String[]{"负债合计", formatAmount((BigDecimal) liabilities.get("负债合计"))});
            liabilityItems.add(new String[]{"", ""});
            liabilityItems.add(new String[]{"所有者权益", ""});
            for (Map.Entry<String, Object> entry : equity.entrySet()) {
                if (!"所有者权益合计".equals(entry.getKey())) {
                    liabilityItems.add(new String[]{"  " + entry.getKey(), formatAmount((BigDecimal) entry.getValue())});
                }
            }
            liabilityItems.add(new String[]{"所有者权益合计", formatAmount((BigDecimal) equity.get("所有者权益合计"))});
            liabilityItems.add(new String[]{"负债及所有者权益总计", formatAmount((BigDecimal) data.get("负债及所有者权益总计"))});
            
            // 填充数据
            int maxRows = Math.max(assetItems.size(), liabilityItems.size());
            for (int i = 0; i < maxRows; i++) {
                Row row = sheet.createRow(rowNum++);
                
                // 资产列
                if (i < assetItems.size()) {
                    String[] item = assetItems.get(i);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(item[0]);
                    cell0.setCellStyle(item[0].contains("总计") || item[0].contains("合计") ? totalStyle : dataStyle);
                    
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(item[1]);
                    cell1.setCellStyle(item[0].contains("总计") || item[0].contains("合计") ? totalStyle : amountStyle);
                }
                
                // 负债及权益列
                if (i < liabilityItems.size()) {
                    String[] item = liabilityItems.get(i);
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(item[0]);
                    cell2.setCellStyle(item[0].contains("总计") || item[0].contains("合计") ? totalStyle : dataStyle);
                    
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(item[1]);
                    cell3.setCellStyle(item[0].contains("总计") || item[0].contains("合计") ? totalStyle : amountStyle);
                }
            }
            
            // 平衡校验
            rowNum++;
            Row balanceRow = sheet.createRow(rowNum);
            Cell balanceCell = balanceRow.createCell(0);
            Boolean isBalanced = (Boolean) data.get("平衡校验");
            balanceCell.setCellValue(isBalanced ? "✓ 报表平衡校验通过" : "✗ 报表平衡校验失败");
            balanceCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));
            
            // 调整列宽
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 4000);
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    /**
     * 导出利润表为Excel
     */
    public byte[] exportIncomeStatementToExcel(String startDate, String endDate) throws IOException {
        Map<String, Object> data = generateIncomeStatement(startDate, endDate);
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("利润表");
            
            // 创建样式
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle amountStyle = createAmountStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);
            
            int rowNum = 0;
            
            // 标题
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("利润表");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
            
            // 日期
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue(data.get("报表期间").toString());
            dateCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
            
            // 空行
            rowNum++;
            
            // 表头
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("项目");
            headerRow.createCell(1).setCellValue("金额(元)");
            for (int i = 0; i < 2; i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }
            
            // 数据行
            String[][] items = {
                {"一、营业收入", "营业收入", "false"},
                {"减：营业成本", "营业成本", "false"},
                {"    销售费用", "销售费用", "false"},
                {"    管理费用", "管理费用", "false"},
                {"    财务费用", "财务费用", "false"},
                {"二、营业利润", "营业利润", "true"},
                {"加：营业外收入", "营业外收入", "false"},
                {"减：营业外支出", "营业外支出", "false"},
                {"三、利润总额", "利润总额", "true"},
                {"减：所得税费用", "所得税费用", "false"},
                {"四、净利润", "净利润", "true"}
            };
            
            for (String[] item : items) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(item[0]);
                boolean isTotal = Boolean.parseBoolean(item[2]);
                cell0.setCellStyle(isTotal ? totalStyle : dataStyle);
                
                Cell cell1 = row.createCell(1);
                BigDecimal value = (BigDecimal) data.get(item[1]);
                cell1.setCellValue(formatAmount(value));
                cell1.setCellStyle(isTotal ? totalStyle : amountStyle);
            }
            
            // 调整列宽
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 4000);
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    /**
     * 导出现金流量表为Excel（详细版）
     */
    public byte[] exportCashFlowToExcel(String startDate, String endDate) throws IOException {
        Map<String, Object> data = generateCashFlowStatement(startDate, endDate);
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("现金流量表");
            
            // 创建样式
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle amountStyle = createAmountStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);
            
            int rowNum = 0;
            
            // 标题
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("现金流量表");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
            
            // 日期
            Row dateRow = sheet.createRow(rowNum++);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue(data.get("报表期间").toString());
            dateCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
            
            // 空行
            rowNum++;
            
            // 一、经营活动
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> operating = (Map<String, BigDecimal>) data.get("经营活动");
            rowNum = writeCashFlowSection(sheet, rowNum, "一、经营活动产生的现金流量", operating, 
                    new String[]{"销售商品、提供劳务收到的现金", "购买商品、接受劳务支付的现金", 
                                "支付给职工以及为职工支付的现金", "支付的各项税费", "支付的其他与经营活动有关的现金"},
                    "经营活动产生的现金流量净额", headerStyle, dataStyle, amountStyle, totalStyle);
            
            // 二、投资活动
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> investing = (Map<String, BigDecimal>) data.get("投资活动");
            rowNum = writeCashFlowSection(sheet, rowNum, "二、投资活动产生的现金流量", investing,
                    new String[]{"购建固定资产、无形资产和其他长期资产支付的现金", "投资支付的现金"},
                    "投资活动产生的现金流量净额", headerStyle, dataStyle, amountStyle, totalStyle);
            
            // 三、筹资活动
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> financing = (Map<String, BigDecimal>) data.get("筹资活动");
            rowNum = writeCashFlowSection(sheet, rowNum, "三、筹资活动产生的现金流量", financing,
                    new String[]{"吸收投资收到的现金", "取得借款收到的现金", "偿还债务支付的现金", "分配股利、利润或偿付利息支付的现金"},
                    "筹资活动产生的现金流量净额", headerStyle, dataStyle, amountStyle, totalStyle);
            
            // 汇总
            Row summaryHeaderRow = sheet.createRow(rowNum++);
            Cell summaryHeaderCell = summaryHeaderRow.createCell(0);
            summaryHeaderCell.setCellValue("现金及现金等价物净增加情况");
            summaryHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));
            
            String[] summaryItems = {"现金及现金等价物净增加额", "期初现金及现金等价物余额", "期末现金及现金等价物余额"};
            for (String item : summaryItems) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(item);
                boolean isTotal = item.contains("净增加额") || item.contains("期末");
                cell0.setCellStyle(isTotal ? totalStyle : dataStyle);
                
                Cell cell1 = row.createCell(1);
                BigDecimal value = (BigDecimal) data.get(item);
                cell1.setCellValue(formatAmount(value));
                cell1.setCellStyle(isTotal ? totalStyle : amountStyle);
            }
            
            // 调整列宽
            sheet.setColumnWidth(0, 10000);
            sheet.setColumnWidth(1, 5000);
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    /**
     * 写入现金流量表的一个部分
     */
    private int writeCashFlowSection(Sheet sheet, int startRow, String title, Map<String, BigDecimal> data,
                                    String[] itemNames, String totalName, 
                                    CellStyle headerStyle, CellStyle dataStyle, 
                                    CellStyle amountStyle, CellStyle totalStyle) {
        int rowNum = startRow;
        
        // 标题行
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));
        
        // 数据行
        for (String itemName : itemNames) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue("  " + itemName);
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            BigDecimal value = data.getOrDefault(itemName, BigDecimal.ZERO);
            cell1.setCellValue(formatAmount(value));
            cell1.setCellStyle(amountStyle);
        }
        
        // 小计行
        Row totalRow = sheet.createRow(rowNum++);
        Cell totalCell0 = totalRow.createCell(0);
        totalCell0.setCellValue(totalName);
        totalCell0.setCellStyle(totalStyle);
        
        Cell totalCell1 = totalRow.createCell(1);
        BigDecimal totalValue = data.getOrDefault(totalName, BigDecimal.ZERO);
        totalCell1.setCellValue(formatAmount(totalValue));
        totalCell1.setCellStyle(totalStyle);
        
        // 空行
        rowNum++;
        
        return rowNum;
    }
    
    // Excel样式创建方法
    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createAmountStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private String formatAmount(BigDecimal amount) {
        if (amount == null) return "0.00";
        return String.format("%,.2f", amount);
    }
}


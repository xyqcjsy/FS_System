package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.service.FinancialReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 财务报表Controller
 */
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class FinancialReportController {
    
    private final FinancialReportService financialReportService;
    
    /**
     * 生成资产负债表
     */
    @GetMapping("/balance-sheet")
    public Result<Map<String, Object>> balanceSheet(
            @RequestParam(required = false) String startDate,
            @RequestParam String endDate) {
        Map<String, Object> report = financialReportService.generateBalanceSheet(startDate, endDate);
        return Result.successData(report);
    }
    
    /**
     * 导出资产负债表为Excel
     */
    @GetMapping("/balance-sheet/export")
    public void exportBalanceSheet(
            @RequestParam(required = false) String startDate,
            @RequestParam String endDate,
            HttpServletResponse response) throws IOException {
        byte[] excelData = financialReportService.exportBalanceSheetToExcel(startDate, endDate);
        
        String fileName = URLEncoder.encode("资产负债表_" + endDate + ".xlsx", StandardCharsets.UTF_8.toString());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentLength(excelData.length);
        response.getOutputStream().write(excelData);
        response.getOutputStream().flush();
    }
    
    /**
     * 生成利润表
     */
    @GetMapping("/income-statement")
    public Result<Map<String, Object>> incomeStatement(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Map<String, Object> report = financialReportService.generateIncomeStatement(startDate, endDate);
        return Result.successData(report);
    }
    
    /**
     * 导出利润表为Excel
     */
    @GetMapping("/income-statement/export")
    public void exportIncomeStatement(
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletResponse response) throws IOException {
        byte[] excelData = financialReportService.exportIncomeStatementToExcel(startDate, endDate);
        
        String fileName = URLEncoder.encode("利润表_" + startDate + "至" + endDate + ".xlsx", StandardCharsets.UTF_8.toString());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentLength(excelData.length);
        response.getOutputStream().write(excelData);
        response.getOutputStream().flush();
    }
    
    /**
     * 生成现金流量表
     */
    @GetMapping("/cash-flow")
    public Result<Map<String, Object>> cashFlow(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Map<String, Object> report = financialReportService.generateCashFlowStatement(startDate, endDate);
        return Result.successData(report);
    }
    
    /**
     * 导出现金流量表为Excel
     */
    @GetMapping("/cash-flow/export")
    public void exportCashFlow(
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletResponse response) throws IOException {
        byte[] excelData = financialReportService.exportCashFlowToExcel(startDate, endDate);
        
        String fileName = URLEncoder.encode("现金流量表_" + startDate + "至" + endDate + ".xlsx", StandardCharsets.UTF_8.toString());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentLength(excelData.length);
        response.getOutputStream().write(excelData);
        response.getOutputStream().flush();
    }
}


import request from '@/utils/request'

// 财务报表 API

/**
 * 获取资产负债表
 */
export const getBalanceSheet = (params) => {
  return request.get('/report/balance-sheet', { params })
}

/**
 * 获取利润表
 */
export const getIncomeStatement = (params) => {
  return request.get('/report/income-statement', { params })
}

/**
 * 获取现金流量表
 */
export const getCashFlow = (params) => {
  return request.get('/report/cash-flow', { params })
}

export default {
  getBalanceSheet,
  getIncomeStatement,
  getCashFlow
}


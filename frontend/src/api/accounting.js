import request from '@/utils/request'

// 凭证 API
export const voucherApi = {
  getList() {
    return request.get('/voucher/list')
  },
  getById(id) {
    return request.get(`/voucher/${id}`)
  },
  getEntries(id) {
    return request.get(`/voucher/${id}/entries`)
  },
  create(data) {
    return request.post('/voucher', data)
  },
  update(data) {
    return request.put('/voucher', data)
  },
  post(id) {
    return request.post(`/voucher/${id}/post`)
  },
  delete(id) {
    return request.delete(`/voucher/${id}`)
  }
}

// 过账 API
export const postingApi = {
  // 采购订单过账
  postPurchaseOrder(orderId) {
    return request.post(`/posting/purchase/${orderId}`)
  },
  batchPostPurchaseOrders(orderIds) {
    return request.post('/posting/purchase/batch', { orderIds })
  },
  // 销售发票过账
  postSalesInvoice(invoiceId) {
    return request.post(`/posting/sales/${invoiceId}`)
  },
  batchPostSalesInvoices(invoiceIds) {
    return request.post('/posting/sales/batch', { invoiceIds })
  },
  // 费用报销过账
  postExpenseClaim(claimId) {
    return request.post(`/posting/expense/${claimId}`)
  },
  batchPostExpenseClaims(claimIds) {
    return request.post('/posting/expense/batch', { claimIds })
  }
}

// 导出便捷函数
export const getVoucherList = (params) => request.get('/voucher/list', { params })
export const getVoucherDetail = (id) => request.get(`/voucher/${id}`)
export const addVoucher = (data) => request.post('/voucher', data)
export const updateVoucher = (data) => request.put('/voucher', data)
export const deleteVoucher = (id) => request.delete(`/voucher/${id}`)
export const postVoucher = (id) => request.post(`/voucher/${id}/post`)

export const postBusiness = (data) => request.post('/posting/business', data)

export const getReconciliationList = (params) => request.get('/reconciliation/list', { params })
export const addReconciliation = (data) => request.post('/reconciliation', data)
export const updateReconciliation = (data) => request.put('/reconciliation', data)
export const deleteReconciliation = (id) => request.delete(`/reconciliation/${id}`)

// 对账 API
export const reconciliationApi = {
  getList(params) {
    return request.get('/reconciliation/list', { params })
  },
  getById(id) {
    return request.get(`/reconciliation/${id}`)
  },
  create(data) {
    return request.post('/reconciliation', data)
  },
  update(data) {
    return request.put('/reconciliation', data)
  },
  delete(id) {
    return request.delete(`/reconciliation/${id}`)
  },
  autoReconcile(data) {
    return request.post('/reconciliation/auto', data)
  },
  // 获取科目余额（基于所有凭证自动计算）
  getSubjectBalance(subjectId, date) {
    return request.get(`/reconciliation/subject-balance/${subjectId}`, {
      params: { date }
    })
  }
}

// 税务记录 API
export const taxRecordApi = {
  getList(params) {
    return request.get('/tax/list', { params })
  },
  getById(id) {
    return request.get(`/tax/${id}`)
  },
  create(data) {
    return request.post('/tax', data)
  },
  update(data) {
    return request.put('/tax', data)
  },
  delete(id) {
    return request.delete(`/tax/${id}`)
  },
  declare(id) {
    return request.put(`/tax/${id}/declare`)
  },
  pay(id) {
    return request.put(`/tax/${id}/pay`)
  }
}

// 定期业务 API
export const periodicOperationApi = {
  getList(params) {
    return request.get('/periodic/list', { params })
  },
  getById(id) {
    return request.get(`/periodic/${id}`)
  },
  create(data) {
    return request.post('/periodic', data)
  },
  update(data) {
    return request.put('/periodic', data)
  },
  delete(id) {
    return request.delete(`/periodic/${id}`)
  },
  execute(id) {
    return request.put(`/periodic/${id}/execute`)
  }
}

// 以下为兼容旧代码的导出
export const getTaxRecordList = (params) => request.get('/tax/list', { params })
export const addTaxRecord = (data) => request.post('/tax', data)
export const updateTaxRecord = (data) => request.put('/tax', data)
export const deleteTaxRecord = (id) => request.delete(`/tax/${id}`)

export const getPeriodicOperationList = (params) => request.get('/periodic/list', { params })
export const addPeriodicOperation = (data) => request.post('/periodic', data)
export const updatePeriodicOperation = (data) => request.put('/periodic', data)
export const deletePeriodicOperation = (id) => request.delete(`/periodic/${id}`)


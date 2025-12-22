import request from '@/utils/request'

// 采购订单 API
export const purchaseOrderApi = {
  getList() {
    return request.get('/purchase-order/list')
  },
  getById(id) {
    return request.get(`/purchase-order/${id}`)
  },
  getItems(id) {
    return request.get(`/purchase-order/${id}/items`)
  },
  getUnposted() {
    return request.get('/purchase-order/unposted')
  },
  create(data) {
    return request.post('/purchase-order', data)
  },
  update(data) {
    return request.put('/purchase-order', data)
  },
  delete(id) {
    return request.delete(`/purchase-order/${id}`)
  }
}

// 销售订单 API
export const salesInvoiceApi = {
  getList() {
    return request.get('/sales-invoice/list')
  },
  getById(id) {
    return request.get(`/sales-invoice/${id}`)
  },
  create(data) {
    return request.post('/sales-invoice', data)
  },
  update(data) {
    return request.put('/sales-invoice', data)
  },
  delete(id) {
    return request.delete(`/sales-invoice/${id}`)
  }
}

// 费用报销 API
export const expenseClaimApi = {
  getList() {
    return request.get('/expense-claim/list')
  },
  getById(id) {
    return request.get(`/expense-claim/${id}`)
  },
  create(data) {
    return request.post('/expense-claim', data)
  },
  update(data) {
    return request.put('/expense-claim', data)
  },
  delete(id) {
    return request.delete(`/expense-claim/${id}`)
  }
}

// 导出便捷函数
export const getPurchaseOrderList = () => request.get('/purchase-order/list')
export const getPurchaseOrderDetail = (id) => request.get(`/purchase-order/${id}`)
export const addPurchaseOrder = (data) => request.post('/purchase-order', data)
export const updatePurchaseOrder = (data) => request.put('/purchase-order', data)
export const deletePurchaseOrder = (id) => request.delete(`/purchase-order/${id}`)
export const confirmPurchaseOrder = (id) => request.put(`/purchase-order/${id}/confirm`)

export const getSalesInvoiceList = (params) => request.get('/sales/list', { params })
export const getSalesInvoiceDetail = (id) => request.get(`/sales/${id}`)
export const addSalesInvoice = (data) => request.post('/sales', data)
export const updateSalesInvoice = (data) => request.put('/sales', data)
export const deleteSalesInvoice = (id) => request.delete(`/sales/${id}`)
export const confirmSalesInvoice = (id) => request.put(`/sales/${id}/confirm`)

export const getExpenseClaimList = (params) => request.get('/expense/list', { params })
export const getExpenseClaimDetail = (id) => request.get(`/expense/${id}`)
export const addExpenseClaim = (data) => request.post('/expense', data)
export const updateExpenseClaim = (data) => request.put('/expense', data)
export const deleteExpenseClaim = (id) => request.delete(`/expense/${id}`)
export const submitExpenseClaim = (id) => request.put(`/expense/${id}/submit`)
export const approveExpenseClaim = (id) => request.put(`/expense/${id}/approve`)
export const rejectExpenseClaim = (id, reason) => request.put(`/expense/${id}/reject`, { reason })


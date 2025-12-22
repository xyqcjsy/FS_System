import request from '@/utils/request'

// 企业信息 API
export const companyApi = {
  // 获取企业列表
  getList() {
    return request.get('/company/list')
  },
  // 根据ID获取企业信息
  getById(id) {
    return request.get(`/company/${id}`)
  },
  // 创建企业
  create(data) {
    return request.post('/company', data)
  },
  // 更新企业
  update(data) {
    return request.put('/company', data)
  },
  // 删除企业
  delete(id) {
    return request.delete(`/company/${id}`)
  }
}

// 会计科目 API
export const accountSubjectApi = {
  // 获取科目列表
  getList() {
    return request.get('/account-subject/list')
  },
  // 根据ID获取科目
  getById(id) {
    return request.get(`/account-subject/${id}`)
  },
  // 根据类型获取科目
  getByType(type) {
    return request.get(`/account-subject/type/${type}`)
  },
  // 获取子科目
  getChildren(parentId) {
    return request.get(`/account-subject/children/${parentId}`)
  },
  // 获取叶子科目
  getLeafSubjects() {
    return request.get('/account-subject/leaf')
  },
  // 创建科目
  create(data) {
    return request.post('/account-subject', data)
  },
  // 更新科目
  update(data) {
    return request.put('/account-subject', data)
  },
  // 删除科目
  delete(id) {
    return request.delete(`/account-subject/${id}`)
  }
}

// 员工 API
export const employeeApi = {
  getList() {
    return request.get('/employee/list')
  },
  getById(id) {
    return request.get(`/employee/${id}`)
  },
  create(data) {
    return request.post('/employee', data)
  },
  update(data) {
    return request.put('/employee', data)
  },
  delete(id) {
    return request.delete(`/employee/${id}`)
  }
}

// 供应商 API
export const supplierApi = {
  getList() {
    return request.get('/supplier/list')
  },
  getById(id) {
    return request.get(`/supplier/${id}`)
  },
  create(data) {
    return request.post('/supplier', data)
  },
  update(data) {
    return request.put('/supplier', data)
  },
  delete(id) {
    return request.delete(`/supplier/${id}`)
  }
}

// 客户 API
export const customerApi = {
  getList() {
    return request.get('/customer/list')
  },
  getById(id) {
    return request.get(`/customer/${id}`)
  },
  create(data) {
    return request.post('/customer', data)
  },
  update(data) {
    return request.put('/customer', data)
  },
  delete(id) {
    return request.delete(`/customer/${id}`)
  }
}

// 导出便捷函数
export const getCompanyInfo = companyApi.getList
export const updateCompany = companyApi.update

export const getAccountSubjectList = accountSubjectApi.getList
export const addAccountSubject = accountSubjectApi.create
export const updateAccountSubject = accountSubjectApi.update
export const deleteAccountSubject = accountSubjectApi.delete

export const getEmployeeList = () => request.get('/employee/list')
export const addEmployee = employeeApi.create
export const updateEmployee = employeeApi.update
export const deleteEmployee = employeeApi.delete

export const getSupplierList = () => request.get('/supplier/list')
export const addSupplier = supplierApi.create
export const updateSupplier = supplierApi.update
export const deleteSupplier = supplierApi.delete

export const getCustomerList = () => request.get('/customer/list')
export const addCustomer = customerApi.create
export const updateCustomer = customerApi.update
export const deleteCustomer = customerApi.delete


<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户管理</span>
          <el-button type="primary" @click="handleAdd">新增客户</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="客户编号/名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="customerCode" label="客户编号" width="150" />
        <el-table-column prop="customerName" label="客户名称" width="200" />
        <el-table-column prop="contactPerson" label="联系人" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="150" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="creditLimit" label="信用额度" width="120" align="right" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'Active' ? 'success' : 'info'">
              {{ row.status === 'Active' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="客户编号" prop="customerCode">
          <el-input v-model="form.customerCode" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" type="textarea" />
        </el-form-item>
        <el-form-item label="税号">
          <el-input v-model="form.taxNumber" />
        </el-form-item>
        <el-form-item label="开户银行">
          <el-input v-model="form.bankName" />
        </el-form-item>
        <el-form-item label="银行账号">
          <el-input v-model="form.bankAccount" />
        </el-form-item>
        <el-form-item label="信用额度">
          <el-input-number v-model="form.creditLimit" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="Active">启用</el-radio>
            <el-radio label="Inactive">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCustomerList, addCustomer, updateCustomer, deleteCustomer } from '@/api/system'

const searchForm = reactive({
  keyword: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const formRef = ref()

const form = reactive({
  id: null,
  customerCode: '',
  customerName: '',
  contactPerson: '',
  contactPhone: '',
  email: '',
  address: '',
  taxNumber: '',
  bankName: '',
  bankAccount: '',
  creditLimit: 0,
  status: 'Active',
  remark: ''
})

const rules = {
  customerCode: [{ required: true, message: '请输入客户编号', trigger: 'blur' }],
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const { data } = await getCustomerList()
    // 后端返回的是数组，前端进行过滤和分页
    let filteredData = data || []
    
    // 关键词搜索过滤
    if (searchForm.keyword) {
      const keyword = searchForm.keyword.toLowerCase()
      filteredData = filteredData.filter(item => 
        (item.customerCode && item.customerCode.toLowerCase().includes(keyword)) ||
        (item.customerName && item.customerName.toLowerCase().includes(keyword))
      )
    }
    
    // 前端分页
    pagination.total = filteredData.length
    const start = (pagination.pageNum - 1) * pagination.pageSize
    const end = start + pagination.pageSize
    tableData.value = filteredData.slice(start, end)
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
    tableData.value = []
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  searchForm.keyword = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增客户'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑客户'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该客户吗？', '提示', {
      type: 'warning'
    })
    await deleteCustomer(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    // 用户取消
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (form.id) {
    await updateCustomer(form)
    ElMessage.success('更新成功')
  } else {
    await addCustomer(form)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadData()
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    customerCode: '',
    customerName: '',
    contactPerson: '',
    contactPhone: '',
    email: '',
    address: '',
    taxNumber: '',
    bankName: '',
    bankAccount: '',
    creditLimit: 0,
    status: 'Active',
    remark: ''
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>


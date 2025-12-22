<template>
  <div class="app-container business-container expense-claim-container">
    <el-card class="header-card">
      <template #header>
        <div class="card-header">
          <span>
            <el-icon><Money /></el-icon>
            费用报销管理
          </span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增报销单
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="报销单编号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="草稿" value="Draft" />
            <el-option label="已提交" value="Submitted" />
            <el-option label="已审批" value="Approved" />
            <el-option label="已付款" value="Paid" />
            <el-option label="已过账" value="Posted" />
            <el-option label="已拒绝" value="Rejected" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" border>
        <el-table-column prop="claimNo" label="报销单号" width="150" />
        <el-table-column prop="employeeName" label="报销人" width="120" />
        <el-table-column prop="claimDate" label="报销日期" width="120" />
        <el-table-column prop="totalAmount" label="报销总额" width="120" align="right">
          <template #default="{ row }">
            ¥{{ row.totalAmount?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isPosted" label="是否过账" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isPosted ? 'success' : 'info'">
              {{ row.isPosted ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)" v-if="row.status === 'Draft'">编辑</el-button>
            <el-button link type="success" @click="handleSubmit(row.id)" v-if="row.status === 'Draft'">提交</el-button>
            <el-button link type="success" @click="handleApprove(row.id)" v-if="row.status === 'Submitted'">审批</el-button>
            <el-button link type="danger" @click="handleReject(row.id)" v-if="row.status === 'Submitted'">拒绝</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)" v-if="row.status === 'Draft'">删除</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="900px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报销单编号" prop="claimNo">
              <el-input v-model="form.claimNo" :disabled="!!form.id" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报销人" prop="employeeId">
              <el-select v-model="form.employeeId" placeholder="请选择报销人" style="width: 100%">
                <el-option
                  v-for="item in employeeList"
                  :key="item.id"
                  :label="item.employeeName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报销日期" prop="claimDate">
              <el-date-picker
                v-model="form.claimDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报销总额">
              <el-input-number v-model="form.totalAmount" :precision="2" readonly style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 费用明细 -->
        <el-divider content-position="left">费用明细</el-divider>
        <el-button type="primary" size="small" @click="addItem" style="margin-bottom: 10px">添加明细</el-button>

        <el-table :data="form.items" border style="margin-bottom: 20px">
          <el-table-column label="费用类型" width="140">
            <template #default="{ row }">
              <el-select v-model="row.expenseType" placeholder="请选择">
                <el-option label="交通费" value="交通费" />
                <el-option label="住宿费" value="住宿费" />
                <el-option label="餐饮费" value="餐饮费" />
                <el-option label="会务费" value="会务费" />
                <el-option label="办公费" value="办公费" />
                <el-option label="招待费" value="招待费" />
                <el-option label="通讯费" value="通讯费" />
                <el-option label="其他" value="其他" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="发生日期" width="160">
            <template #default="{ row }">
              <el-date-picker
                v-model="row.expenseDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                size="small"
              />
            </template>
          </el-table-column>
          <el-table-column label="金额" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.amount" :min="0" :precision="2" @change="calculateTotal" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="费用说明">
            <template #default="{ row }">
              <el-input v-model="row.description" placeholder="请输入说明" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="备注" width="120">
            <template #default="{ row }">
              <el-input v-model="row.remark" placeholder="备注" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ $index }">
              <el-button link type="danger" @click="removeItem($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="报销单详情" width="900px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报销单号">{{ viewData.claimNo }}</el-descriptions-item>
        <el-descriptions-item label="报销人">{{ viewData.employeeName }}</el-descriptions-item>
        <el-descriptions-item label="报销日期">{{ viewData.claimDate }}</el-descriptions-item>
        <el-descriptions-item label="报销总额">¥{{ viewData.totalAmount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.status)">{{ getStatusLabel(viewData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否过账">
          <el-tag :type="viewData.isPosted ? 'success' : 'info'">
            {{ viewData.isPosted ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">费用明细</el-divider>
      <el-table :data="viewData.items" border>
        <el-table-column prop="expenseType" label="费用类型" width="120" />
        <el-table-column prop="expenseDate" label="发生日期" width="120" />
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="{ row }">¥{{ row.amount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="description" label="费用说明" />
        <el-table-column prop="remark" label="备注" width="120" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getExpenseClaimList, getExpenseClaimDetail, addExpenseClaim, updateExpenseClaim, deleteExpenseClaim, submitExpenseClaim, approveExpenseClaim, rejectExpenseClaim } from '@/api/business'
import { getEmployeeList } from '@/api/system'

const searchForm = reactive({ keyword: '', status: '' })
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })
const tableData = ref([])
const employeeList = ref([])
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('新增报销单')
const formRef = ref()

const form = reactive({
  id: null,
  claimNo: '',
  employeeId: null,
  claimDate: new Date().toISOString().split('T')[0],
  totalAmount: 0,
  status: 'Draft',
  remark: '',
  items: []
})

const viewData = ref({})

const rules = {
  claimNo: [{ required: true, message: '请输入报销单编号', trigger: 'blur' }],
  employeeId: [{ required: true, message: '请选择报销人', trigger: 'change' }],
  claimDate: [{ required: true, message: '请选择报销日期', trigger: 'change' }]
}

// 监听items变化，自动计算总金额
watch(() => form.items, () => {
  calculateTotal()
}, { deep: true })

const getStatusType = (status) => {
  const map = {
    'Draft': 'info',
    'Submitted': 'warning',
    'Approved': 'primary',
    'Paid': 'success',
    'Posted': 'success',
    'Rejected': 'danger'
  }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = {
    'Draft': '草稿',
    'Submitted': '已提交',
    'Approved': '已审批',
    'Paid': '已付款',
    'Posted': '已过账',
    'Rejected': '已拒绝'
  }
  return map[status] || status
}

const loadData = async () => {
  try {
    const { data } = await getExpenseClaimList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    })
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
    tableData.value = []
  }
}

const loadEmployees = async () => {
  try {
    const { data } = await getEmployeeList()
    employeeList.value = data || []
  } catch (error) {
    console.error('加载员工列表失败:', error)
    employeeList.value = []
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.status = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增报销单'
  resetForm()
  dialogVisible.value = true
}

const handleView = async (row) => {
  try {
    const { data } = await getExpenseClaimDetail(row.id)
    viewData.value = data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('加载详情失败:', error)
    ElMessage.error('加载详情失败')
  }
}

const handleEdit = async (row) => {
  try {
    const { data } = await getExpenseClaimDetail(row.id)
    dialogTitle.value = '编辑报销单'
    Object.assign(form, {
      ...data,
      items: data.items || []
    })
    dialogVisible.value = true
  } catch (error) {
    console.error('加载详情失败:', error)
    ElMessage.error('加载详情失败')
  }
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    if (form.items.length === 0) {
      ElMessage.warning('请至少添加一条费用明细')
      return
    }
    
    if (form.id) {
      await updateExpenseClaim(form)
      ElMessage.success('更新成功')
    } else {
      await addExpenseClaim(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      console.error('保存失败:', error)
      ElMessage.error('保存失败')
    }
  }
}

const handleSubmit = async (id) => {
  try {
    await ElMessageBox.confirm('确定要提交该报销单吗？提交后将无法修改', '提示', { type: 'warning' })
    await submitExpenseClaim(id)
    ElMessage.success('提交成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    }
  }
}

const handleApprove = async (id) => {
  try {
    await ElMessageBox.confirm('确定要审批通过该报销单吗？', '提示', { type: 'warning' })
    await approveExpenseClaim(id)
    ElMessage.success('审批成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('审批失败:', error)
      ElMessage.error('审批失败')
    }
  }
}

const handleReject = async (id) => {
  try {
    const { value } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝报销', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /.+/,
      inputErrorMessage: '请输入拒绝原因'
    })
    await rejectExpenseClaim(id, value)
    ElMessage.success('已拒绝')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('拒绝失败:', error)
      ElMessage.error('拒绝失败')
    }
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该报销单吗？', '提示', { type: 'warning' })
    await deleteExpenseClaim(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const addItem = () => {
  form.items.push({
    expenseType: '',
    expenseDate: new Date().toISOString().split('T')[0],
    amount: 0,
    description: '',
    attachmentUrl: '',
    remark: ''
  })
}

const removeItem = (index) => {
  form.items.splice(index, 1)
}

const calculateTotal = () => {
  form.totalAmount = form.items.reduce((sum, item) => sum + (Number(item.amount) || 0), 0)
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    claimNo: 'EXP' + Date.now(),
    employeeId: null,
    claimDate: new Date().toISOString().split('T')[0],
    totalAmount: 0,
    status: 'Draft',
    remark: '',
    items: []
  })
  formRef.value?.clearValidate()
}

onMounted(() => {
  loadData()
  loadEmployees()
})
</script>

<style scoped lang="scss">
// 费用报销模块特定样式
.expense-claim-container {
  // 金额列特殊样式
  :deep(.el-table) {
    .amount-cell {
      font-weight: 600;
      font-family: 'Inter', 'SF Pro Display', monospace;
      color: var(--primary-color);
    }
  }
}
</style>

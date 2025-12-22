<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>定期业务管理</span>
          <el-button type="primary" @click="handleAdd">新增定期业务</el-button>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="业务期间">
          <el-input v-model="queryForm.period" placeholder="如：2025-01" clearable />
        </el-form-item>
        <el-form-item label="业务类型">
          <el-select v-model="queryForm.operationType" placeholder="请选择" clearable style="width: 180px">
            <el-option label="计提折旧" value="计提折旧" />
            <el-option label="费用摊销" value="费用摊销" />
            <el-option label="期末结转" value="期末结转" />
            <el-option label="工资计提" value="工资计提" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 150px">
            <el-option label="待执行" value="Pending" />
            <el-option label="已执行" value="Executed" />
            <el-option label="已取消" value="Cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border>
        <el-table-column prop="operationType" label="业务类型" width="150" />
        <el-table-column prop="operationPeriod" label="业务期间" width="120" />
        <el-table-column prop="amount" label="金额" width="150" align="right">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="voucherId" label="关联凭证ID" width="120" />
        <el-table-column prop="executedAt" label="执行时间" width="160" />
        <el-table-column prop="description" label="说明" min-width="200" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)" v-if="row.status === 'Pending'">
              编辑
            </el-button>
            <el-button 
              link 
              type="success" 
              @click="handleExecute(row)" 
              v-if="row.status === 'Pending'"
            >
              执行
            </el-button>
            <el-button 
              link 
              type="info" 
              @click="handleViewVoucher(row)" 
              v-if="row.voucherId"
            >
              查看凭证
            </el-button>
            <el-button 
              link 
              type="danger" 
              @click="handleDelete(row)" 
              v-if="row.status === 'Pending'"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        class="pagination"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="业务类型" prop="operationType">
          <el-select v-model="formData.operationType" placeholder="请选择业务类型">
            <el-option label="计提折旧" value="计提折旧" />
            <el-option label="费用摊销" value="费用摊销" />
            <el-option label="期末结转" value="期末结转" />
            <el-option label="工资计提" value="工资计提" />
            <el-option label="利息计提" value="利息计提" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务期间" prop="operationPeriod">
          <el-input v-model="formData.operationPeriod" placeholder="如：2025-01" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number
            v-model="formData.amount"
            :precision="2"
            :min="0"
            :controls="false"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="说明" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入业务说明"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 凭证查看对话框 -->
    <el-dialog
      v-model="voucherDialogVisible"
      title="凭证详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="凭证编号">{{ voucherDetail.voucherNo }}</el-descriptions-item>
        <el-descriptions-item label="凭证日期">{{ voucherDetail.voucherDate }}</el-descriptions-item>
        <el-descriptions-item label="摘要" :span="2">{{ voucherDetail.abstractText }}</el-descriptions-item>
      </el-descriptions>
      
      <el-divider>分录明细</el-divider>
      
      <el-table :data="voucherDetail.entries" border>
        <el-table-column prop="lineNumber" label="行号" width="80" />
        <el-table-column prop="subjectName" label="科目" width="200" />
        <el-table-column prop="debitAmount" label="借方" width="150" align="right">
          <template #default="{ row }">
            {{ row.debitAmount > 0 ? '¥' + row.debitAmount.toFixed(2) : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="creditAmount" label="贷方" width="150" align="right">
          <template #default="{ row }">
            {{ row.creditAmount > 0 ? '¥' + row.creditAmount.toFixed(2) : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="abstractText" label="摘要" min-width="200" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { periodicOperationApi } from '@/api/accounting'
import { voucherApi } from '@/api/accounting'

// 查询表单
const queryForm = reactive({
  period: '',
  operationType: '',
  status: ''
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增定期业务')
const formRef = ref()

// 凭证查看对话框
const voucherDialogVisible = ref(false)
const voucherDetail = ref({})

// 表单数据
const formData = reactive({
  id: null,
  operationType: '',
  operationPeriod: '',
  amount: 0,
  description: '',
  remark: ''
})

// 表单验证规则
const rules = {
  operationType: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
  operationPeriod: [{ required: true, message: '请输入业务期间', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  description: [{ required: true, message: '请输入业务说明', trigger: 'blur' }]
}

// 加载数据
const loadData = async () => {
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      period: queryForm.period || undefined,
      operationType: queryForm.operationType || undefined,
      status: queryForm.status || undefined
    }
    const { data } = await periodicOperationApi.getList(params)
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增定期业务'
  Object.assign(formData, {
    id: null,
    operationType: '',
    operationPeriod: '',
    amount: 0,
    description: '',
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑定期业务'
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (formData.id) {
      await periodicOperationApi.update(formData)
      ElMessage.success('更新成功')
    } else {
      await periodicOperationApi.create(formData)
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('操作失败：' + (error.message || error))
    }
  }
}

// 执行定期业务
const handleExecute = async (row) => {
  try {
    await ElMessageBox.confirm(
      '确认要执行该定期业务吗？执行后将自动生成会计凭证。',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await periodicOperationApi.execute(row.id)
    ElMessage.success('执行成功，已生成会计凭证')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('执行失败：' + (error.message || error))
    }
  }
}

// 查看凭证
const handleViewVoucher = async (row) => {
  try {
    const { data } = await voucherApi.getById(row.voucherId)
    voucherDetail.value = data
    voucherDialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载凭证详情失败')
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认要删除该定期业务吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await periodicOperationApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + (error.message || error))
    }
  }
}

// 重置查询
const handleReset = () => {
  queryForm.period = ''
  queryForm.operationType = ''
  queryForm.status = ''
  pagination.pageNum = 1
  loadData()
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    'Pending': 'warning',
    'Executed': 'success',
    'Cancelled': 'info'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    'Pending': '待执行',
    'Executed': '已执行',
    'Cancelled': '已取消'
  }
  return textMap[status] || status
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.query-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-divider {
  margin: 20px 0;
}
</style>


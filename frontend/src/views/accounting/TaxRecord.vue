<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>税务记录管理</span>
          <el-button type="primary" @click="handleAdd">新增税务记录</el-button>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="税期">
          <el-input v-model="queryForm.period" placeholder="如：2025-01" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 150px">
            <el-option label="待处理" value="Pending" />
            <el-option label="已申报" value="Declared" />
            <el-option label="已缴纳" value="Paid" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border>
        <el-table-column prop="taxPeriod" label="税期" width="120" />
        <el-table-column prop="taxType" label="税种" width="150" />
        <el-table-column prop="taxableAmount" label="应税金额" width="150" align="right">
          <template #default="{ row }">
            ¥{{ row.taxableAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="taxRate" label="税率" width="100" align="center">
          <template #default="{ row }">
            {{ row.taxRate }}%
          </template>
        </el-table-column>
        <el-table-column prop="taxAmount" label="税额" width="150" align="right">
          <template #default="{ row }">
            ¥{{ row.taxAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="declareDate" label="申报日期" width="120" />
        <el-table-column prop="paymentDate" label="缴纳日期" width="120" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button 
              link 
              type="success" 
              @click="handleDeclare(row)" 
              v-if="row.status === 'Pending'"
            >
              申报
            </el-button>
            <el-button 
              link 
              type="warning" 
              @click="handlePay(row)" 
              v-if="row.status === 'Declared'"
            >
              标记已缴纳
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
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
        <el-form-item label="税期" prop="taxPeriod">
          <el-input v-model="formData.taxPeriod" placeholder="如：2025-01" />
        </el-form-item>
        <el-form-item label="税种" prop="taxType">
          <el-select v-model="formData.taxType" placeholder="请选择税种">
            <el-option label="增值税" value="增值税" />
            <el-option label="企业所得税" value="企业所得税" />
            <el-option label="个人所得税" value="个人所得税" />
            <el-option label="城市维护建设税" value="城市维护建设税" />
            <el-option label="教育费附加" value="教育费附加" />
            <el-option label="地方教育附加" value="地方教育附加" />
            <el-option label="印花税" value="印花税" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="应税金额" prop="taxableAmount">
          <el-input-number
            v-model="formData.taxableAmount"
            :precision="2"
            :min="0"
            :controls="false"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="税率(%)" prop="taxRate">
          <el-input-number
            v-model="formData.taxRate"
            :precision="2"
            :min="0"
            :max="100"
            :controls="false"
            style="width: 100%"
            @change="calculateTaxAmount"
          />
        </el-form-item>
        <el-form-item label="税额" prop="taxAmount">
          <el-input-number
            v-model="formData.taxAmount"
            :precision="2"
            :min="0"
            :controls="false"
            style="width: 100%"
            disabled
          />
        </el-form-item>
        <el-form-item label="申报日期" prop="declareDate">
          <el-date-picker
            v-model="formData.declareDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
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
import { taxRecordApi } from '@/api/accounting'

// 查询表单
const queryForm = reactive({
  period: '',
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
const dialogTitle = ref('新增税务记录')
const formRef = ref()

// 表单数据
const formData = reactive({
  id: null,
  taxPeriod: '',
  taxType: '',
  taxableAmount: 0,
  taxRate: 0,
  taxAmount: 0,
  status: 'Pending',
  declareDate: null,
  paymentDate: null,
  remark: ''
})

// 表单验证规则
const rules = {
  taxPeriod: [{ required: true, message: '请输入税期', trigger: 'blur' }],
  taxType: [{ required: true, message: '请选择税种', trigger: 'change' }],
  taxableAmount: [{ required: true, message: '请输入应税金额', trigger: 'blur' }],
  taxRate: [{ required: true, message: '请输入税率', trigger: 'blur' }]
}

// 加载数据
const loadData = async () => {
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      period: queryForm.period || undefined,
      status: queryForm.status || undefined
    }
    const { data } = await taxRecordApi.getList(params)
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

// 计算税额
const calculateTaxAmount = () => {
  if (formData.taxableAmount && formData.taxRate) {
    formData.taxAmount = (formData.taxableAmount * formData.taxRate / 100).toFixed(2)
  }
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增税务记录'
  Object.assign(formData, {
    id: null,
    taxPeriod: '',
    taxType: '',
    taxableAmount: 0,
    taxRate: 0,
    taxAmount: 0,
    status: 'Pending',
    declareDate: null,
    paymentDate: null,
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑税务记录'
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (formData.id) {
      await taxRecordApi.update(formData)
      ElMessage.success('更新成功')
    } else {
      await taxRecordApi.create(formData)
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

// 申报
const handleDeclare = async (row) => {
  try {
    await ElMessageBox.confirm('确认要申报该税务记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await taxRecordApi.declare(row.id)
    ElMessage.success('申报成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('申报失败：' + (error.message || error))
    }
  }
}

// 标记已缴纳
const handlePay = async (row) => {
  try {
    await ElMessageBox.confirm('确认已缴纳该税款吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await taxRecordApi.pay(row.id)
    ElMessage.success('操作成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败：' + (error.message || error))
    }
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认要删除该税务记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await taxRecordApi.delete(row.id)
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
    'Declared': 'primary',
    'Paid': 'success'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    'Pending': '待处理',
    'Declared': '已申报',
    'Paid': '已缴纳'
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
</style>


<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>银行对账</span>
          <div>
            <el-button type="success" @click="handleAutoReconcile">自动对账</el-button>
            <el-button type="primary" @click="handleAdd">新增对账</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true">
        <el-form-item label="期间">
          <el-date-picker
            v-model="searchForm.period"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border>
        <el-table-column prop="subjectName" label="科目" width="200" />
        <el-table-column prop="reconciliationDate" label="对账日期" width="120" />
        <el-table-column prop="bookBalance" label="账面余额" width="150" align="right">
          <template #default="{ row }">
            {{ formatAmount(row.bookBalance) }}
          </template>
        </el-table-column>
        <el-table-column prop="bankBalance" label="银行余额" width="150" align="right">
          <template #default="{ row }">
            {{ formatAmount(row.bankBalance) }}
          </template>
        </el-table-column>
        <el-table-column prop="differenceAmount" label="差异金额" width="150" align="right">
          <template #default="{ row }">
            <span :style="{ color: Math.abs(row.differenceAmount || 0) > 0.01 ? 'red' : 'green' }">
              {{ formatAmount(row.differenceAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'Reconciled' ? 'success' : 'warning'">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reconciledBy" label="对账人" width="100" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, prev, pager, next"
        @current-change="loadData"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="对账科目" prop="subjectId">
          <el-select
            v-model="form.subjectId"
            placeholder="请选择科目"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="item in subjectList"
              :key="item.id"
              :label="`${item.subjectCode} - ${item.subjectName}`"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="对账日期" prop="reconciliationDate">
          <el-date-picker
            v-model="form.reconciliationDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="账面余额" prop="bookBalance">
          <el-input-number
            v-model="form.bookBalance"
            :precision="2"
            :step="100"
            disabled
            placeholder="系统自动计算"
            style="width: 100%"
          />
          <el-alert
            title="账面余额由系统根据所有已记账凭证自动计算（关联采购、销售、费用等所有业务）"
            type="info"
            :closable="false"
            style="margin-top: 5px"
          />
        </el-form-item>

        <el-form-item label="银行余额" prop="bankBalance">
          <el-input-number
            v-model="form.bankBalance"
            :precision="2"
            :step="100"
            :min="0"
            placeholder="请输入银行余额"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="差异金额">
          <el-input
            :value="differenceAmount"
            disabled
            style="width: 100%"
          >
            <template #suffix>
              <span :style="{ color: Math.abs(differenceAmount) > 0.01 ? 'red' : 'green' }">
                {{ Math.abs(differenceAmount) > 0.01 ? '有差异' : '已平衡' }}
              </span>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="对账人" prop="reconciledBy">
          <el-input v-model="form.reconciledBy" placeholder="请输入对账人" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 自动对账对话框 -->
    <el-dialog
      v-model="autoReconcileVisible"
      title="自动对账"
      width="500px"
    >
      <el-form label-width="100px">
        <el-form-item label="对账日期">
          <el-date-picker
            v-model="autoReconcileDate"
            type="date"
            placeholder="选择对账日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-alert
          title="自动对账说明"
          type="info"
          :closable="false"
          style="margin-top: 20px"
        >
          <p>系统将自动汇总银行存款和库存现金科目的账面余额</p>
          <p>您需要手动输入银行实际余额进行对比</p>
        </el-alert>
      </el-form>

      <template #footer>
        <el-button @click="autoReconcileVisible = false">取消</el-button>
        <el-button type="primary" @click="executeAutoReconcile">开始对账</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reconciliationApi } from '@/api/accounting'
import { accountSubjectApi } from '@/api/system'

const searchForm = reactive({ period: '' })
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })
const tableData = ref([])
const subjectList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const form = reactive({
  id: null,
  subjectId: null,
  reconciliationDate: '',
  bookBalance: 0,
  bankBalance: 0,
  reconciledBy: 'Admin',
  remark: ''
})

const rules = {
  subjectId: [{ required: true, message: '请选择对账科目', trigger: 'change' }],
  reconciliationDate: [{ required: true, message: '请选择对账日期', trigger: 'change' }],
  bankBalance: [{ required: true, message: '请输入银行余额', trigger: 'blur' }],
  reconciledBy: [{ required: true, message: '请输入对账人', trigger: 'blur' }]
}

// 自动对账相关
const autoReconcileVisible = ref(false)
const autoReconcileDate = ref('')

// 计算差异金额
const differenceAmount = computed(() => {
  const book = form.bookBalance || 0
  const bank = form.bankBalance || 0
  return (book - bank).toFixed(2)
})

// 格式化金额
const formatAmount = (amount) => {
  if (amount === null || amount === undefined) return '0.00'
  return Number(amount).toFixed(2)
}

// 获取状态标签
const getStatusLabel = (status) => {
  const map = {
    'Pending': '待对账',
    'Reconciled': '已平衡',
    'Discrepancy': '有差异'
  }
  return map[status] || status
}

// 加载数据
const loadData = async () => {
  try {
    const { data } = await reconciliationApi.getList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      period: searchForm.period
    })
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
    tableData.value = []
  }
}

// 加载科目列表（只加载货币资金类科目）
const loadSubjects = async () => {
  try {
    const { data } = await accountSubjectApi.getList()
    // 筛选出银行存款、库存现金等货币资金科目
    subjectList.value = (data || []).filter(item => 
      ['1001', '1002', '1012'].includes(item.subjectCode)
    )
  } catch (error) {
    console.error('加载科目列表失败:', error)
    subjectList.value = []
  }
}

// 查询
const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

// 重置
const handleReset = () => {
  searchForm.period = ''
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增对账记录'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  dialogTitle.value = '编辑对账记录'
  try {
    const { data } = await reconciliationApi.getById(row.id)
    Object.assign(form, {
      id: data.id,
      subjectId: data.subjectId,
      reconciliationDate: data.reconciliationDate,
      bookBalance: data.bookBalance,
      bankBalance: data.bankBalance,
      reconciledBy: data.reconciledBy || 'Admin',
      remark: data.remark || ''
    })
    dialogVisible.value = true
  } catch (error) {
    console.error('加载对账记录失败:', error)
    ElMessage.error('加载对账记录失败')
  }
}

// 删除
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条对账记录吗？', '提示', {
      type: 'warning'
    })
    await reconciliationApi.delete(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    const submitData = { ...form }
    
    if (form.id) {
      await reconciliationApi.update(submitData)
      ElMessage.success('更新成功')
    } else {
      await reconciliationApi.create(submitData)
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    }
  }
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.subjectId = null
  form.reconciliationDate = ''
  form.bookBalance = 0
  form.bankBalance = 0
  form.reconciledBy = 'Admin'
  form.remark = ''
  formRef.value?.clearValidate()
}

// 自动对账
const handleAutoReconcile = () => {
  // 默认为今天
  const today = new Date()
  autoReconcileDate.value = today.toISOString().split('T')[0]
  autoReconcileVisible.value = true
}

// 执行自动对账
const executeAutoReconcile = async () => {
  if (!autoReconcileDate.value) {
    ElMessage.warning('请选择对账日期')
    return
  }

  try {
    const { data } = await reconciliationApi.autoReconcile({
      reconciliationDate: autoReconcileDate.value
    })
    
    ElMessage.success(`自动对账完成，生成了 ${data} 条对账记录`)
    autoReconcileVisible.value = false
    loadData()
  } catch (error) {
    console.error('自动对账失败:', error)
    ElMessage.error('自动对账失败')
  }
}

// 查询科目账面余额（基于所有凭证自动计算）
const loadSubjectBalance = async () => {
  if (!form.subjectId || !form.reconciliationDate) {
    return
  }
  
  try {
    const { data } = await reconciliationApi.getSubjectBalance(
      form.subjectId,
      form.reconciliationDate
    )
    form.bookBalance = data || 0
  } catch (error) {
    console.error('查询科目余额失败:', error)
    form.bookBalance = 0
  }
}

// 监听科目和日期变化，自动查询账面余额
watch(
  () => [form.subjectId, form.reconciliationDate],
  () => {
    loadSubjectBalance()
  }
)

onMounted(() => {
  loadData()
  loadSubjects()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-button + .el-button {
  margin-left: 10px;
}
</style>

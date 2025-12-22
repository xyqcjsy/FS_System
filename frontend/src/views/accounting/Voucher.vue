<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>凭证管理</span>
          <el-button type="primary" @click="handleAdd">新增凭证</el-button>
        </div>
      </template>

      <el-form :inline="true">
        <el-form-item label="凭证编号">
          <el-input v-model="searchForm.keyword" placeholder="凭证编号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" clearable style="width: 150px">
            <el-option label="草稿" value="Draft" />
            <el-option label="已记账" value="Posted" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border>
        <el-table-column prop="voucherNo" label="凭证编号" width="150" />
        <el-table-column prop="voucherDate" label="凭证日期" width="120" />
        <el-table-column prop="voucherType" label="类型" width="100">
          <template #default="{ row }">
            {{ row.voucherType === 'Manual' ? '手工' : '自动' }}
          </template>
        </el-table-column>
        <el-table-column prop="totalDebit" label="借方金额" width="120" align="right" />
        <el-table-column prop="totalCredit" label="贷方金额" width="120" align="right" />
        <el-table-column prop="abstract" label="摘要" width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 'Posted' ? 'success' : 'info'">
              {{ row.status === 'Posted' ? '已记账' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)" v-if="row.status === 'Draft'">编辑</el-button>
            <el-button link type="success" @click="handlePost(row.id)" v-if="row.status === 'Draft'">记账</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)" v-if="row.status === 'Draft'">删除</el-button>
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

    <!-- 凭证详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="凭证详情"
      width="900px"
    >
      <el-descriptions :column="2" border v-if="currentVoucher.id">
        <el-descriptions-item label="凭证编号">{{ currentVoucher.voucherNo }}</el-descriptions-item>
        <el-descriptions-item label="凭证日期">{{ currentVoucher.voucherDate }}</el-descriptions-item>
        <el-descriptions-item label="凭证类型">
          {{ currentVoucher.voucherType === 'Manual' ? '手工凭证' : '自动凭证' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentVoucher.status === 'Posted' ? 'success' : 'info'">
            {{ currentVoucher.status === 'Posted' ? '已记账' : '草稿' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="借方总额">
          <span style="color: #409EFF">¥{{ currentVoucher.totalDebit?.toFixed(2) || '0.00' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="贷方总额">
          <span style="color: #67C23A">¥{{ currentVoucher.totalCredit?.toFixed(2) || '0.00' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="摘要" :span="2">{{ currentVoucher.abstractText || currentVoucher.abstract || '-' }}</el-descriptions-item>
        <el-descriptions-item label="制单人">{{ currentVoucher.createdBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentVoucher.createdAt || '-' }}</el-descriptions-item>
        <el-descriptions-item label="记账时间" :span="2">{{ currentVoucher.postedAt || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentVoucher.remark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider>凭证分录</el-divider>

      <el-table :data="currentVoucherEntries" border :summary-method="getSummaries" show-summary>
        <el-table-column prop="lineNumber" label="行号" width="80" align="center" />
        <el-table-column prop="subjectName" label="会计科目" width="250">
          <template #default="{ row }">
            <span v-if="row.subjectCode">{{ row.subjectCode }} - {{ row.subjectName }}</span>
            <span v-else>{{ row.subjectName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="debitAmount" label="借方金额" width="150" align="right">
          <template #default="{ row }">
            <span v-if="row.debitAmount > 0" style="color: #409EFF; font-weight: bold">
              ¥{{ row.debitAmount.toFixed(2) }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="creditAmount" label="贷方金额" width="150" align="right">
          <template #default="{ row }">
            <span v-if="row.creditAmount > 0" style="color: #67C23A; font-weight: bold">
              ¥{{ row.creditAmount.toFixed(2) }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="abstractText" label="摘要" min-width="200">
          <template #default="{ row }">
            {{ row.abstractText || row.abstract || '-' }}
          </template>
        </el-table-column>
      </el-table>

      <el-alert
        v-if="!isBorrect"
        title="警告：该凭证借贷不平衡！"
        type="error"
        :closable="false"
        style="margin-top: 20px"
      />
      <el-alert
        v-else
        title="凭证借贷平衡"
        type="success"
        :closable="false"
        style="margin-top: 20px"
      />

      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button 
          v-if="currentVoucher.status === 'Draft'" 
          type="primary" 
          @click="handleEditFromView"
        >
          编辑
        </el-button>
        <el-button 
          v-if="currentVoucher.status === 'Draft'" 
          type="success" 
          @click="handlePostFromView"
        >
          记账
        </el-button>
      </template>
    </el-dialog>

    <!-- 新增/编辑凭证对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="dialogTitle"
      width="1200px"
      :close-on-click-modal="false"
    >
      <el-form :model="voucherForm" :rules="voucherRules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="凭证日期" prop="voucherDate">
              <el-date-picker
                v-model="voucherForm.voucherDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="凭证摘要" prop="abstractText">
              <el-input v-model="voucherForm.abstractText" placeholder="请输入凭证摘要" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>凭证分录</el-divider>

        <!-- 分录表格 -->
        <el-table :data="voucherForm.entries" border style="width: 100%; margin-bottom: 20px">
          <el-table-column prop="lineNumber" label="行号" width="60" />
          <el-table-column label="会计科目" width="250">
            <template #default="{ row }">
              <el-select
                v-model="row.subjectId"
                placeholder="请选择科目"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="subject in subjectList"
                  :key="subject.id"
                  :label="`${subject.subjectCode} ${subject.subjectName}`"
                  :value="subject.id"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="摘要" min-width="150">
            <template #default="{ row }">
              <el-input v-model="row.abstractText" placeholder="分录摘要" />
            </template>
          </el-table-column>
          <el-table-column label="借方金额" width="150">
            <template #default="{ row }">
              <el-input-number
                v-model="row.debitAmount"
                :precision="2"
                :min="0"
                :controls="false"
                style="width: 100%"
                @change="onDebitChange(row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="贷方金额" width="150">
            <template #default="{ row }">
              <el-input-number
                v-model="row.creditAmount"
                :precision="2"
                :min="0"
                :controls="false"
                style="width: 100%"
                @change="onCreditChange(row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" fixed="right">
            <template #default="{ $index }">
              <el-button
                link
                type="danger"
                @click="removeEntry($index)"
                :disabled="voucherForm.entries.length <= 2"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-button type="primary" plain @click="addEntry" style="margin-bottom: 20px">
          <el-icon><Plus /></el-icon>
          添加分录
        </el-button>

        <!-- 借贷合计 -->
        <el-row :gutter="20" class="summary-row">
          <el-col :span="8">
            <div class="summary-item">
              <span>借方合计：</span>
              <span class="amount debit">¥{{ totalDebit.toFixed(2) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <span>贷方合计：</span>
              <span class="amount credit">¥{{ totalCredit.toFixed(2) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <span>平衡状态：</span>
              <el-tag :type="isBalanced ? 'success' : 'danger'">
                {{ isBalanced ? '✓ 借贷平衡' : '✗ 借贷不平衡' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input
            v-model="voucherForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注（选填）"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitVoucher" :disabled="!isBalanced">
          {{ voucherForm.id ? '更新凭证' : '保存为草稿' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getVoucherList, postVoucher, deleteVoucher, voucherApi } from '@/api/accounting'
import { accountSubjectApi } from '@/api/system'

const searchForm = reactive({ keyword: '', status: '' })
const dateRange = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })
const tableData = ref([])

// 查看对话框
const viewDialogVisible = ref(false)
const currentVoucher = ref({})
const currentVoucherEntries = ref([])

// 计算借贷是否平衡
const isBorrect = computed(() => {
  const totalDebit = currentVoucherEntries.value.reduce((sum, item) => sum + (item.debitAmount || 0), 0)
  const totalCredit = currentVoucherEntries.value.reduce((sum, item) => sum + (item.creditAmount || 0), 0)
  return Math.abs(totalDebit - totalCredit) < 0.01
})

const loadData = async () => {
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    const { data } = await getVoucherList(params)
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

// 新增/编辑对话框
const editDialogVisible = ref(false)
const dialogTitle = ref('新增凭证')
const formRef = ref()

// 科目列表
const subjectList = ref([])

// 表单数据
const voucherForm = reactive({
  id: null, // 凭证ID，null表示新增，有值表示编辑
  voucherNo: null, // 凭证编号
  voucherDate: new Date().toISOString().split('T')[0],
  abstractText: '',
  remark: '',
  entries: [
    { lineNumber: 1, subjectId: null, debitAmount: 0, creditAmount: 0, abstractText: '' },
    { lineNumber: 2, subjectId: null, debitAmount: 0, creditAmount: 0, abstractText: '' }
  ]
})

// 表单验证规则
const voucherRules = {
  voucherDate: [{ required: true, message: '请选择凭证日期', trigger: 'blur' }],
  abstractText: [{ required: true, message: '请输入凭证摘要', trigger: 'blur' }]
}

// 计算借贷合计和是否平衡
const totalDebit = computed(() => {
  return voucherForm.entries.reduce((sum, entry) => sum + Number(entry.debitAmount || 0), 0)
})

const totalCredit = computed(() => {
  return voucherForm.entries.reduce((sum, entry) => sum + Number(entry.creditAmount || 0), 0)
})

const isBalanced = computed(() => {
  return Math.abs(totalDebit.value - totalCredit.value) < 0.01
})

// 加载科目列表
const loadSubjects = async () => {
  try {
    const { data } = await accountSubjectApi.getList()
    subjectList.value = data || []
  } catch (error) {
    console.error('加载科目列表失败:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增凭证'
  // 重置表单
  voucherForm.id = null
  voucherForm.voucherNo = null
  voucherForm.voucherDate = new Date().toISOString().split('T')[0]
  voucherForm.abstractText = ''
  voucherForm.remark = ''
  voucherForm.entries = [
    { lineNumber: 1, subjectId: null, debitAmount: 0, creditAmount: 0, abstractText: '' },
    { lineNumber: 2, subjectId: null, debitAmount: 0, creditAmount: 0, abstractText: '' }
  ]
  editDialogVisible.value = true
  loadSubjects()
}

// 添加分录行
const addEntry = () => {
  const lineNumber = voucherForm.entries.length + 1
  voucherForm.entries.push({
    lineNumber,
    subjectId: null,
    debitAmount: 0,
    creditAmount: 0,
    abstractText: voucherForm.abstractText // 默认使用凭证摘要
  })
}

// 删除分录行
const removeEntry = (index) => {
  if (voucherForm.entries.length <= 2) {
    ElMessage.warning('至少需要保留两条分录')
    return
  }
  voucherForm.entries.splice(index, 1)
  // 重新编号
  voucherForm.entries.forEach((entry, idx) => {
    entry.lineNumber = idx + 1
  })
}

// 获取科目名称
const getSubjectName = (subjectId) => {
  const subject = subjectList.value.find(s => s.id === subjectId)
  return subject ? `${subject.subjectCode} ${subject.subjectName}` : ''
}

// 借方金额变化时自动清空贷方
const onDebitChange = (entry) => {
  if (entry.debitAmount > 0) {
    entry.creditAmount = 0
  }
}

// 贷方金额变化时自动清空借方
const onCreditChange = (entry) => {
  if (entry.creditAmount > 0) {
    entry.debitAmount = 0
  }
}

// 提交凭证
const submitVoucher = async () => {
  try {
    // 验证表单
    await formRef.value.validate()
    
    // 验证分录
    for (let i = 0; i < voucherForm.entries.length; i++) {
      const entry = voucherForm.entries[i]
      if (!entry.subjectId) {
        ElMessage.error(`第 ${i + 1} 行分录未选择科目`)
        return
      }
      if (entry.debitAmount === 0 && entry.creditAmount === 0) {
        ElMessage.error(`第 ${i + 1} 行分录借贷金额不能都为0`)
        return
      }
    }
    
    // 验证借贷平衡
    if (!isBalanced.value) {
      ElMessage.error(`借贷不平衡！借方合计：${totalDebit.value.toFixed(2)}，贷方合计：${totalCredit.value.toFixed(2)}`)
      return
    }
    
    // 构造请求数据
    const requestData = {
      voucher: {
        voucherDate: voucherForm.voucherDate,
        voucherType: 'Manual',
        sourceType: 'Manual',
        totalDebit: totalDebit.value,
        totalCredit: totalCredit.value,
        status: 'Draft',
        abstractText: voucherForm.abstractText,
        remark: voucherForm.remark,
        createdBy: 'Admin'
      },
      entries: voucherForm.entries.map(entry => ({
        id: entry.id, // 保留分录ID（如果有）
        lineNumber: entry.lineNumber,
        subjectId: entry.subjectId,
        debitAmount: Number(entry.debitAmount || 0),
        creditAmount: Number(entry.creditAmount || 0),
        abstractText: entry.abstractText || voucherForm.abstractText
      }))
    }
    
    // 如果是编辑模式，添加ID和voucherNo
    if (voucherForm.id) {
      requestData.voucher.id = voucherForm.id
      requestData.voucher.voucherNo = voucherForm.voucherNo // 保留凭证编号
      await voucherApi.update(requestData)
      ElMessage.success('凭证更新成功')
    } else {
      await voucherApi.create(requestData)
      ElMessage.success('凭证创建成功')
    }
    
    editDialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      console.error(voucherForm.id ? '更新凭证失败:' : '创建凭证失败:', error)
      ElMessage.error((voucherForm.id ? '更新' : '创建') + '凭证失败：' + (error.message || error))
    }
  }
}

// 查看凭证详情
const handleView = async (row) => {
  try {
    // 获取凭证详情
    const voucherRes = await voucherApi.getById(row.id)
    currentVoucher.value = voucherRes.data || {}
    
    // 获取凭证分录
    const entriesRes = await voucherApi.getEntries(row.id)
    currentVoucherEntries.value = entriesRes.data || []
    
    viewDialogVisible.value = true
  } catch (error) {
    console.error('加载凭证详情失败:', error)
    ElMessage.error('加载凭证详情失败')
  }
}

const handleEdit = async (row) => {
  try {
    dialogTitle.value = '编辑凭证'
    
    // 获取凭证详情
    const voucherRes = await voucherApi.getById(row.id)
    const voucher = voucherRes.data || {}
    
    // 获取凭证分录
    const entriesRes = await voucherApi.getEntries(row.id)
    const entries = entriesRes.data || []
    
    // 填充表单数据
    voucherForm.id = voucher.id
    voucherForm.voucherNo = voucher.voucherNo // 保留凭证编号
    voucherForm.voucherDate = voucher.voucherDate
    voucherForm.abstractText = voucher.abstractText || voucher.abstract || ''
    voucherForm.remark = voucher.remark || ''
    voucherForm.entries = entries.map(entry => ({
      id: entry.id, // 保留分录ID
      lineNumber: entry.lineNumber,
      subjectId: entry.subjectId,
      debitAmount: Number(entry.debitAmount || 0),
      creditAmount: Number(entry.creditAmount || 0),
      abstractText: entry.abstractText || entry.abstract || ''
    }))
    
    // 加载科目列表并显示对话框
    await loadSubjects()
    editDialogVisible.value = true
  } catch (error) {
    console.error('加载凭证数据失败:', error)
    ElMessage.error('加载凭证数据失败')
  }
}

// 从查看对话框编辑
const handleEditFromView = async () => {
  viewDialogVisible.value = false
  await handleEdit({ id: currentVoucher.value.id })
}

// 从查看对话框记账
const handlePostFromView = async () => {
  try {
    await ElMessageBox.confirm('确认要记账该凭证吗？记账后将不可修改。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await postVoucher(currentVoucher.value.id)
    ElMessage.success('记账成功')
    viewDialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('记账失败:', error)
      ElMessage.error('记账失败')
    }
  }
}

const handlePost = async (id) => {
  try {
    await ElMessageBox.confirm('确认要记账该凭证吗？记账后将不可修改。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await postVoucher(id)
    ElMessage.success('记账成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('记账失败:', error)
      ElMessage.error('记账失败')
    }
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认要删除该凭证吗？删除后将无法恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await voucherApi.delete(id)
    ElMessage.success('凭证删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除凭证失败:', error)
      ElMessage.error('删除凭证失败：' + (error.message || '未知错误'))
    }
  }
}

// 合计行计算
const getSummaries = (param) => {
  const { columns, data } = param
  const sums = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (index === 2) { // 借方金额列
      const values = data.map(item => Number(item.debitAmount))
      if (!values.every(value => isNaN(value))) {
        const total = values.reduce((prev, curr) => {
          const value = Number(curr)
          if (!isNaN(value)) {
            return prev + curr
          } else {
            return prev
          }
        }, 0)
        sums[index] = `¥${total.toFixed(2)}`
      } else {
        sums[index] = '-'
      }
    } else if (index === 3) { // 贷方金额列
      const values = data.map(item => Number(item.creditAmount))
      if (!values.every(value => isNaN(value))) {
        const total = values.reduce((prev, curr) => {
          const value = Number(curr)
          if (!isNaN(value)) {
            return prev + curr
          } else {
            return prev
          }
        }, 0)
        sums[index] = `¥${total.toFixed(2)}`
      } else {
        sums[index] = '-'
      }
    } else {
      sums[index] = ''
    }
  })
  return sums
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

.el-divider {
  margin: 24px 0;
}

.summary-row {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
}

.amount {
  font-size: 18px;
}

.amount.debit {
  color: #409eff;
}

.amount.credit {
  color: #67c23a;
}

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-input-number .el-input__inner) {
  text-align: right;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
}

:deep(.el-table .el-table__footer) {
  font-weight: 600;
  color: #303133;
}

.app-container {
  padding: 20px;
}
</style>


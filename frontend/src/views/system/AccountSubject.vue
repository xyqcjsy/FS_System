<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>会计科目管理</span>
          <el-button type="primary" @click="handleAdd">新增科目</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="loadData">
        <el-tab-pane label="全部科目" name="all" />
        <el-tab-pane label="资产类" name="Asset" />
        <el-tab-pane label="负债类" name="Liability" />
        <el-tab-pane label="所有者权益类" name="Equity" />
        <el-tab-pane label="收入类" name="Income" />
        <el-tab-pane label="费用类" name="Expense" />
      </el-tabs>

      <el-table :data="tableData" border stripe row-key="id" default-expand-all>
        <el-table-column prop="subjectCode" label="科目编码" width="150" />
        <el-table-column prop="subjectName" label="科目名称" width="200" />
        <el-table-column prop="subjectType" label="科目类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getSubjectTypeColor(row.subjectType)">
              {{ getSubjectTypeName(row.subjectType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="balanceDirection" label="余额方向" width="100">
          <template #default="{ row }">
            {{ row.balanceDirection === 'Debit' ? '借方' : '贷方' }}
          </template>
        </el-table-column>
        <el-table-column prop="level" label="层级" width="80" />
        <el-table-column prop="isLeaf" label="叶子节点" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isLeaf ? 'success' : 'info'">
              {{ row.isLeaf ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled ? 'success' : 'danger'">
              {{ row.isEnabled ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="科目编码" prop="subjectCode">
          <el-input v-model="formData.subjectCode" placeholder="请输入科目编码，如：1001" />
        </el-form-item>
        <el-form-item label="科目名称" prop="subjectName">
          <el-input v-model="formData.subjectName" placeholder="请输入科目名称" />
        </el-form-item>
        <el-form-item label="科目类型" prop="subjectType">
          <el-select v-model="formData.subjectType" placeholder="请选择科目类型">
            <el-option label="资产类" value="Asset" />
            <el-option label="负债类" value="Liability" />
            <el-option label="所有者权益类" value="Equity" />
            <el-option label="收入类" value="Income" />
            <el-option label="费用类" value="Expense" />
          </el-select>
        </el-form-item>
        <el-form-item label="余额方向" prop="balanceDirection">
          <el-radio-group v-model="formData.balanceDirection">
            <el-radio label="Debit">借方</el-radio>
            <el-radio label="Credit">贷方</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="父科目" prop="parentId">
          <el-input-number v-model="formData.parentId" :min="0" controls-position="right" style="width: 100%;" />
          <span style="font-size: 12px; color: #999;">（0表示顶级科目）</span>
        </el-form-item>
        <el-form-item label="科目层级" prop="level">
          <el-input-number v-model="formData.level" :min="1" controls-position="right" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="是否叶子节点" prop="isLeaf">
          <el-switch v-model="formData.isLeaf" />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="说明" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入科目说明" />
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { accountSubjectApi } from '@/api/system'

const activeTab = ref('all')
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const formData = ref({
  id: null,
  subjectCode: '',
  subjectName: '',
  subjectType: '',
  parentId: 0,
  level: 1,
  isLeaf: true,
  balanceDirection: 'Debit',
  isEnabled: true,
  description: ''
})

const rules = {
  subjectCode: [{ required: true, message: '请输入科目编码', trigger: 'blur' }],
  subjectName: [{ required: true, message: '请输入科目名称', trigger: 'blur' }],
  subjectType: [{ required: true, message: '请选择科目类型', trigger: 'change' }],
  balanceDirection: [{ required: true, message: '请选择余额方向', trigger: 'change' }]
}

const getSubjectTypeName = (type) => {
  const map = {
    'Asset': '资产类',
    'Liability': '负债类',
    'Equity': '所有者权益类',
    'Income': '收入类',
    'Expense': '费用类'
  }
  return map[type] || type
}

const getSubjectTypeColor = (type) => {
  const colorMap = {
    'Asset': 'success',
    'Liability': 'warning',
    'Equity': 'info',
    'Income': 'primary',
    'Expense': 'danger'
  }
  return colorMap[type] || ''
}

const loadData = async () => {
  try {
    let res
    if (activeTab.value === 'all') {
      res = await accountSubjectApi.getList()
    } else {
      res = await accountSubjectApi.getByType(activeTab.value)
    }
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载数据失败：', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增科目'
  formData.value = {
    id: null,
    subjectCode: '',
    subjectName: '',
    subjectType: activeTab.value !== 'all' ? activeTab.value : '',
    parentId: 0,
    level: 1,
    isLeaf: true,
    balanceDirection: 'Debit',
    isEnabled: true,
    description: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑科目'
  formData.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  
  try {
    if (formData.value.id) {
      await accountSubjectApi.update(formData.value)
      ElMessage.success('科目更新成功')
    } else {
      await accountSubjectApi.create(formData.value)
      ElMessage.success('科目创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败：', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该科目吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await accountSubjectApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败：', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>


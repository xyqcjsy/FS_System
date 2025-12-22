<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>企业信息管理</span>
          <el-button type="primary" @click="handleAdd">新增企业</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="companyName" label="企业名称" width="200" />
        <el-table-column prop="companyScale" label="企业规模" width="120" />
        <el-table-column prop="registeredCapital" label="注册资金" width="150">
          <template #default="{ row }">
            {{ row.registeredCapital ? `¥${row.registeredCapital.toLocaleString()}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="legalRepresentative" label="法人代表" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="150" />
        <el-table-column prop="address" label="企业地址" min-width="200" show-overflow-tooltip />
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
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="formData.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="企业规模" prop="companyScale">
          <el-select v-model="formData.companyScale" placeholder="请选择企业规模">
            <el-option label="小型" value="小型" />
            <el-option label="中型" value="中型" />
            <el-option label="大型" value="大型" />
          </el-select>
        </el-form-item>
        <el-form-item label="注册资金" prop="registeredCapital">
          <el-input-number v-model="formData.registeredCapital" :min="0" :step="10000" controls-position="right" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="营业执照号" prop="businessLicense">
          <el-input v-model="formData.businessLicense" placeholder="请输入营业执照号" />
        </el-form-item>
        <el-form-item label="法人代表" prop="legalRepresentative">
          <el-input v-model="formData.legalRepresentative" placeholder="请输入法人代表" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="企业地址" prop="address">
          <el-input v-model="formData.address" type="textarea" :rows="2" placeholder="请输入企业地址" />
        </el-form-item>
        <el-form-item label="税号" prop="taxNumber">
          <el-input v-model="formData.taxNumber" placeholder="请输入税号" />
        </el-form-item>
        <el-form-item label="会计年度起始月份" prop="fiscalYearStart">
          <el-input-number v-model="formData.fiscalYearStart" :min="1" :max="12" controls-position="right" style="width: 100%;" />
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
import { companyApi } from '@/api/system'

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const formData = ref({
  id: null,
  companyName: '',
  companyScale: '',
  registeredCapital: null,
  businessLicense: '',
  legalRepresentative: '',
  contactPhone: '',
  address: '',
  taxNumber: '',
  fiscalYearStart: 1
})

const rules = {
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  companyScale: [{ required: true, message: '请选择企业规模', trigger: 'change' }]
}

const loadData = async () => {
  try {
    const res = await companyApi.getList()
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载数据失败：', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增企业'
  formData.value = {
    id: null,
    companyName: '',
    companyScale: '',
    registeredCapital: null,
    businessLicense: '',
    legalRepresentative: '',
    contactPhone: '',
    address: '',
    taxNumber: '',
    fiscalYearStart: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑企业'
  formData.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  
  try {
    if (formData.value.id) {
      await companyApi.update(formData.value)
      ElMessage.success('企业信息更新成功')
    } else {
      await companyApi.create(formData.value)
      ElMessage.success('企业信息创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败：', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该企业吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await companyApi.delete(row.id)
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


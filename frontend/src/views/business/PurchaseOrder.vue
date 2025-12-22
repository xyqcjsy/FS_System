<template>
  <div class="app-container business-container">
    <el-card class="header-card">
      <template #header>
        <div class="card-header">
          <span>
            <el-icon><ShoppingBag /></el-icon>
            采购订单管理
          </span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增采购订单
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="订单编号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="草稿" value="Draft" />
            <el-option label="已确认" value="Confirmed" />
            <el-option label="已收货" value="Received" />
            <el-option label="已付款" value="Paid" />
            <el-option label="已过账" value="Posted" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" border>
        <el-table-column prop="orderNo" label="订单编号" width="150" />
        <el-table-column prop="supplierName" label="供应商" width="200" />
        <el-table-column prop="orderDate" label="订单日期" width="120" />
        <el-table-column prop="finalAmount" label="最终金额" width="120" align="right">
          <template #default="{ row }">
            ¥{{ row.finalAmount?.toFixed(2) || '0.00' }}
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
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)" v-if="row.status === 'Draft'">编辑</el-button>
            <el-button link type="success" @click="handleConfirm(row.id)" v-if="row.status === 'Draft'">确认</el-button>
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
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="form.orderNo" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="form.supplierId" placeholder="请选择供应商" style="width: 100%">
                <el-option
                  v-for="item in supplierList"
                  :key="item.id"
                  :label="item.supplierName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单日期" prop="orderDate">
              <el-date-picker
                v-model="form.orderDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方式">
              <el-input v-model="form.paymentMethod" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 订单明细 -->
        <el-divider content-position="left">订单明细</el-divider>
        <el-button type="primary" size="small" @click="addItem" style="margin-bottom: 10px">添加明细</el-button>
        
        <el-table :data="form.items" border style="margin-bottom: 20px" max-height="400">
          <el-table-column label="商品名称" width="200">
            <template #default="{ row }">
              <el-input v-model="row.itemName" placeholder="请输入" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="规格" width="150">
            <template #default="{ row }">
              <el-input v-model="row.specification" placeholder="规格" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80">
            <template #default="{ row }">
              <el-input v-model="row.unit" placeholder="单位" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="数量" width="100">
            <template #default="{ row }">
              <el-input-number v-model="row.quantity" :min="0" :precision="2" @change="calculateItemAmount(row)" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template #default="{ row }">
              <el-input-number v-model="row.unitPrice" :min="0" :precision="2" @change="calculateItemAmount(row)" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="金额" width="120">
            <template #default="{ row }">
              {{ row.amount?.toFixed(2) || '0.00' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ $index }">
              <el-button link type="danger" @click="removeItem($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="税额">
              <el-input-number v-model="form.taxAmount" :precision="2" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="折扣金额">
              <el-input-number v-model="form.discountAmount" :precision="2" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最终金额">
              <el-input-number v-model="form.finalAmount" :precision="2" readonly style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="采购订单详情" width="900px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ viewData.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ viewData.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="订单日期">{{ viewData.orderDate }}</el-descriptions-item>
        <el-descriptions-item label="付款方式">{{ viewData.paymentMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="订单总额">¥{{ viewData.totalAmount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="税额">¥{{ viewData.taxAmount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="折扣金额">¥{{ viewData.discountAmount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="最终金额">¥{{ viewData.finalAmount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.status)">{{ getStatusLabel(viewData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否过账">
          <el-tag :type="viewData.isPosted ? 'success' : 'info'">
            {{ viewData.isPosted ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="付款日期">{{ viewData.paymentDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="过账时间">{{ viewData.postedAt || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">订单明细</el-divider>
      <el-table :data="viewData.items" border>
        <el-table-column prop="itemName" label="商品/服务名称" />
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="quantity" label="数量" width="100" align="right">
          <template #default="{ row }">{{ row.quantity }}</template>
        </el-table-column>
        <el-table-column prop="unitPrice" label="单价" width="120" align="right">
          <template #default="{ row }">¥{{ row.unitPrice?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="{ row }">¥{{ row.amount?.toFixed(2) }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPurchaseOrderList, getPurchaseOrderDetail, addPurchaseOrder, updatePurchaseOrder, deletePurchaseOrder, confirmPurchaseOrder } from '@/api/business'
import { getSupplierList } from '@/api/system'

const searchForm = reactive({
  keyword: '',
  status: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const supplierList = ref([])
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('新增采购订单')
const formRef = ref()
const viewData = ref({})

const form = reactive({
  id: null,
  orderNo: '',
  supplierId: null,
  orderDate: new Date().toISOString().split('T')[0],
  totalAmount: 0,
  taxAmount: 0,
  discountAmount: 0,
  finalAmount: 0,
  status: 'Draft',
  paymentMethod: '银行转账',
  remark: '',
  items: []
})

const rules = {
  orderNo: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
  supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
  orderDate: [{ required: true, message: '请选择订单日期', trigger: 'change' }]
}

// 监听items变化，自动计算总金额
watch(() => form.items, () => {
  form.totalAmount = form.items.reduce((sum, item) => sum + (Number(item.amount) || 0), 0)
  form.finalAmount = form.totalAmount + (form.taxAmount || 0) - (form.discountAmount || 0)
}, { deep: true })

watch([() => form.taxAmount, () => form.discountAmount], () => {
  form.finalAmount = form.totalAmount + (form.taxAmount || 0) - (form.discountAmount || 0)
})

const getStatusType = (status) => {
  const map = {
    'Draft': 'info',
    'Confirmed': 'warning',
    'Received': 'primary',
    'Paid': 'success',
    'Posted': 'success',
    'Cancelled': 'danger'
  }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = {
    'Draft': '草稿',
    'Confirmed': '已确认',
    'Received': '已收货',
    'Paid': '已付款',
    'Posted': '已过账',
    'Cancelled': '已取消'
  }
  return map[status] || status
}

const loadData = async () => {
  try {
    const { data } = await getPurchaseOrderList()
    // 后端返回简单数组，前端进行过滤和分页
    let filteredData = data || []
    
    // 关键词搜索过滤
    if (searchForm.keyword) {
      const keyword = searchForm.keyword.toLowerCase()
      filteredData = filteredData.filter(item => 
        (item.orderNo && item.orderNo.toLowerCase().includes(keyword))
      )
    }
    
    // 状态过滤
    if (searchForm.status) {
      filteredData = filteredData.filter(item => item.status === searchForm.status)
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

const loadSuppliers = async () => {
  try {
    const { data } = await getSupplierList()
    supplierList.value = data || []
  } catch (error) {
    console.error('加载供应商列表失败:', error)
    supplierList.value = []
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
  dialogTitle.value = '新增采购订单'
  resetForm()
  dialogVisible.value = true
}

const handleView = async (row) => {
  try {
    const { data } = await getPurchaseOrderDetail(row.id)
    viewData.value = data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('加载订单详情失败:', error)
    ElMessage.error('加载订单详情失败')
  }
}

const handleEdit = async (row) => {
  try {
    const { data } = await getPurchaseOrderDetail(row.id)
    dialogTitle.value = '编辑采购订单'
    // 后端返回的order对象已经包含items
    Object.assign(form, {
      ...data,
      items: data.items || []
    })
    dialogVisible.value = true
  } catch (error) {
    console.error('加载订单详情失败:', error)
    ElMessage.error('加载订单详情失败')
  }
}

const handleConfirm = async (id) => {
  try {
    await ElMessageBox.confirm('确定要确认该订单吗？', '提示', { type: 'warning' })
    await confirmPurchaseOrder(id)
    ElMessage.success('确认成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('确认失败:', error)
      ElMessage.error('确认失败')
    }
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该订单吗？', '提示', { type: 'warning' })
    await deletePurchaseOrder(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (form.items.length === 0) {
      ElMessage.warning('请至少添加一条明细')
      return
    }
    
    // 构造后端期望的数据格式
    const requestData = {
      order: {
        id: form.id,
        orderNo: form.orderNo,
        supplierId: form.supplierId,
        orderDate: form.orderDate,
        totalAmount: form.totalAmount,
        taxAmount: form.taxAmount,
        discountAmount: form.discountAmount,
        finalAmount: form.finalAmount,
        paymentMethod: form.paymentMethod,
        remark: form.remark
      },
      items: form.items
    }
    
    if (form.id) {
      await updatePurchaseOrder(requestData)
      ElMessage.success('更新成功')
    } else {
      await addPurchaseOrder(requestData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
    if (error !== false) {
      ElMessage.error('操作失败')
    }
  }
}

const addItem = () => {
  form.items.push({
    itemName: '',
    specification: '',
    unit: '个',
    quantity: 1,
    unitPrice: 0,
    amount: 0,
    taxRate: 0
  })
}

const removeItem = (index) => {
  form.items.splice(index, 1)
}

const calculateItemAmount = (row) => {
  row.amount = (row.quantity || 0) * (row.unitPrice || 0)
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    orderNo: 'PO' + Date.now(),
    supplierId: null,
    orderDate: new Date().toISOString().split('T')[0],
    totalAmount: 0,
    taxAmount: 0,
    discountAmount: 0,
    finalAmount: 0,
    status: 'Draft',
    paymentMethod: '银行转账',
    remark: '',
    items: []
  })
}

onMounted(() => {
  loadData()
  loadSuppliers()
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


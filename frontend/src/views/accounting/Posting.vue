<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>智能过账</span>
          <el-button type="primary" @click="handleBatchPost" v-if="selectedRows.length > 0">
            批量过账 ({{ selectedRows.length }})
          </el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="采购过账" name="purchase">
          <el-table 
            :data="purchaseList" 
            border 
            @selection-change="handleSelectionChange"
            ref="purchaseTableRef"
          >
            <el-table-column type="selection" width="55" :selectable="checkSelectable" />
            <el-table-column prop="orderNo" label="订单编号" width="150" />
            <el-table-column prop="supplierName" label="供应商" width="200" />
            <el-table-column prop="orderDate" label="订单日期" width="120" />
            <el-table-column prop="finalAmount" label="金额" width="120" align="right">
              <template #default="{ row }">
                ¥{{ row.finalAmount?.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="isPosted" label="过账状态" width="120">
              <template #default="{ row }">
                <el-tag :type="row.isPosted ? 'success' : 'warning'">
                  {{ row.isPosted ? '已过账' : '未过账' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="{ row }">
                <el-button
                  link
                  type="primary"
                  @click="handlePost('purchase', row.id)"
                  :disabled="row.isPosted"
                >
                  过账
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="销售过账" name="sales">
          <el-table 
            :data="salesList" 
            border 
            @selection-change="handleSelectionChange"
            ref="salesTableRef"
          >
            <el-table-column type="selection" width="55" :selectable="checkSelectable" />
            <el-table-column prop="invoiceNo" label="发票编号" width="150" />
            <el-table-column prop="customerName" label="客户" width="200" />
            <el-table-column prop="invoiceDate" label="开票日期" width="120" />
            <el-table-column prop="finalAmount" label="金额" width="120" align="right">
              <template #default="{ row }">
                ¥{{ row.finalAmount?.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="isPosted" label="过账状态" width="120">
              <template #default="{ row }">
                <el-tag :type="row.isPosted ? 'success' : 'warning'">
                  {{ row.isPosted ? '已过账' : '未过账' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="{ row }">
                <el-button
                  link
                  type="primary"
                  @click="handlePost('sales', row.id)"
                  :disabled="row.isPosted"
                >
                  过账
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="费用过账" name="expense">
          <el-table 
            :data="expenseList" 
            border 
            @selection-change="handleSelectionChange"
            ref="expenseTableRef"
          >
            <el-table-column type="selection" width="55" :selectable="checkSelectable" />
            <el-table-column prop="claimNo" label="报销单号" width="150" />
            <el-table-column prop="employeeName" label="报销人" width="150" />
            <el-table-column prop="claimDate" label="报销日期" width="120" />
            <el-table-column prop="totalAmount" label="金额" width="120" align="right">
              <template #default="{ row }">
                ¥{{ row.totalAmount?.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="isPosted" label="过账状态" width="120">
              <template #default="{ row }">
                <el-tag :type="row.isPosted ? 'success' : 'warning'">
                  {{ row.isPosted ? '已过账' : '未过账' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="{ row }">
                <el-button
                  link
                  type="primary"
                  @click="handlePost('expense', row.id)"
                  :disabled="row.isPosted"
                >
                  过账
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPurchaseOrderList, getSalesInvoiceList, getExpenseClaimList } from '@/api/business'
import { postingApi } from '@/api/accounting'

const activeTab = ref('purchase')
const purchaseList = ref([])
const salesList = ref([])
const expenseList = ref([])
const selectedRows = ref([])
const purchaseTableRef = ref()
const salesTableRef = ref()
const expenseTableRef = ref()

// 加载采购订单列表
const loadPurchaseList = async () => {
  try {
    const { data } = await getPurchaseOrderList()
    // 处理分页数据
    purchaseList.value = data.list || data || []
  } catch (error) {
    console.error('加载采购订单失败:', error)
    ElMessage.error('加载采购订单失败')
    purchaseList.value = []
  }
}

// 加载销售发票列表
const loadSalesList = async () => {
  try {
    const { data } = await getSalesInvoiceList()
    // 处理分页数据
    salesList.value = data.list || data || []
  } catch (error) {
    console.error('加载销售发票失败:', error)
    ElMessage.error('加载销售发票失败')
    salesList.value = []
  }
}

// 加载费用报销列表
const loadExpenseList = async () => {
  try {
    const { data } = await getExpenseClaimList()
    // 处理分页数据
    expenseList.value = data.list || data || []
  } catch (error) {
    console.error('加载费用报销失败:', error)
    ElMessage.error('加载费用报销失败')
    expenseList.value = []
  }
}

// 单个过账
const handlePost = async (type, id) => {
  try {
    await ElMessageBox.confirm(
      '确认要执行过账操作吗？过账后将自动生成会计凭证。',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    let result
    if (type === 'purchase') {
      result = await postingApi.postPurchaseOrder(id)
    } else if (type === 'sales') {
      result = await postingApi.postSalesInvoice(id)
    } else if (type === 'expense') {
      result = await postingApi.postExpenseClaim(id)
    }

    ElMessage.success('过账成功，生成凭证ID：' + result.data)
    
    // 重新加载对应列表
    if (type === 'purchase') await loadPurchaseList()
    else if (type === 'sales') await loadSalesList()
    else if (type === 'expense') await loadExpenseList()
    
    // 清空选择
    selectedRows.value = []
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('过账失败：' + (error.message || error))
    }
  }
}

// 批量过账
const handleBatchPost = async () => {
  try {
    await ElMessageBox.confirm(
      `确认要批量过账选中的 ${selectedRows.value.length} 条记录吗？`,
      '批量过账',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const ids = selectedRows.value.map(row => row.id)
    let result

    if (activeTab.value === 'purchase') {
      result = await postingApi.batchPostPurchaseOrders(ids)
      await loadPurchaseList()
    } else if (activeTab.value === 'sales') {
      result = await postingApi.batchPostSalesInvoices(ids)
      await loadSalesList()
    } else if (activeTab.value === 'expense') {
      result = await postingApi.batchPostExpenseClaims(ids)
      await loadExpenseList()
    }

    ElMessage.success(`批量过账成功，共生成 ${result.data.length} 张凭证`)
    selectedRows.value = []
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量过账失败：' + (error.message || error))
    }
  }
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 标签页切换
const handleTabChange = () => {
  selectedRows.value = []
}

// 检查是否可选择
const checkSelectable = (row) => {
  return !row.isPosted
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    'Draft': 'info',
    'Confirmed': 'success',
    'Approved': 'success',
    'Delivered': 'success',
    'Received': 'success',
    'Paid': 'success',
    'Posted': 'success',
    'Cancelled': 'danger',
    'Rejected': 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    'Draft': '草稿',
    'Confirmed': '已确认',
    'Approved': '已审批',
    'Delivered': '已发货',
    'Received': '已收货',
    'Paid': '已付款',
    'Posted': '已过账',
    'Cancelled': '已取消',
    'Rejected': '已拒绝'
  }
  return textMap[status] || status
}

onMounted(() => {
  loadPurchaseList()
  loadSalesList()
  loadExpenseList()
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
</style>


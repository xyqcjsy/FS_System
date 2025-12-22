<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>资产负债表</span>
          <div>
            <el-date-picker
              v-model="endDate"
              type="date"
              placeholder="选择报表日期"
              value-format="YYYY-MM-DD"
              style="margin-right: 10px"
            />
            <el-button type="primary" @click="loadReport">生成报表</el-button>
            <el-button @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <div v-if="reportData" class="report-container">
        <div class="report-header">
          <div class="header-content">
            <h2 class="report-title">资产负债表</h2>
            <div class="report-info">
              <span class="report-date">报表日期：{{ reportData.报表日期 }}</span>
              <el-tag :type="reportData.平衡校验 ? 'success' : 'danger'" size="small">
                {{ reportData.平衡校验 ? '平衡' : '不平衡' }}
              </el-tag>
            </div>
          </div>
        </div>

        <el-row :gutter="20">
          <el-col :span="12">
            <div class="section-title">资产</div>
            <el-table :data="assetItems" border stripe :show-header="false" size="small">
              <el-table-column prop="name" label="项目">
                <template #default="{ row }">
                  <span :class="{ 'total-item': row.isTotal }">{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="金额" align="right" width="150">
                <template #default="{ row }">
                  <span :class="{ 'total-item': row.isTotal }">
                    {{ formatMoney(row.value) }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <el-col :span="12">
            <div class="section-title">负债及所有者权益</div>
            <el-table :data="liabilityAndEquityItems" border stripe :show-header="false" size="small">
              <el-table-column prop="name" label="项目">
                <template #default="{ row }">
                  <span :class="{ 'total-item': row.isTotal }">{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="金额" align="right" width="150">
                <template #default="{ row }">
                  <span :class="{ 'total-item': row.isTotal }">
                    {{ formatMoney(row.value) }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </div>

      <el-empty v-else description="请选择日期并生成报表" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBalanceSheet } from '@/api/report'

const endDate = ref('2025-02-28') // 默认设置为测试数据的日期
const reportData = ref(null)

const assetItems = computed(() => {
  if (!reportData.value) return []
  const data = reportData.value.资产
  return [
    { name: '流动资产', value: data.流动资产合计, isTotal: true },
    ...Object.entries(data.流动资产 || {}).map(([k, v]) => ({ name: `  ${k}`, value: v })),
    { name: '非流动资产', value: data.非流动资产合计, isTotal: true },
    ...Object.entries(data.非流动资产 || {}).map(([k, v]) => ({ name: `  ${k}`, value: v })),
    { name: '资产总计', value: data.资产总计, isTotal: true }
  ]
})

const liabilityAndEquityItems = computed(() => {
  if (!reportData.value) return []
  const liability = reportData.value.负债
  const equity = reportData.value.所有者权益
  return [
    { name: '流动负债', value: liability.流动负债合计, isTotal: true },
    ...Object.entries(liability.流动负债 || {}).map(([k, v]) => ({ name: `  ${k}`, value: v })),
    { name: '非流动负债', value: liability.非流动负债合计, isTotal: true },
    ...Object.entries(liability.非流动负债 || {}).map(([k, v]) => ({ name: `  ${k}`, value: v })),
    { name: '负债合计', value: liability.负债合计, isTotal: true },
    { name: ' ', value: '' },
    { name: '所有者权益', value: equity.所有者权益合计, isTotal: true },
    ...Object.entries(equity || {}).filter(([k]) => k !== '所有者权益合计').map(([k, v]) => ({ name: `  ${k}`, value: v })),
    { name: '负债及所有者权益总计', value: reportData.value.负债及所有者权益总计, isTotal: true }
  ]
})

const loadReport = async () => {
  try {
    const { data } = await getBalanceSheet({ startDate: null, endDate: endDate.value })
    reportData.value = data
    ElMessage.success('报表生成成功')
  } catch (error) {
    ElMessage.error('报表生成失败')
  }
}

const handleExport = () => {
  if (!endDate.value) {
    ElMessage.warning('请先选择日期并生成报表')
    return
  }
  
  const url = `/api/report/balance-sheet/export?endDate=${endDate.value}`
  const link = document.createElement('a')
  link.href = url
  link.download = `资产负债表_${endDate.value}.xlsx`
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  ElMessage.success('开始导出...')
}

const formatMoney = (value) => {
  if (value === '' || value === undefined || value === null) return ''
  return Number(value).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 页面加载时自动生成报表
onMounted(() => {
  loadReport()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.report-container {
  padding: 20px;
  background: #fff;
}

.report-header {
  border-bottom: 2px solid #303133;
  padding-bottom: 15px;
  margin-bottom: 25px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.report-title {
  font-size: 22px;
  font-weight: bold;
  margin: 0;
  color: #303133;
  letter-spacing: 2px;
}

.report-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.report-date {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.section-title {
  background: #f5f7fa;
  color: #303133;
  padding: 10px 15px;
  margin-bottom: 0;
  border-left: 4px solid #409eff;
  font-weight: bold;
  font-size: 15px;
}

.total-item {
  font-weight: bold;
  color: #303133;
  font-size: 14px;
}
</style>


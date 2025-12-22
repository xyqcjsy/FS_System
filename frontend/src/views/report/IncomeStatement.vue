<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>利润表</span>
          <div>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
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
            <h2 class="report-title">利润表</h2>
            <div class="report-info">
              <span class="report-date">{{ reportData.报表期间 }}</span>
              <el-tag :type="reportData.净利润 >= 0 ? 'success' : 'danger'" size="small">
                净利润：{{ formatMoney(reportData.净利润) }}
              </el-tag>
            </div>
          </div>
        </div>

        <el-table :data="reportItems" border stripe style="width: 100%" size="small">
          <el-table-column prop="name" label="项目">
            <template #default="{ row }">
              <span :class="{ 'total-row': row.isTotal, 'highlight-row': row.isHighlight }">
                {{ row.name }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="value" label="金额" align="right" width="200">
            <template #default="{ row }">
              <span :class="{ 'total-row': row.isTotal, 'highlight-row': row.isHighlight }">
                {{ formatMoney(row.value) }}
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-empty v-else description="请选择日期范围并生成报表" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getIncomeStatement } from '@/api/report'

const dateRange = ref(['2025-01-01', '2025-02-28']) // 默认设置为测试数据的日期范围
const reportData = ref(null)

const reportItems = computed(() => {
  if (!reportData.value) return []
  return [
    { name: '一、营业收入', value: reportData.value.营业收入, isTotal: false, isHighlight: true },
    { name: '减：营业成本', value: reportData.value.营业成本, isTotal: false },
    { name: '    销售费用', value: reportData.value.销售费用, isTotal: false },
    { name: '    管理费用', value: reportData.value.管理费用, isTotal: false },
    { name: '    财务费用', value: reportData.value.财务费用, isTotal: false },
    { name: '二、营业利润', value: reportData.value.营业利润, isTotal: true },
    { name: '加：营业外收入', value: reportData.value.营业外收入, isTotal: false },
    { name: '减：营业外支出', value: reportData.value.营业外支出, isTotal: false },
    { name: '三、利润总额', value: reportData.value.利润总额, isTotal: true },
    { name: '减：所得税费用', value: reportData.value.所得税费用, isTotal: false },
    { name: '四、净利润', value: reportData.value.净利润, isTotal: true, isHighlight: true }
  ]
})

const loadReport = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) {
    ElMessage.warning('请选择日期范围')
    return
  }
  try {
    const { data } = await getIncomeStatement({
      startDate: dateRange.value[0],
      endDate: dateRange.value[1]
    })
    reportData.value = data
    ElMessage.success('报表生成成功')
  } catch (error) {
    ElMessage.error('报表生成失败')
  }
}

const handleExport = () => {
  if (!dateRange.value || dateRange.value.length !== 2) {
    ElMessage.warning('请先选择日期范围并生成报表')
    return
  }
  
  const url = `/api/report/income-statement/export?startDate=${dateRange.value[0]}&endDate=${dateRange.value[1]}`
  const link = document.createElement('a')
  link.href = url
  link.download = `利润表_${dateRange.value[0]}至${dateRange.value[1]}.xlsx`
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

.total-row {
  font-weight: bold;
  color: #303133;
  font-size: 14px;
}

.highlight-row {
  font-weight: bold;
  color: #409eff;
  font-size: 14px;
}
</style>


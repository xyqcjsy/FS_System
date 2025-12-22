<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>现金流量表</span>
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
            <h2 class="report-title">现金流量表</h2>
            <div class="report-info">
              <span class="report-date">{{ reportData.报表期间 }}</span>
              <el-tag :type="reportData.现金及现金等价物净增加额 >= 0 ? 'success' : 'warning'" size="small">
                现金净增：{{ formatMoney(reportData.现金及现金等价物净增加额) }}
              </el-tag>
            </div>
          </div>
        </div>

        <el-row :gutter="15">
          <!-- 一、经营活动 -->
          <el-col :span="8">
            <div class="section-header">一、经营活动</div>
            <el-table :data="operatingItems" border stripe :show-header="false" size="small">
              <el-table-column prop="name" label="项目">
                <template #default="{ row }">
                  <span :class="{ 'total-row': row.isTotal }">{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="金额" align="right" width="120">
                <template #default="{ row }">
                  <span :class="{ 'total-row': row.isTotal }">{{ formatMoney(row.value) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <!-- 二、投资活动 -->
          <el-col :span="8">
            <div class="section-header">二、投资活动</div>
            <el-table :data="investingItems" border stripe :show-header="false" size="small">
              <el-table-column prop="name" label="项目">
                <template #default="{ row }">
                  <span :class="{ 'total-row': row.isTotal }">{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="金额" align="right" width="120">
                <template #default="{ row }">
                  <span :class="{ 'total-row': row.isTotal }">{{ formatMoney(row.value) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <!-- 三、筹资活动 -->
          <el-col :span="8">
            <div class="section-header">三、筹资活动</div>
            <el-table :data="financingItems" border stripe :show-header="false" size="small">
              <el-table-column prop="name" label="项目">
                <template #default="{ row }">
                  <span :class="{ 'total-row': row.isTotal }">{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="金额" align="right" width="120">
                <template #default="{ row }">
                  <span :class="{ 'total-row': row.isTotal }">{{ formatMoney(row.value) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>

        <!-- 汇总 -->
        <div class="summary-section">
          <div class="section-header">四、现金及现金等价物净增加情况</div>
          <el-table :data="summaryItems" border stripe :show-header="false" size="small" style="margin-top: 10px;">
            <el-table-column prop="name" label="项目">
              <template #default="{ row }">
                <span :class="{ 'total-row': row.isTotal }">{{ row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="value" label="金额" align="right" width="200">
              <template #default="{ row }">
                <span :class="{ 'total-row': row.isTotal }">{{ formatMoney(row.value) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <el-empty v-else description="请选择日期范围并生成报表" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCashFlow } from '@/api/report'

const dateRange = ref(['2025-01-01', '2025-02-28']) // 默认设置为测试数据的日期范围
const reportData = ref(null)

// 经营活动现金流量项目
const operatingItems = computed(() => {
  if (!reportData.value || !reportData.value.经营活动) return []
  const data = reportData.value.经营活动
  return [
    { name: '销售商品、提供劳务收到的现金', value: data['销售商品、提供劳务收到的现金'] || 0 },
    { name: '购买商品、接受劳务支付的现金', value: -(data['购买商品、接受劳务支付的现金'] || 0) },
    { name: '支付给职工以及为职工支付的现金', value: -(data['支付给职工以及为职工支付的现金'] || 0) },
    { name: '支付的各项税费', value: -(data['支付的各项税费'] || 0) },
    { name: '支付的其他与经营活动有关的现金', value: -(data['支付的其他与经营活动有关的现金'] || 0) },
    { name: '经营活动产生的现金流量净额', value: data['经营活动产生的现金流量净额'] || 0, isTotal: true }
  ]
})

// 投资活动现金流量项目
const investingItems = computed(() => {
  if (!reportData.value || !reportData.value.投资活动) return []
  const data = reportData.value.投资活动
  return [
    { name: '购建固定资产、无形资产和其他长期资产支付的现金', value: -(data['购建固定资产、无形资产和其他长期资产支付的现金'] || 0) },
    { name: '投资支付的现金', value: -(data['投资支付的现金'] || 0) },
    { name: '投资活动产生的现金流量净额', value: data['投资活动产生的现金流量净额'] || 0, isTotal: true }
  ]
})

// 筹资活动现金流量项目
const financingItems = computed(() => {
  if (!reportData.value || !reportData.value.筹资活动) return []
  const data = reportData.value.筹资活动
  return [
    { name: '吸收投资收到的现金', value: data['吸收投资收到的现金'] || 0 },
    { name: '取得借款收到的现金', value: data['取得借款收到的现金'] || 0 },
    { name: '偿还债务支付的现金', value: -(data['偿还债务支付的现金'] || 0) },
    { name: '分配股利、利润或偿付利息支付的现金', value: -(data['分配股利、利润或偿付利息支付的现金'] || 0) },
    { name: '筹资活动产生的现金流量净额', value: data['筹资活动产生的现金流量净额'] || 0, isTotal: true }
  ]
})

// 汇总项目
const summaryItems = computed(() => {
  if (!reportData.value) return []
  return [
    { name: '现金及现金等价物净增加额', value: reportData.value.现金及现金等价物净增加额 || 0, isTotal: true, highlight: true },
    { name: '期初现金及现金等价物余额', value: reportData.value.期初现金及现金等价物余额 || 0 },
    { name: '期末现金及现金等价物余额', value: reportData.value.期末现金及现金等价物余额 || 0, isTotal: true }
  ]
})

const loadReport = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) {
    ElMessage.warning('请选择日期范围')
    return
  }
  try {
    const { data } = await getCashFlow({
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
  
  const url = `/api/report/cash-flow/export?startDate=${dateRange.value[0]}&endDate=${dateRange.value[1]}`
  const link = document.createElement('a')
  link.href = url
  link.download = `现金流量表_${dateRange.value[0]}至${dateRange.value[1]}.xlsx`
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

.section-header {
  background: #f5f7fa;
  color: #303133;
  padding: 10px 15px;
  margin-bottom: 0;
  border-left: 4px solid #409eff;
  font-weight: bold;
  font-size: 14px;
}

.total-row {
  font-weight: bold;
  color: #303133;
  font-size: 14px;
}

.summary-section {
  margin-top: 25px;
}
</style>


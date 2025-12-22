<template>
  <div class="dashboard">
    <!-- 顶部信息区 - 统一容器 -->
    <el-card class="header-section" shadow="never">
      <div class="header-content">
        <div class="welcome-info">
          <h2 class="system-title">FS_System 财务会计管理系统</h2>
          <div class="system-meta">
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ currentDate }}
            </span>
            <span class="meta-divider">|</span>
            <span class="meta-item">
              <el-icon><User /></el-icon>
              管理员
            </span>
            <span class="meta-divider">|</span>
            <span class="meta-item">
              <el-icon><Monitor /></el-icon>
              v1.0.0
            </span>
          </div>
        </div>
        <div class="header-icon">
          <el-icon><TrendCharts /></el-icon>
        </div>
      </div>
    </el-card>

    <!-- 核心数据统计区 - 统一容器 -->
    <el-card class="stats-section" shadow="never">
      <div class="section-title">
        <el-icon><DataLine /></el-icon>
        <span>核心业务数据</span>
      </div>
      <div class="stats-grid">
        <div class="stat-item" @click="$router.push('/accounting/voucher')">
          <div class="stat-icon stat-icon-1">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-data">
            <div class="stat-value">{{ stats.voucherCount }}</div>
            <div class="stat-label">会计凭证</div>
          </div>
        </div>
        
        <div class="stat-item" @click="$router.push('/business/purchase-order')">
          <div class="stat-icon stat-icon-2">
            <el-icon><ShoppingBag /></el-icon>
          </div>
          <div class="stat-data">
            <div class="stat-value">{{ stats.purchaseOrderCount }}</div>
            <div class="stat-label">采购订单</div>
          </div>
        </div>
        
        <div class="stat-item" @click="$router.push('/business/sales-invoice')">
          <div class="stat-icon stat-icon-3">
            <el-icon><Tickets /></el-icon>
          </div>
          <div class="stat-data">
            <div class="stat-value">{{ stats.salesInvoiceCount }}</div>
            <div class="stat-label">销售发票</div>
          </div>
        </div>
        
        <div class="stat-item" @click="$router.push('/business/expense-claim')">
          <div class="stat-icon stat-icon-4">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-data">
            <div class="stat-value">{{ stats.expenseClaimCount }}</div>
            <div class="stat-label">费用报销</div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 主功能区 - 两列布局 -->
    <el-row :gutter="16" class="main-section">
      <!-- 左侧：快捷操作 -->
      <el-col :span="16">
        <el-card class="function-section" shadow="never">
          <div class="section-title">
            <el-icon><Guide /></el-icon>
            <span>快捷操作</span>
          </div>
          <div class="quick-grid">
            <div 
              v-for="(item, index) in quickAccessItems.slice(0, 6)" 
              :key="index"
              class="quick-item"
              @click="$router.push(item.path)"
            >
              <div class="quick-icon" :style="{ background: item.background }">
                <el-icon :size="24"><component :is="item.icon" /></el-icon>
              </div>
              <div class="quick-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：业务流程 -->
      <el-col :span="8">
        <el-card class="workflow-section" shadow="never">
          <div class="section-title">
            <el-icon><Operation /></el-icon>
            <span>业务流程</span>
          </div>
          <div class="workflow-list">
            <div class="flow-item">
              <div class="flow-number">1</div>
              <div class="flow-text">基础设置</div>
            </div>
            <div class="flow-connector"></div>
            <div class="flow-item">
              <div class="flow-number">2</div>
              <div class="flow-text">业务处理</div>
            </div>
            <div class="flow-connector"></div>
            <div class="flow-item">
              <div class="flow-number">3</div>
              <div class="flow-text">总账核算</div>
            </div>
            <div class="flow-connector"></div>
            <div class="flow-item">
              <div class="flow-number">4</div>
              <div class="flow-text">财务报表</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部信息区 -->
    <el-card class="info-section" shadow="never">
      <div class="section-title">
        <el-icon><InfoFilled /></el-icon>
        <span>技术架构</span>
      </div>
      <div class="tech-grid">
        <div class="tech-item">
          <div class="tech-label">前端框架</div>
          <div class="tech-value">Vue 3 + Element Plus</div>
        </div>
        <div class="tech-item">
          <div class="tech-label">后端框架</div>
          <div class="tech-value">Spring Boot + MyBatis</div>
        </div>
        <div class="tech-item">
          <div class="tech-label">数据库</div>
          <div class="tech-value">MySQL 8.0</div>
        </div>
        <div class="tech-item">
          <div class="tech-label">核心功能</div>
          <div class="tech-value">财务管理 · 会计核算 · 智能过账</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

// 当前日期
const currentDate = computed(() => {
  const date = new Date()
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
})

// 统计数据
const stats = ref({
  voucherCount: 0,
  purchaseOrderCount: 0,
  salesInvoiceCount: 0,
  expenseClaimCount: 0
})

// 加载统计数据
const loadStats = async () => {
  try {
    // 并行请求所有数据
    const [voucherRes, purchaseRes, salesRes, expenseRes] = await Promise.all([
      request.get('/voucher/list', { params: { pageNum: 1, pageSize: 1 } }),
      request.get('/purchase-order/list'),
      request.get('/sales/list', { params: { pageNum: 1, pageSize: 1 } }),
      request.get('/expense/list', { params: { pageNum: 1, pageSize: 1 } })
    ])
    
    stats.value = {
      voucherCount: voucherRes.data?.total || voucherRes.data?.rows?.length || 0,
      purchaseOrderCount: Array.isArray(purchaseRes.data) ? purchaseRes.data.length : 0,
      salesInvoiceCount: salesRes.data?.total || salesRes.data?.rows?.length || 0,
      expenseClaimCount: expenseRes.data?.total || expenseRes.data?.rows?.length || 0
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    // 静默失败，使用默认值
  }
}

// 快捷入口数据
const quickAccessItems = [
  {
    title: '会计凭证',
    desc: '手工录入会计凭证',
    icon: 'DocumentCopy',
    path: '/accounting/voucher',
    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    title: '采购订单',
    desc: '管理采购业务单据',
    icon: 'ShoppingBag',
    path: '/business/purchase-order',
    background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    title: '智能过账',
    desc: '自动生成会计凭证',
    icon: 'Connection',
    path: '/accounting/posting',
    background: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    title: '财务报表',
    desc: '查看财务分析报表',
    icon: 'DataLine',
    path: '/report/balance-sheet',
    background: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  },
  {
    title: '销售发票',
    desc: '管理销售业务单据',
    icon: 'Tickets',
    path: '/business/sales-invoice',
    background: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  },
  {
    title: '费用报销',
    desc: '员工费用报销管理',
    icon: 'Money',
    path: '/business/expense-claim',
    background: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)'
  },
  {
    title: '科目管理',
    desc: '设置会计科目体系',
    icon: 'Grid',
    path: '/system/account-subject',
    background: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)'
  },
  {
    title: '企业信息',
    desc: '维护企业基本信息',
    icon: 'OfficeBuilding',
    path: '/system/company',
    background: 'linear-gradient(135deg, #fbc2eb 0%, #a6c1ee 100%)'
  }
]

// 组件挂载时加载数据
onMounted(() => {
  loadStats()
})
</script>

<style scoped lang="scss">
.dashboard {
  // 统一的卡片样式
  :deep(.el-card) {
    border-radius: 12px;
    border: 1px solid var(--el-border-color-lighter);
    margin-bottom: 16px;
    overflow: hidden;
    
    .el-card__body {
      padding: 20px;
    }
  }
  
  // 统一的区块标题
  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 2px solid var(--el-border-color-lighter);
    
    .el-icon {
      font-size: 18px;
      color: var(--primary-color);
    }
  }
  
  // 顶部信息区
  .header-section {
    background: linear-gradient(135deg, var(--primary-color) 0%, #0052CC 100%);
    border: none;
    border-radius: 16px;
    color: white;
    overflow: hidden;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .welcome-info {
        flex: 1;
        
        .system-title {
          font-size: 20px;
          font-weight: 700;
          margin-bottom: 10px;
          letter-spacing: 0.5px;
        }
        
        .system-meta {
          display: flex;
          align-items: center;
          gap: 12px;
          font-size: 13px;
          opacity: 0.95;
          
          .meta-item {
            display: flex;
            align-items: center;
            gap: 6px;
            
            .el-icon {
              font-size: 14px;
            }
          }
          
          .meta-divider {
            opacity: 0.5;
          }
        }
      }
      
      .header-icon {
        font-size: 64px;
        opacity: 0.15;
        animation: rotate 20s linear infinite;
      }
    }
  }
  
  // 核心数据统计区
  .stats-section {
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      
      .stat-item {
        display: flex;
        align-items: center;
        gap: 14px;
        padding: 16px;
        background: linear-gradient(135deg, #F7F9FC 0%, #FFFFFF 100%);
        border-radius: 12px;
        border: 2px solid transparent;
        cursor: pointer;
        transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
        
        &:hover {
          border-color: var(--primary-color);
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(38, 132, 255, 0.15);
        }
        
        .stat-icon {
          width: 48px;
          height: 48px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 24px;
          flex-shrink: 0;
        }
        
        .stat-icon-1 { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        .stat-icon-2 { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        .stat-icon-3 { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        .stat-icon-4 { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
        
        .stat-data {
          flex: 1;
          
          .stat-value {
            font-size: 26px;
            font-weight: 700;
            color: var(--text-primary);
            line-height: 1.2;
            margin-bottom: 4px;
          }
          
          .stat-label {
            font-size: 13px;
            color: var(--text-secondary);
            font-weight: 500;
          }
        }
      }
    }
  }
  
  // 主功能区
  .main-section {
    margin-bottom: 16px;
  }
  
  // 快捷操作
  .function-section {
    .quick-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 12px;
      
      .quick-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 14px;
        background: linear-gradient(135deg, #F7F9FC 0%, #FFFFFF 100%);
        border-radius: 12px;
        border: 2px solid transparent;
        cursor: pointer;
        transition: all 0.25s;
        
        &:hover {
          border-color: var(--primary-color);
          transform: translateX(3px);
          box-shadow: 0 2px 8px rgba(38, 132, 255, 0.12);
          
          .quick-icon {
            transform: scale(1.08);
          }
        }
        
        .quick-icon {
          width: 40px;
          height: 40px;
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          flex-shrink: 0;
          transition: all 0.25s;
        }
        
        .quick-title {
          flex: 1;
          font-size: 13px;
          font-weight: 600;
          color: var(--text-primary);
        }
      }
    }
  }
  
  // 业务流程
  .workflow-section {
    height: 100%;
    
    .workflow-list {
      display: flex;
      flex-direction: column;
      gap: 0;
      
      .flow-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px 14px;
        background: linear-gradient(135deg, #F7F9FC 0%, #FFFFFF 100%);
        border-radius: 12px;
        border-left: 3px solid var(--primary-color);
        
        .flow-number {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          background: linear-gradient(135deg, var(--primary-color) 0%, #0052CC 100%);
          color: white;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 600;
          font-size: 14px;
          flex-shrink: 0;
        }
        
        .flow-text {
          font-size: 14px;
          font-weight: 600;
          color: var(--text-primary);
        }
      }
      
      .flow-connector {
        width: 2px;
        height: 12px;
        background: linear-gradient(180deg, var(--primary-color) 0%, rgba(38, 132, 255, 0.3) 100%);
        margin-left: 28px;
      }
    }
  }
  
  // 技术架构信息
  .info-section {
    .tech-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 14px;
      
      .tech-item {
        padding: 14px;
        background: linear-gradient(135deg, #F7F9FC 0%, #FFFFFF 100%);
        border-radius: 12px;
        border-left: 3px solid var(--primary-color);
        
        .tech-label {
          font-size: 12px;
          color: var(--text-secondary);
          margin-bottom: 6px;
          font-weight: 500;
        }
        
        .tech-value {
          font-size: 13px;
          color: var(--text-primary);
          font-weight: 600;
          line-height: 1.4;
        }
      }
    }
  }
}

// 动画
@keyframes float {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  50% {
    transform: translateY(-20px) translateX(10px);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>








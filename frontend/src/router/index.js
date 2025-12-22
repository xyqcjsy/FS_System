import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/Index.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '工作台', icon: 'Monitor' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    name: 'System',
    meta: { title: '基础设置', icon: 'Setting' },
    children: [
      {
        path: 'company',
        name: 'Company',
        component: () => import('@/views/system/Company.vue'),
        meta: { title: '企业初始化', icon: 'OfficeBuilding' }
      },
      {
        path: 'account-subject',
        name: 'AccountSubject',
        component: () => import('@/views/system/AccountSubject.vue'),
        meta: { title: '会计科目管理', icon: 'List' }
      },
      {
        path: 'employee',
        name: 'Employee',
        component: () => import('@/views/system/Employee.vue'),
        meta: { title: '员工管理', icon: 'User' }
      },
      {
        path: 'supplier',
        name: 'Supplier',
        component: () => import('@/views/system/Supplier.vue'),
        meta: { title: '供应商管理', icon: 'ShoppingCart' }
      },
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('@/views/system/Customer.vue'),
        meta: { title: '客户管理', icon: 'UserFilled' }
      }
    ]
  },
  {
    path: '/business',
    component: Layout,
    name: 'Business',
    meta: { title: '业务处理', icon: 'Document' },
    children: [
      {
        path: 'purchase-order',
        name: 'PurchaseOrder',
        component: () => import('@/views/business/PurchaseOrder.vue'),
        meta: { title: '采购订单', icon: 'ShoppingBag' }
      },
      {
        path: 'sales-invoice',
        name: 'SalesInvoice',
        component: () => import('@/views/business/SalesInvoice.vue'),
        meta: { title: '销售订单/发票', icon: 'Tickets' }
      },
      {
        path: 'expense-claim',
        name: 'ExpenseClaim',
        component: () => import('@/views/business/ExpenseClaim.vue'),
        meta: { title: '费用报销', icon: 'Money' }
      }
    ]
  },
  {
    path: '/accounting',
    component: Layout,
    name: 'Accounting',
    meta: { title: '总账核算', icon: 'Notebook' },
    children: [
      {
        path: 'voucher',
        name: 'Voucher',
        component: () => import('@/views/accounting/Voucher.vue'),
        meta: { title: '凭证管理', icon: 'DocumentCopy' }
      },
      {
        path: 'posting',
        name: 'Posting',
        component: () => import('@/views/accounting/Posting.vue'),
        meta: { title: '智能过账', icon: 'Connection' }
      },
      {
        path: 'reconciliation',
        name: 'Reconciliation',
        component: () => import('@/views/accounting/Reconciliation.vue'),
        meta: { title: '对账', icon: 'Check' }
      },
      {
        path: 'periodic-operation',
        name: 'PeriodicOperation',
        component: () => import('@/views/accounting/PeriodicOperation.vue'),
        meta: { title: '定期业务', icon: 'Calendar' }
      },
      {
        path: 'tax-record',
        name: 'TaxRecord',
        component: () => import('@/views/accounting/TaxRecord.vue'),
        meta: { title: '税务管理', icon: 'Coin' }
      }
    ]
  },
  {
    path: '/report',
    component: Layout,
    name: 'Report',
    meta: { title: '财务报表', icon: 'DataAnalysis' },
    children: [
      {
        path: 'balance-sheet',
        name: 'BalanceSheet',
        component: () => import('@/views/report/BalanceSheet.vue'),
        meta: { title: '资产负债表', icon: 'DataLine' }
      },
      {
        path: 'income-statement',
        name: 'IncomeStatement',
        component: () => import('@/views/report/IncomeStatement.vue'),
        meta: { title: '利润表', icon: 'TrendCharts' }
      },
      {
        path: 'cash-flow',
        name: 'CashFlow',
        component: () => import('@/views/report/CashFlow.vue'),
        meta: { title: '现金流量表', icon: 'Wallet' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router


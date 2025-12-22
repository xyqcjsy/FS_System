# FS_System - 财务会计管理系统

> ©CSU商学院信管2302谢扬强 2025

---

## 📋 目录

1. [项目愿景](#项目愿景)
2. [系统概述](#系统概述)
3. [技术架构](#技术架构)
4. [业务建模](#业务建模)
5. [系统用例](#系统用例)
6. [数据库设计](#数据库设计)
7. [文件结构](#文件结构)
8. [主要功能模块](#主要功能模块)
9. [页面设计](#页面设计)
10. [快速开始](#快速开始)
11. [部署说明](#部署说明)

---

## 🎯 项目愿景

### 愿景目标
打造一款面向中小企业的**智能化财务会计管理系统**，实现财务业务一体化管理，帮助企业：
- 📊 提升财务管理效率，减少人工重复劳动
- 💰 实现业务与财务数据的实时同步
- 📈 提供精准的财务分析和决策支持
- 🔐 确保财务数据的安全性和准确性
- 🌐 支持多公司、多账套管理

### 核心价值
- **业财一体化**：销售、采购、报销等业务自动生成会计凭证
- **标准化流程**：遵循企业会计准则，规范财务处理流程
- **智能化分析**：自动生成三大财务报表，支持多维度分析
- **易用性**：简洁直观的操作界面，降低使用门槛

---

## 📖 系统概述

### 项目简介
财务会计管理系统是一个基于 **Spring Boot 3.2 + Vue.js 3** 开发的企业级财务管理解决方案，提供完整的财务核算、业务管理和报表分析功能。系统采用前后端分离架构，支持RESTful API，具备高可扩展性和可维护性。

### 适用场景
- 中小型企业财务管理
- 会计事务所客户管理
- 财务培训教学系统
- 企业内部财务数字化转型

---

## 🏗️ 技术架构

### 系统架构
```
┌─────────────────────────────────────────────┐
│            前端层 (Presentation)             │
│  Vue 3 + Element Plus + Vue Router + Vite   │
└─────────────────┬───────────────────────────┘
                  │ HTTP/HTTPS (RESTful API)
┌─────────────────▼───────────────────────────┐
│            应用层 (Application)              │
│     Spring Boot 3.2 + Spring MVC            │
│  Controller → Service → Mapper → Entity     │
└─────────────────┬───────────────────────────┘
                  │ MyBatis ORM
┌─────────────────▼───────────────────────────┐
│            数据层 (Data Layer)               │
│    MySQL 8.0 + Druid 连接池                  │
└─────────────────────────────────────────────┘
```

### 技术栈详情

#### 后端技术栈
| 技术 | 版本 | 说明 |
|-----|------|------|
| Java | 21 | 核心开发语言 |
| Spring Boot | 3.2.0 | 应用框架 |
| MyBatis | 3.0.3 | ORM框架 |
| MySQL | 8.0.44 | 关系型数据库 |
| Druid | 1.2.20 | 数据库连接池 |
| Lombok | Latest | 简化Java代码 |
| Hutool | 5.8.24 | Java工具类库 |
| PageHelper | 2.1.0 | 分页插件 |
| Apache POI | 5.2.5 | Excel导入导出 |
| Fastjson2 | 2.0.43 | JSON处理 |

#### 前端技术栈
| 技术 | 版本 | 说明 |
|-----|------|------|
| Vue.js | 3.x | 渐进式JavaScript框架 |
| Element Plus | Latest | UI组件库 |
| Vue Router | 4.x | 路由管理 |
| Vite | Latest | 前端构建工具 |
| Axios | Latest | HTTP客户端 |

---

## 💼 业务建模

### 核心业务流程

#### 1. 业务单据流转流程
```
业务发生 → 单据录入 → 审核确认 → 自动生成凭证 → 过账 → 生成报表
```

#### 2. 销售业务流程
```
客户下单 → 销售发票录入 → 确认收入 → 生成销售凭证
   ↓
借：银行存款/应收账款
贷：主营业务收入
贷：应交税费-增值税(销项)
```

#### 3. 采购业务流程
```
采购需求 → 采购订单录入 → 验收入库 → 生成采购凭证
   ↓
借：原材料/库存商品
借：应交税费-增值税(进项)
贷：银行存款/应付账款
```

#### 4. 报销业务流程
```
员工提交报销单 → 审批 → 财务审核 → 生成报销凭证
   ↓
借：管理费用/销售费用等
贷：银行存款/其他应付款
```

#### 5. 会计期间流程
```
日常业务 → 凭证录入 → 凭证过账 → 期末对账 → 税务处理 → 结转损益 → 生成报表
```

### 关键业务规则
- **复式记账原则**：有借必有贷，借贷必相等
- **权责发生制**：收入费用按权责发生制确认
- **会计期间**：按月度进行会计核算和报表生成
- **审批控制**：重要业务单据需要审批后才能生成凭证
- **过账锁定**：已过账凭证不可修改，需要反过账后才能调整

---

## 📋 系统用例

### 用例图说明

#### 系统管理模块
| 用例名称 | 参与者 | 主要功能 |
|---------|-------|---------|
| 科目管理 | 财务主管 | 新增、修改、删除会计科目；维护科目树形结构 |
| 公司管理 | 系统管理员 | 管理多公司信息，配置公司参数 |
| 员工管理 | 人事/财务 | 维护员工档案，用于报销关联 |
| 客户管理 | 销售/财务 | 维护客户信息，用于销售业务 |
| 供应商管理 | 采购/财务 | 维护供应商信息，用于采购业务 |

#### 业务管理模块
| 用例名称 | 参与者 | 主要流程 |
|---------|-------|---------|
| 销售发票 | 销售人员 | 录入销售发票→添加明细→保存→生成凭证 |
| 采购订单 | 采购人员 | 录入采购订单→添加明细→保存→生成凭证 |
| 报销单据 | 员工 | 提交报销申请→添加费用明细→审批→生成凭证 |

#### 会计核算模块
| 用例名称 | 参与者 | 主要流程 |
|---------|-------|---------|
| 凭证管理 | 会计人员 | 手工录入凭证、修改、删除、查询 |
| 过账处理 | 会计主管 | 批量过账、反过账、查看过账状态 |
| 对账管理 | 会计人员 | 科目余额对账、往来对账 |
| 税务记录 | 税务会计 | 记录税费计提、缴纳情况 |
| 期间操作 | 财务主管 | 期初建账、结转损益、期末结账 |

#### 财务报表模块
| 用例名称 | 参与者 | 输出内容 |
|---------|-------|---------|
| 资产负债表 | 管理层/会计 | 显示企业资产、负债、所有者权益状况 |
| 利润表 | 管理层/会计 | 显示企业收入、成本、利润情况 |
| 现金流量表 | 管理层/会计 | 显示经营、投资、筹资活动现金流 |

---

## 🗄️ 数据库设计

### ER模型概述
系统采用关系型数据库设计，主要实体及关系如下：

```
Company(公司) ──1:N── AccountSubject(会计科目)
                │
                ├──1:N── Employee(员工)
                ├──1:N── Customer(客户)
                └──1:N── Supplier(供应商)

SalesInvoice(销售发票) ──1:N── SalesInvoiceItem(明细)
PurchaseOrder(采购订单) ──1:N── PurchaseOrderItem(明细)
ExpenseClaim(报销单) ──1:N── ExpenseClaimItem(明细)

Voucher(会计凭证) ──1:N── VoucherEntry(凭证分录)
                    │
                    └──N:1── AccountSubject(会计科目)

VoucherEntry ──N:M── CashFlowItem(现金流量项目)
```

### 核心数据表

#### 1. 系统管理表

**公司表 (sys_company)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| company_code | varchar(50) | 公司编码(唯一) |
| company_name | varchar(200) | 公司名称 |
| legal_person | varchar(100) | 法人代表 |
| tax_id | varchar(50) | 税号 |
| registered_capital | decimal(18,2) | 注册资本 |
| address | varchar(500) | 地址 |
| contact_phone | varchar(20) | 联系电话 |
| is_enabled | tinyint | 是否启用 |

**会计科目表 (acct_account_subject)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| subject_code | varchar(50) | 科目编码(唯一) |
| subject_name | varchar(200) | 科目名称 |
| subject_type | enum | 科目类型(资产/负债/权益/成本/损益) |
| direction | enum | 余额方向(借方/贷方) |
| level | int | 科目级次 |
| parent_id | bigint | 父科目ID |
| is_leaf | tinyint | 是否末级科目 |
| is_cash | tinyint | 是否现金类科目 |

**员工表 (sys_employee)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| employee_code | varchar(50) | 员工编号 |
| employee_name | varchar(100) | 姓名 |
| department | varchar(100) | 部门 |
| position | varchar(100) | 职位 |
| phone | varchar(20) | 电话 |

#### 2. 业务单据表

**销售发票表 (bus_sales_invoice)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| invoice_no | varchar(50) | 发票号 |
| customer_id | bigint | 客户ID |
| invoice_date | date | 开票日期 |
| total_amount | decimal(18,2) | 总金额 |
| tax_amount | decimal(18,2) | 税额 |
| voucher_id | bigint | 关联凭证ID |
| status | enum | 状态(草稿/已生成凭证) |

**销售发票明细表 (bus_sales_invoice_item)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| invoice_id | bigint | 发票ID |
| item_name | varchar(200) | 项目名称 |
| quantity | decimal(18,2) | 数量 |
| unit_price | decimal(18,2) | 单价 |
| amount | decimal(18,2) | 金额 |
| tax_rate | decimal(5,2) | 税率 |

**采购订单表 (bus_purchase_order)** - 结构类似销售发票

**报销单表 (bus_expense_claim)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| claim_no | varchar(50) | 报销单号 |
| employee_id | bigint | 报销人ID |
| claim_date | date | 报销日期 |
| total_amount | decimal(18,2) | 总金额 |
| voucher_id | bigint | 关联凭证ID |
| status | enum | 状态 |

#### 3. 会计核算表

**会计凭证表 (acct_voucher)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| voucher_no | varchar(50) | 凭证号 |
| voucher_date | date | 凭证日期 |
| voucher_type | enum | 凭证类型(记账/收款/付款/转账) |
| abstract | varchar(500) | 摘要 |
| total_debit | decimal(18,2) | 借方合计 |
| total_credit | decimal(18,2) | 贷方合计 |
| is_posted | tinyint | 是否已过账 |
| source_type | varchar(50) | 来源类型 |
| source_id | bigint | 来源单据ID |

**凭证分录表 (acct_voucher_entry)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| voucher_id | bigint | 凭证ID |
| line_no | int | 行号 |
| subject_id | bigint | 科目ID |
| abstract | varchar(500) | 摘要 |
| debit_amount | decimal(18,2) | 借方金额 |
| credit_amount | decimal(18,2) | 贷方金额 |

**现金流量项目表 (acct_cash_flow_item)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| item_code | varchar(50) | 项目编码 |
| item_name | varchar(200) | 项目名称 |
| category | enum | 活动类别(经营/投资/筹资) |
| direction | enum | 流向(流入/流出) |

**过账记录表 (acct_posting_record)**
| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | bigint | 主键 |
| voucher_id | bigint | 凭证ID |
| subject_id | bigint | 科目ID |
| debit_amount | decimal(18,2) | 借方金额 |
| credit_amount | decimal(18,2) | 贷方金额 |
| balance | decimal(18,2) | 余额 |
| posting_date | date | 过账日期 |

### 数据库索引设计
- 所有外键字段建立索引
- 单据编号字段建立唯一索引
- 日期字段建立普通索引以优化查询
- 状态字段建立索引以支持状态筛选

---

## 📁 文件结构

### 后端项目结构
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/fs/system/
│   │   │   ├── FsSystemApplication.java      # 启动类
│   │   │   ├── common/                        # 公共类
│   │   │   │   ├── BaseEntity.java           # 实体基类
│   │   │   │   ├── PageResult.java           # 分页结果
│   │   │   │   └── Result.java               # 统一响应
│   │   │   ├── config/                        # 配置类
│   │   │   │   └── CorsConfig.java           # 跨域配置
│   │   │   ├── controller/                    # 控制器层
│   │   │   │   ├── AccountSubjectController.java    # 科目管理
│   │   │   │   ├── CompanyController.java           # 公司管理
│   │   │   │   ├── EmployeeController.java          # 员工管理
│   │   │   │   ├── CustomerController.java          # 客户管理
│   │   │   │   ├── SupplierController.java          # 供应商管理
│   │   │   │   ├── SalesInvoiceController.java      # 销售发票
│   │   │   │   ├── PurchaseOrderController.java     # 采购订单
│   │   │   │   ├── ExpenseClaimController.java      # 报销单
│   │   │   │   ├── VoucherController.java           # 凭证管理
│   │   │   │   ├── PostingController.java           # 过账处理
│   │   │   │   ├── ReconciliationController.java    # 对账管理
│   │   │   │   ├── TaxRecordController.java         # 税务记录
│   │   │   │   ├── PeriodicOperationController.java # 期间操作
│   │   │   │   └── ReportController.java            # 报表生成
│   │   │   ├── entity/                        # 实体类
│   │   │   │   ├── AccountSubject.java       # 会计科目
│   │   │   │   ├── Company.java              # 公司
│   │   │   │   ├── Employee.java             # 员工
│   │   │   │   ├── Customer.java             # 客户
│   │   │   │   ├── Supplier.java             # 供应商
│   │   │   │   ├── SalesInvoice.java         # 销售发票
│   │   │   │   ├── SalesInvoiceItem.java     # 销售明细
│   │   │   │   ├── PurchaseOrder.java        # 采购订单
│   │   │   │   ├── PurchaseOrderItem.java    # 采购明细
│   │   │   │   ├── ExpenseClaim.java         # 报销单
│   │   │   │   ├── ExpenseClaimItem.java     # 报销明细
│   │   │   │   ├── Voucher.java              # 会计凭证
│   │   │   │   ├── VoucherEntry.java         # 凭证分录
│   │   │   │   ├── CashFlowItem.java         # 现金流量项目
│   │   │   │   ├── PostingRecord.java        # 过账记录
│   │   │   │   ├── Reconciliation.java       # 对账记录
│   │   │   │   ├── TaxRecord.java            # 税务记录
│   │   │   │   └── PeriodicOperation.java    # 期间操作
│   │   │   ├── exception/                     # 异常处理
│   │   │   │   ├── BusinessException.java    # 业务异常
│   │   │   │   └── GlobalExceptionHandler.java # 全局异常处理
│   │   │   ├── mapper/                        # 数据访问层
│   │   │   │   ├── AccountSubjectMapper.java
│   │   │   │   ├── CompanyMapper.java
│   │   │   │   ├── EmployeeMapper.java
│   │   │   │   ├── CustomerMapper.java
│   │   │   │   ├── SupplierMapper.java
│   │   │   │   ├── SalesInvoiceMapper.java
│   │   │   │   ├── PurchaseOrderMapper.java
│   │   │   │   ├── ExpenseClaimMapper.java
│   │   │   │   ├── VoucherMapper.java
│   │   │   │   ├── VoucherEntryMapper.java
│   │   │   │   ├── PostingRecordMapper.java
│   │   │   │   ├── ReconciliationMapper.java
│   │   │   │   └── TaxRecordMapper.java
│   │   │   └── service/                       # 业务逻辑层
│   │   │       ├── AccountSubjectService.java
│   │   │       ├── CompanyService.java
│   │   │       ├── EmployeeService.java
│   │   │       ├── CustomerService.java
│   │   │       ├── SupplierService.java
│   │   │       ├── SalesInvoiceService.java
│   │   │       ├── PurchaseOrderService.java
│   │   │       ├── ExpenseClaimService.java
│   │   │       ├── VoucherService.java
│   │   │       ├── PostingService.java
│   │   │       ├── ReconciliationService.java
│   │   │       ├── TaxRecordService.java
│   │   │       ├── PeriodicOperationService.java
│   │   │       └── ReportService.java
│   │   └── resources/
│   │       ├── application.yml                # 开发环境配置
│   │       ├── application-prod.yml           # 生产环境配置
│   │       └── mapper/                        # MyBatis XML映射
│   │           ├── AccountSubjectMapper.xml
│   │           ├── CompanyMapper.xml
│   │           ├── EmployeeMapper.xml
│   │           ├── CustomerMapper.xml
│   │           ├── SupplierMapper.xml
│   │           ├── SalesInvoiceMapper.xml
│   │           ├── PurchaseOrderMapper.xml
│   │           ├── ExpenseClaimMapper.xml
│   │           ├── VoucherMapper.xml
│   │           └── ...
│   └── test/                                  # 测试代码
└── pom.xml                                    # Maven依赖配置
```

### 前端项目结构
```
frontend/
├── public/                                    # 静态资源
├── src/
│   ├── api/                                   # API接口封装
│   │   ├── accounting.js                     # 会计核算接口
│   │   ├── business.js                       # 业务管理接口
│   │   ├── report.js                         # 报表接口
│   │   └── system.js                         # 系统管理接口
│   ├── assets/                                # 资源文件
│   │   └── styles/
│   │       ├── main.scss                     # 全局样式
│   │       └── business-common.scss          # 业务模块公共样式
│   ├── layout/                                # 布局组件
│   │   └── Index.vue                         # 主布局
│   ├── router/                                # 路由配置
│   │   └── index.js                          # 路由定义
│   ├── utils/                                 # 工具类
│   │   └── request.js                        # Axios封装
│   ├── views/                                 # 页面组件
│   │   ├── Dashboard.vue                     # 首页仪表盘
│   │   ├── system/                           # 系统管理模块
│   │   │   ├── AccountSubject.vue           # 科目管理
│   │   │   ├── Company.vue                  # 公司管理
│   │   │   ├── Employee.vue                 # 员工管理
│   │   │   ├── Customer.vue                 # 客户管理
│   │   │   └── Supplier.vue                 # 供应商管理
│   │   ├── business/                         # 业务管理模块
│   │   │   ├── SalesInvoice.vue             # 销售发票
│   │   │   ├── PurchaseOrder.vue            # 采购订单
│   │   │   └── ExpenseClaim.vue             # 报销单据
│   │   ├── accounting/                       # 会计核算模块
│   │   │   ├── Voucher.vue                  # 凭证管理
│   │   │   ├── Posting.vue                  # 过账处理
│   │   │   ├── Reconciliation.vue           # 对账管理
│   │   │   ├── TaxRecord.vue                # 税务记录
│   │   │   └── PeriodicOperation.vue        # 期间操作
│   │   └── report/                           # 财务报表模块
│   │       ├── BalanceSheet.vue             # 资产负债表
│   │       ├── IncomeStatement.vue          # 利润表
│   │       └── CashFlow.vue                 # 现金流量表
│   ├── App.vue                                # 根组件
│   └── main.js                                # 入口文件
├── index.html                                 # HTML模板
├── package.json                               # 依赖配置
└── vite.config.js                             # Vite配置
```

### 数据库脚本
```
database/
└── fs_system.sql                              # 完整数据库建表脚本
```

---

## 🔧 主要功能模块

### 1. 系统管理模块

#### 1.1 科目管理
- **功能**：维护企业会计科目体系
- **特性**：
  - 树形结构展示，支持多级科目
  - 科目类型分类（资产、负债、权益、成本、损益）
  - 余额方向设置（借方、贷方）
  - 现金类科目标识
  - 末级科目控制（只有末级科目可用于记账）
- **操作**：新增、编辑、删除、启用/停用

#### 1.2 公司管理
- **功能**：管理企业基本信息
- **字段**：公司编码、名称、法人、税号、注册资本、地址、联系方式
- **支持**：多公司账套管理

#### 1.3 客户/供应商管理
- **功能**：维护往来单位信息
- **应用**：销售发票、采购订单关联使用
- **字段**：编码、名称、联系人、电话、地址、信用等级

#### 1.4 员工管理
- **功能**：维护员工档案
- **应用**：报销单关联、费用归属
- **字段**：员工编号、姓名、部门、职位、联系方式

### 2. 业务管理模块

#### 2.1 销售发票管理
- **流程**：
  1. 录入发票基本信息（客户、日期、总金额）
  2. 添加销售明细（商品、数量、单价、税率）
  3. 系统自动计算金额和税额
  4. 生成销售凭证
- **凭证分录**：
  ```
  借：银行存款/应收账款
  贷：主营业务收入
  贷：应交税费-应交增值税(销项税额)
  ```

#### 2.2 采购订单管理
- **流程**：录入采购信息→添加明细→生成凭证
- **凭证分录**：
  ```
  借：原材料/库存商品
  借：应交税费-应交增值税(进项税额)
  贷：银行存款/应付账款
  ```

#### 2.3 报销单管理
- **流程**：员工提交→录入费用明细→审批→生成凭证
- **凭证分录**：
  ```
  借：管理费用/销售费用/制造费用等
  贷：银行存款/其他应付款
  ```
- **费用类型**：差旅费、办公费、通讯费、招待费等

### 3. 会计核算模块

#### 3.1 凭证管理
- **功能**：手工录入、修改、删除会计凭证
- **凭证类型**：记账凭证、收款凭证、付款凭证、转账凭证
- **录入规则**：
  - 借贷必须平衡
  - 科目必须是末级科目
  - 现金类科目自动关联现金流量项目
- **操作**：新增、编辑、删除、复制、作废

#### 3.2 过账处理
- **功能**：将凭证数据转入账簿，生成科目余额
- **特性**：
  - 批量过账
  - 反过账（取消过账）
  - 已过账凭证锁定不可修改
  - 自动计算科目余额
- **过账规则**：
  - 借方科目：余额 = 期初余额 + 借方发生额 - 贷方发生额
  - 贷方科目：余额 = 期初余额 + 贷方发生额 - 借方发生额

#### 3.3 对账管理
- **功能**：检查账簿数据准确性
- **对账类型**：
  - 科目余额对账：总账与明细账核对
  - 往来对账：应收应付账款核对
  - 银行对账：银行存款日记账与银行对账单核对

#### 3.4 税务记录
- **功能**：记录税费计提和缴纳
- **税种**：增值税、企业所得税、个人所得税、印花税等
- **操作**：计提税费、缴纳税费、查询税务记录

#### 3.5 期间操作
- **功能**：会计期间管理和期末处理
- **操作**：
  - 期初建账：设置期初余额
  - 结转损益：将损益类科目余额结转至本年利润
  - 期末结账：锁定本期数据，不允许再修改
  - 反结账：解锁期间，允许调整
- **结转分录**：
  ```
  借：主营业务收入
  借：其他业务收入
  贷：本年利润
  
  借：本年利润
  贷：主营业务成本
  贷：管理费用
  贷：销售费用
  贷：财务费用
  ```

### 4. 财务报表模块

#### 4.1 资产负债表
- **功能**：反映企业某一特定日期的财务状况
- **结构**：
  - **资产**：流动资产、非流动资产
  - **负债**：流动负债、非流动负债
  - **所有者权益**：实收资本、资本公积、盈余公积、未分配利润
- **平衡公式**：资产 = 负债 + 所有者权益
- **特性**：支持月度、季度、年度查询

#### 4.2 利润表
- **功能**：反映企业一定期间的经营成果
- **结构**：
  - 营业收入
  - 减：营业成本、税金及附加、销售费用、管理费用、财务费用
  - 加：其他收益、投资收益
  - 营业利润
  - 加：营业外收入
  - 减：营业外支出
  - 利润总额
  - 减：所得税费用
  - 净利润
- **特性**：同比、环比分析

#### 4.3 现金流量表
- **功能**：反映企业现金及现金等价物的流入和流出
- **结构**：
  - 经营活动产生的现金流量
  - 投资活动产生的现金流量
  - 筹资活动产生的现金流量
- **编制方法**：通过现金流量项目自动归集
- **特性**：支持直接法编制

---

## 🖥️ 页面设计

### 布局风格
- **顶部导航**：Logo + 系统名称 + 用户信息
- **左侧菜单**：树形导航，分模块展示
- **主内容区**：面包屑 + 功能操作区 + 数据展示区

### 主要页面说明

#### 1. 首页仪表盘 (Dashboard.vue)
- **展示内容**：
  - 关键财务指标卡片（总资产、总负债、净利润、营业收入）
  - 本月凭证统计
  - 待办事项提醒
  - 收入支出趋势图表
- **设计风格**：卡片式布局，数据可视化

#### 2. 科目管理页面 (AccountSubject.vue)
- **布局**：左侧树形结构 + 右侧表格
- **功能**：新增、编辑、删除、搜索
- **字段**：科目编码、名称、类型、方向、是否末级、是否启用

#### 3. 业务单据页面 (SalesInvoice/PurchaseOrder/ExpenseClaim)
- **列表页**：
  - 搜索筛选区（日期范围、单号、客户/供应商/员工）
  - 操作按钮（新增、导出）
  - 数据表格（单号、日期、对方单位、金额、状态）
  - 行操作（编辑、删除、生成凭证、查看凭证）
- **新增/编辑弹窗**：
  - 基本信息表单
  - 明细表格（支持行内编辑、新增行、删除行）
  - 金额自动计算
  - 保存、取消按钮

#### 4. 凭证管理页面 (Voucher.vue)
- **列表页**：
  - 筛选条件（日期、凭证号、摘要、是否过账）
  - 凭证列表（凭证号、日期、摘要、借贷合计、状态）
- **凭证录入弹窗**：
  - 凭证头：日期、类型、摘要
  - 凭证体：分录表格（科目、摘要、借方金额、贷方金额）
  - 合计行：借方合计、贷方合计
  - 平衡校验提示
  - 现金流量分配（针对现金类科目）

#### 5. 过账处理页面 (Posting.vue)
- **未过账凭证列表**：批量选择 + 批量过账
- **已过账凭证列表**：反过账操作
- **过账记录查询**：科目余额、发生额查询

#### 6. 对账管理页面 (Reconciliation.vue)
- **科目余额表**：展示所有科目期初、借方、贷方、期末余额
- **余额试算平衡**：总借方 = 总贷方
- **明细账查询**：选择科目查看明细分录

#### 7. 期间操作页面 (PeriodicOperation.vue)
- **期初建账**：批量导入科目期初余额
- **结转损益**：一键结转损益类科目
- **期末结账**：锁定本期
- **反结账**：解锁上期

#### 8. 财务报表页面 (BalanceSheet/IncomeStatement/CashFlow)
- **查询条件**：期间选择（年月）
- **报表展示**：
  - 标准格式表格
  - 金额右对齐
  - 重点行加粗（合计、净利润等）
- **操作**：导出Excel、打印

---

## 🚀 快速开始

### 环境要求
| 软件 | 版本要求 | 说明 |
|-----|---------|------|
| JDK | 21+ | Java开发环境 |
| Node.js | 16+ | 前端运行环境 |
| MySQL | 8.0+ | 数据库 |
| Maven | 3.6+ | 后端依赖管理 |

### 安装步骤

#### 1. 克隆项目
```bash
git clone https://github.com/xyqcjsy/FS_System.git
cd FS_System
```

#### 2. 数据库初始化
```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE fs_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据
mysql -u root -p fs_system < database/fs_system.sql
```

#### 3. 后端配置
修改 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fs_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password  # 修改为你的MySQL密码
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### 4. 启动后端
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
后端服务启动在 `http://localhost:8080`

#### 5. 启动前端
```bash
cd frontend
npm install
npm run dev
```
前端服务启动在 `http://localhost:5173`

#### 6. 访问系统
浏览器打开 `http://localhost:5173`

---

## 📦 部署说明

### 后端打包
```bash
cd backend
mvn clean package -DskipTests
```
生成文件：`backend/target/fs-system-1.0.0.jar`

### 前端打包
```bash
cd frontend
npm run build
```
生成目录：`frontend/dist/`

### 生产环境部署
1. **后端部署**：
```bash
java -jar fs-system-1.0.0.jar --spring.profiles.active=prod
```

2. **前端部署**：
将 `dist` 目录部署到 Nginx

3. **Nginx配置**：
```nginx
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

    server {
        listen       80;
        server_name  localhost;

        location / {
            root   C:/fs_system/frontend/dist;
            index  index.html index.htm;
            try_files $uri $uri/ /index.html;
        }

        location /api/ {
            proxy_pass http://127.0.0.1:8080;
            
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
        
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

---

## 🔐 系统安全

### 数据安全
- 数据库连接使用连接池，防止连接泄露
- 敏感信息加密存储
- SQL注入防护（使用预编译语句）

### 业务控制
- 已过账凭证不可修改
- 已结账期间不可调整
- 关键操作审计日志记录

---

## 📊 性能优化

### 数据库优化
- 合理使用索引
- 分页查询优化
- 连接池配置（Druid）

### 前端优化
- 路由懒加载
- 组件按需加载
- Vite构建优化

---

## 🛠️ 技术特点

### 设计模式
- **MVC模式**：Controller → Service → Mapper 分层架构
- **DTO模式**：数据传输对象封装
- **统一响应**：Result统一封装接口返回

### 代码规范
- 使用Lombok简化代码
- 遵循阿里巴巴Java开发规范
- RESTful API设计

---

## 📈 未来规划

### 功能扩展
- [ ] 用户权限管理系统
- [ ] 审批工作流引擎
- [ ] 移动端APP支持
- [ ] 报表自定义配置
- [ ] 智能财务分析（AI辅助）
- [ ] 多账套管理优化
- [ ] 固定资产管理模块
- [ ] 成本核算模块
- [ ] 预算管理模块

### 技术优化
- [ ] 引入Redis缓存
- [ ] 引入消息队列
- [ ] 微服务架构改造
- [ ] 容器化部署（Docker）

---

## 📝 开发日志

### v1.0.0 
- ✅ 完成基础架构搭建
- ✅ 实现系统管理模块
- ✅ 实现业务管理模块
- ✅ 实现会计核算模块
- ✅ 实现财务报表模块
- ✅ 完成前后端联调

---

## 📄 许可证 / License

MIT License

Copyright (c) 2025 xyqcjsy

---

## 👨‍💻 关于作者

**开发者 / Developer:** 谢扬强 (xyqcjsy)  
**学校 / Institution:** 中南大学商学院 (CSU Business School)  
**专业班级 / Major:** 信息管理与信息系统 2302班  
**开发时间 / Development Time:** 2025年12月

---

## 📧 联系方式

如有问题或建议，欢迎通过以下方式联系：
- GitHub Issues: [提交问题](https://github.com/xyqcjsy/FS_System/issues)
- Email: xyqcjsy@outlook.com

---

## 🙏 致谢

感谢所有开源项目的贡献者，特别是：
- Spring Boot Team
- Vue.js Team
- Element Plus Team
- MyBatis Team

---

**⭐ 如果这个项目对你有帮助，欢迎 Star 支持！**

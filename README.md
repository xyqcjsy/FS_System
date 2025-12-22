# FS_System - 财务会计管理系统

Financial Accounting Management System

> ©CSU商学院信管2302谢扬强 2025

## 项目简介 / Project Introduction

财务会计管理系统是一个基于 Spring Boot + Vue.js 开发的企业级财务管理解决方案，提供完整的财务核算、业务管理和报表分析功能。

This is an enterprise-level financial management system built with Spring Boot and Vue.js, providing comprehensive financial accounting, business management, and reporting features.

## 技术栈 / Tech Stack

### 后端 / Backend
- Java 11+
- Spring Boot 2.x
- MyBatis
- MySQL

### 前端 / Frontend
- Vue.js 3.x
- Element Plus
- Vite
- Vue Router

## 主要功能 / Main Features

### 系统管理 / System Management
- 科目管理 (Account Subject Management)
- 公司管理 (Company Management)
- 员工管理 (Employee Management)
- 客户管理 (Customer Management)
- 供应商管理 (Supplier Management)

### 业务管理 / Business Management
- 销售发票 (Sales Invoice)
- 采购订单 (Purchase Order)
- 报销单据 (Expense Claim)

### 会计核算 / Accounting
- 凭证管理 (Voucher Management)
- 过账处理 (Posting)
- 对账管理 (Reconciliation)
- 税务记录 (Tax Records)
- 期间操作 (Periodic Operations)

### 财务报表 / Financial Reports
- 资产负债表 (Balance Sheet)
- 利润表 (Income Statement)
- 现金流量表 (Cash Flow Statement)

## 项目结构 / Project Structure

```
FS_System/
├── backend/              # 后端 Spring Boot 项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/fs/system/
│   │   │   │   ├── controller/    # 控制器
│   │   │   │   ├── service/       # 业务逻辑
│   │   │   │   ├── mapper/        # 数据访问
│   │   │   │   ├── entity/        # 实体类
│   │   │   │   └── config/        # 配置类
│   │   │   └── resources/
│   │   │       └── mapper/        # MyBatis XML
│   │   └── pom.xml
├── frontend/             # 前端 Vue.js 项目
│   ├── src/
│   │   ├── views/        # 页面组件
│   │   ├── api/          # API 接口
│   │   ├── router/       # 路由配置
│   │   └── utils/        # 工具函数
│   └── package.json
├── database/             # 数据库脚本
│   └── fs_system.sql
└── 部署指南.md           # 部署说明
```

## 快速开始 / Quick Start

### 环境要求 / Prerequisites
- JDK 11+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端启动 / Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 前端启动 / Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

### 数据库初始化 / Database Setup

导入数据库脚本：
```bash
mysql -u root -p < database/fs_system.sql
```

## 配置说明 / Configuration

修改 `backend/src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fs_system?useUnicode=true&characterEncoding=utf8
    username: your_username
    password: your_password
```

## 许可证 / License

MIT License

Copyright (c) 2025 xyqcjsy

---

**开发者 / Developer:** 谢扬强 (xyqcjsy)  
**学校 / Institution:** 中南大学商学院 (CSU Business School)  
**专业班级 / Major:** 信息管理与信息系统 2302班


# 业务处理模块功能说明

## 📋 模块概览

业务处理模块包含三个核心功能：采购订单管理、销售发票管理、费用报销管理。所有模块都已实现完整的CRUD功能、明细管理和业务流程控制。

---

## 🛒 采购订单管理 (PurchaseOrder.vue)

### 主要功能

#### ✅ 列表功能
- 关键词搜索（订单编号）
- 状态筛选（草稿、已确认、已收货、已付款、已过账）
- 分页显示（前端分页）
- 金额格式化显示
- 状态标签彩色显示
- 过账状态显示

#### ✅ 新增/编辑功能
- **基本信息**
  - 自动生成订单编号
  - 供应商选择（下拉列表）
  - 订单日期选择
  - 付款方式选择

- **明细管理**
  - 动态添加/删除明细行
  - 商品名称、规格、单位输入
  - 数量、单价输入，自动计算金额
  - 支持多行明细
  - 表格内编辑

- **金额计算**
  - 自动计算订单总额
  - 税额输入
  - 折扣金额输入
  - 自动计算最终金额
  - 实时更新

#### ✅ 查看详情
- 完整的订单信息展示
- 明细表格显示
- 状态信息完整展示
- 只读模式

#### ✅ 业务操作
- 确认订单（草稿状态）
- 删除订单（草稿状态）
- 二次确认提示

### 数据字段

```javascript
{
  orderNo: String,        // 订单编号
  supplierId: Long,       // 供应商ID
  orderDate: Date,        // 订单日期
  totalAmount: Decimal,   // 订单总额
  taxAmount: Decimal,     // 税额
  discountAmount: Decimal,// 折扣金额
  finalAmount: Decimal,   // 最终金额
  status: Enum,          // 状态
  paymentMethod: String,  // 付款方式
  isPosted: Boolean,     // 是否过账
  items: Array           // 明细列表
}
```

---

## 📄 销售发票管理 (SalesInvoice.vue)

### 主要功能

#### ✅ 列表功能
- 关键词搜索（发票编号）
- 状态筛选（草稿、已确认、已发货、已收款、已过账、已取消）
- 分页显示（后端分页）
- 金额格式化显示
- 状态标签彩色显示
- 过账状态显示

#### ✅ 新增/编辑功能
- **基本信息**
  - 自动生成发票编号
  - 客户选择（下拉列表）
  - 开票日期选择
  - 收款方式选择（银行转账、现金、承兑汇票、支票）

- **明细管理**
  - 动态添加/删除明细行
  - 商品/服务名称输入
  - 规格、单位输入
  - 数量、单价输入，自动计算金额
  - 表格内编辑
  - 支持滚动（最大高度400px）

- **金额计算**
  - 自动计算销售总额
  - 税额输入
  - 折扣金额输入
  - 自动计算最终金额
  - 实时响应式更新

#### ✅ 查看详情
- 完整的发票信息展示（Descriptions组件）
- 明细表格显示
- 所有金额字段格式化
- 状态信息完整展示

#### ✅ 业务操作
- 确认发票（草稿状态）
- 删除发票（草稿状态）
- 二次确认提示

### 数据字段

```javascript
{
  invoiceNo: String,      // 发票编号
  customerId: Long,       // 客户ID
  invoiceDate: Date,      // 开票日期
  totalAmount: Decimal,   // 销售总额
  taxAmount: Decimal,     // 税额
  discountAmount: Decimal,// 折扣金额
  finalAmount: Decimal,   // 最终金额
  status: Enum,          // 状态
  paymentMethod: String,  // 收款方式
  isPosted: Boolean,     // 是否过账
  items: Array           // 明细列表
}
```

---

## 💰 费用报销管理 (ExpenseClaim.vue)

### 主要功能

#### ✅ 列表功能
- 关键词搜索（报销单编号）
- 状态筛选（草稿、已提交、已审批、已付款、已过账、已拒绝）
- 分页显示（后端分页）
- 金额格式化显示
- 状态标签彩色显示（6种状态）
- 过账状态显示

#### ✅ 新增/编辑功能
- **基本信息**
  - 自动生成报销单编号
  - 报销人选择（下拉列表）
  - 报销日期选择
  - 报销总额自动计算

- **明细管理**
  - 动态添加/删除明细行
  - 费用类型选择（8种类型）：
    - 交通费
    - 住宿费
    - 餐饮费
    - 会务费
    - 办公费
    - 招待费
    - 通讯费
    - 其他
  - 发生日期选择
  - 金额输入
  - 费用说明输入
  - 备注输入

- **金额计算**
  - 自动汇总所有明细金额
  - 实时更新报销总额

#### ✅ 查看详情
- 完整的报销单信息展示
- 明细表格显示
- 状态信息完整展示
- 过账信息显示

#### ✅ 业务操作
- 提交报销（草稿状态）
- 审批通过（已提交状态）
- 拒绝报销（已提交状态，需输入拒绝原因）
- 删除报销单（草稿状态）
- 所有操作都有二次确认

### 数据字段

```javascript
{
  claimNo: String,        // 报销单编号
  employeeId: Long,       // 报销人ID
  claimDate: Date,        // 报销日期
  totalAmount: Decimal,   // 报销总额
  status: Enum,          // 状态
  isPosted: Boolean,     // 是否过账
  items: Array           // 明细列表
}
```

### 费用明细字段

```javascript
{
  expenseType: String,    // 费用类型
  expenseDate: Date,      // 发生日期
  amount: Decimal,        // 金额
  description: String,    // 费用说明
  attachmentUrl: String,  // 附件URL
  remark: String         // 备注
}
```

---

## 🎨 UI/UX 特点

### 统一的用户体验
1. **状态标签颜色**
   - 草稿：灰色(info)
   - 已提交/已确认：橙色(warning)
   - 已审批/已发货/已收款/已付款/已过账：绿色(success)
   - 已拒绝/已取消：红色(danger)

2. **表单验证**
   - 必填字段标注
   - 实时验证反馈
   - 提交前验证

3. **操作反馈**
   - 成功提示
   - 错误提示
   - 二次确认对话框

4. **数据展示**
   - 金额格式化（¥123.45）
   - 日期格式统一
   - 状态图标化

### 响应式设计
- 对话框适配（900px宽度）
- 表格滚动支持
- 分页组件完整功能

### 操作便捷性
- 自动生成单据编号
- 自动计算金额
- 实时更新显示
- 快捷按钮操作

---

## 📡 API 接口

### 采购订单
```javascript
getPurchaseOrderList()              // 获取列表
getPurchaseOrderDetail(id)          // 获取详情
addPurchaseOrder(data)              // 新增
updatePurchaseOrder(data)           // 更新
deletePurchaseOrder(id)             // 删除
confirmPurchaseOrder(id)            // 确认
```

### 销售发票
```javascript
getSalesInvoiceList(params)         // 获取列表
getSalesInvoiceDetail(id)           // 获取详情
addSalesInvoice(data)               // 新增
updateSalesInvoice(data)            // 更新
deleteSalesInvoice(id)              // 删除
confirmSalesInvoice(id)             // 确认
```

### 费用报销
```javascript
getExpenseClaimList(params)         // 获取列表
getExpenseClaimDetail(id)           // 获取详情
addExpenseClaim(data)               // 新增
updateExpenseClaim(data)            // 更新
deleteExpenseClaim(id)              // 删除
submitExpenseClaim(id)              // 提交
approveExpenseClaim(id)             // 审批
rejectExpenseClaim(id, reason)      // 拒绝
```

---

## 🔄 业务流程

### 采购订单流程
```
草稿 → 已确认 → 已收货 → 已付款 → 已过账
```

### 销售发票流程
```
草稿 → 已确认 → 已发货 → 已收款 → 已过账
                      ↓
                   已取消
```

### 费用报销流程
```
草稿 → 已提交 → 已审批 → 已付款 → 已过账
           ↓
        已拒绝
```

---

## ✨ 技术亮点

1. **Vue 3 Composition API**
   - reactive 响应式数据
   - ref 引用类型
   - watch 监听器
   - onMounted 生命周期

2. **Element Plus 组件**
   - Table 表格
   - Form 表单
   - Dialog 对话框
   - Descriptions 描述列表
   - Pagination 分页
   - Tag 标签

3. **自动计算**
   - watch 监听明细变化
   - 自动汇总金额
   - 实时更新UI

4. **错误处理**
   - try-catch 捕获
   - 友好提示信息
   - 防止重复提交

5. **代码复用**
   - 统一的状态映射函数
   - 统一的数据加载逻辑
   - 统一的操作流程

---

## 📝 使用说明

### 新增单据
1. 点击"新增XXX"按钮
2. 填写基本信息
3. 添加明细行
4. 填写明细信息
5. 查看自动计算的金额
6. 点击"保存"

### 编辑单据
1. 找到草稿状态的单据
2. 点击"编辑"按钮
3. 修改信息
4. 点击"保存"

### 查看详情
1. 点击"查看"按钮
2. 查看完整信息
3. 查看明细列表

### 业务操作
1. **确认**：将草稿状态的单据确认
2. **提交**：将报销单提交审批
3. **审批**：审批通过报销单
4. **拒绝**：拒绝报销单并说明原因
5. **删除**：删除草稿状态的单据

---

## 🎯 后续优化建议

1. **功能增强**
   - [ ] 附件上传功能
   - [ ] 打印功能
   - [ ] 导出功能（Excel/PDF）
   - [ ] 批量操作
   - [ ] 审批流转记录

2. **性能优化**
   - [ ] 虚拟滚动（大数据量明细）
   - [ ] 防抖/节流
   - [ ] 懒加载

3. **用户体验**
   - [ ] 快捷键支持
   - [ ] 拖拽排序
   - [ ] 明细模板
   - [ ] 历史记录

---

## 📞 技术支持

如有问题，请查看：
- 项目文档
- API 文档
- 联系开发团队

**最后更新：2025-12-20**


/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80407
 Source Host           : localhost:3306
 Source Schema         : fs_system

 Target Server Type    : MySQL
 Target Server Version : 80407
 File Encoding         : 65001

 Date: 21/12/2025 21:52:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for acct_cash_flow_item
-- ----------------------------
DROP TABLE IF EXISTS `acct_cash_flow_item`;
CREATE TABLE `acct_cash_flow_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '现金流量项目ID',
  `item_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目编码',
  `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目名称',
  `category` enum('经营活动','投资活动','筹资活动') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动类别',
  `direction` enum('流入','流出') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '流向',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父项目ID',
  `display_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `is_enabled` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '说明',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `item_code`(`item_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '现金流量项目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acct_cash_flow_item
-- ----------------------------
INSERT INTO `acct_cash_flow_item` VALUES (1, 'CF_OP_01', '销售商品、提供劳务收到的现金', '经营活动', '流入', 0, 1, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (2, 'CF_OP_02', '收到的税费返还', '经营活动', '流入', 0, 2, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (3, 'CF_OP_03', '收到其他与经营活动有关的现金', '经营活动', '流入', 0, 3, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (4, 'CF_OP_04', '购买商品、接受劳务支付的现金', '经营活动', '流出', 0, 4, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (5, 'CF_OP_05', '支付给职工以及为职工支付的现金', '经营活动', '流出', 0, 5, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (6, 'CF_OP_06', '支付的各项税费', '经营活动', '流出', 0, 6, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (7, 'CF_OP_07', '支付其他与经营活动有关的现金', '经营活动', '流出', 0, 7, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (8, 'CF_INV_01', '收回投资收到的现金', '投资活动', '流入', 0, 11, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (9, 'CF_INV_02', '取得投资收益收到的现金', '投资活动', '流入', 0, 12, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (10, 'CF_INV_03', '处置固定资产、无形资产和其他长期资产收回的现金净额', '投资活动', '流入', 0, 13, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (11, 'CF_INV_04', '购建固定资产、无形资产和其他长期资产支付的现金', '投资活动', '流出', 0, 14, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (12, 'CF_INV_05', '投资支付的现金', '投资活动', '流出', 0, 15, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (13, 'CF_FIN_01', '吸收投资收到的现金', '筹资活动', '流入', 0, 21, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (14, 'CF_FIN_02', '取得借款收到的现金', '筹资活动', '流入', 0, 22, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (15, 'CF_FIN_03', '偿还债务支付的现金', '筹资活动', '流出', 0, 23, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (16, 'CF_FIN_04', '分配股利、利润或偿付利息支付的现金', '筹资活动', '流出', 0, 24, 1, NULL, '2025-12-21 02:27:52');
INSERT INTO `acct_cash_flow_item` VALUES (17, 'CF_FIN_05', '支付其他与筹资活动有关的现金', '筹资活动', '流出', 0, 25, 1, NULL, '2025-12-21 02:27:52');

-- ----------------------------
-- Table structure for acct_entry_cash_flow
-- ----------------------------
DROP TABLE IF EXISTS `acct_entry_cash_flow`;
CREATE TABLE `acct_entry_cash_flow`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '映射ID',
  `entry_id` bigint NOT NULL COMMENT '凭证分录ID',
  `cash_flow_item_id` bigint NOT NULL COMMENT '现金流量项目ID',
  `amount` decimal(18, 2) NOT NULL COMMENT '金额',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_entry_id`(`entry_id` ASC) USING BTREE,
  INDEX `idx_cash_flow_item`(`cash_flow_item_id` ASC) USING BTREE,
  CONSTRAINT `acct_entry_cash_flow_ibfk_1` FOREIGN KEY (`entry_id`) REFERENCES `acct_voucher_entry` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `acct_entry_cash_flow_ibfk_2` FOREIGN KEY (`cash_flow_item_id`) REFERENCES `acct_cash_flow_item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '凭证分录现金流量映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acct_entry_cash_flow
-- ----------------------------
INSERT INTO `acct_entry_cash_flow` VALUES (1, 9, 1, 113000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (2, 17, 1, 167500.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (3, 31, 1, 134100.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (4, 5, 4, 56500.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (5, 25, 4, 89400.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (7, 56, 5, 80000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (8, 58, 6, 35000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (9, 5, 7, 56500.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (10, 12, 7, 3500.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (11, 34, 7, 2800.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (12, 60, 7, 8000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (13, 62, 7, 30000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (16, 62, 11, 30000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (17, 1, 13, 10000000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (18, 63, 14, 200000.00, '2025-12-21 02:27:52');
INSERT INTO `acct_entry_cash_flow` VALUES (19, 66, 16, 1500.00, '2025-12-21 02:27:52');

-- ----------------------------
-- Table structure for acct_periodic_operation
-- ----------------------------
DROP TABLE IF EXISTS `acct_periodic_operation`;
CREATE TABLE `acct_periodic_operation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '定期业务ID',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务类型（计提折旧、摊销、结转等）',
  `operation_period` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务期间（如：2025-01）',
  `voucher_id` bigint NULL DEFAULT NULL COMMENT '关联凭证ID',
  `amount` decimal(18, 2) NOT NULL COMMENT '金额',
  `status` enum('Pending','Executed','Cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Pending' COMMENT '状态',
  `executed_at` datetime NULL DEFAULT NULL COMMENT '执行时间',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '说明',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operation_period`(`operation_period` ASC) USING BTREE,
  INDEX `idx_operation_type`(`operation_type` ASC) USING BTREE,
  INDEX `fk_periodic_voucher`(`voucher_id` ASC) USING BTREE,
  CONSTRAINT `fk_periodic_voucher` FOREIGN KEY (`voucher_id`) REFERENCES `acct_voucher` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定期业务记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of acct_periodic_operation
-- ----------------------------
INSERT INTO `acct_periodic_operation` VALUES (1, '计提折旧', '2025-01', 8, 500.00, 'Executed', '2025-01-31 17:00:00', '1月办公电脑折旧', '按5年折旧，每月500元', '2025-01-31 16:00:00');
INSERT INTO `acct_periodic_operation` VALUES (2, '工资计提', '2025-01', 9, 50000.00, 'Executed', '2025-01-31 17:30:00', '1月全员工资计提', '5名员工，平均月薪1万元', '2025-01-31 16:30:00');
INSERT INTO `acct_periodic_operation` VALUES (3, '计提折旧', '2025-01', NULL, 833.33, 'Executed', '2025-12-21 01:48:55', '计提1月份固定资产折旧', '办公设备按5年折旧，每月833.33元', '2025-01-25 10:00:00');
INSERT INTO `acct_periodic_operation` VALUES (4, '工资计提', '2025-01', 22, 80000.00, 'Executed', '2025-12-21 01:49:12', '计提1月份员工工资', '管理人员工资', '2025-01-25 10:30:00');
INSERT INTO `acct_periodic_operation` VALUES (5, '费用摊销', '2025-01', NULL, 500.00, 'Executed', '2025-12-21 01:49:13', '1月份长期待摊费用摊销', '装修费用按2年摊销', '2025-01-25 11:00:00');
INSERT INTO `acct_periodic_operation` VALUES (6, '计提折旧', '2025-02', 21, 833.33, 'Executed', '2025-12-21 01:49:10', '计提2月份固定资产折旧', '办公设备按5年折旧，每月833.33元', '2025-02-20 10:00:00');
INSERT INTO `acct_periodic_operation` VALUES (7, '工资计提', '2025-02', 20, 85000.00, 'Executed', '2025-12-21 01:49:09', '计提2月份员工工资', '管理人员工资（含年终奖）', '2025-02-20 10:30:00');
INSERT INTO `acct_periodic_operation` VALUES (8, '费用摊销', '2025-02', 19, 500.00, 'Executed', '2025-12-21 01:49:07', '2月份长期待摊费用摊销', '装修费用按2年摊销', '2025-02-20 11:00:00');
INSERT INTO `acct_periodic_operation` VALUES (9, '利息计提', '2025-02', 18, 2000.00, 'Executed', '2025-12-21 01:49:05', '计提2月份借款利息', '短期借款利息', '2025-02-20 11:30:00');
INSERT INTO `acct_periodic_operation` VALUES (10, '计提折旧', '2025-03', 17, 833.33, 'Executed', '2025-12-21 01:49:03', '计提3月份固定资产折旧', '办公设备按5年折旧，每月833.33元', '2025-03-20 10:00:00');
INSERT INTO `acct_periodic_operation` VALUES (11, '工资计提', '2025-03', 16, 80000.00, 'Executed', '2025-12-21 01:48:59', '计提3月份员工工资', '管理人员工资', '2025-03-20 10:30:00');
INSERT INTO `acct_periodic_operation` VALUES (12, '费用摊销', '2025-03', 15, 500.00, 'Executed', '2025-12-21 01:48:58', '3月份长期待摊费用摊销', '装修费用按2年摊销', '2025-03-20 11:00:00');

-- ----------------------------
-- Table structure for acct_posting_record
-- ----------------------------
DROP TABLE IF EXISTS `acct_posting_record`;
CREATE TABLE `acct_posting_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '过账记录ID',
  `source_type` enum('Purchase','Sales','Expense') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '来源类型',
  `source_id` bigint NOT NULL COMMENT '来源单据ID',
  `source_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '来源单据编号',
  `voucher_id` bigint NULL DEFAULT NULL COMMENT '鐢熸垚鐨勫嚟璇両D',
  `voucher_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '凭证编号',
  `posted_amount` decimal(18, 2) NOT NULL COMMENT '过账金额',
  `posted_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Admin' COMMENT '过账人',
  `posted_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过账时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_source`(`source_type` ASC, `source_id` ASC) USING BTREE,
  INDEX `idx_voucher_id`(`voucher_id` ASC) USING BTREE,
  INDEX `idx_posted_at`(`posted_at` ASC) USING BTREE,
  CONSTRAINT `fk_posting_voucher` FOREIGN KEY (`voucher_id`) REFERENCES `acct_voucher` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '过账记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of acct_posting_record
-- ----------------------------
INSERT INTO `acct_posting_record` VALUES (1, 'Purchase', 1, 'PO-2025-001', 2, 'JZ-2025-002', 56500.00, 'System', '2025-01-10 14:00:00', '采购过账');
INSERT INTO `acct_posting_record` VALUES (2, 'Sales', 1, 'INV-2025-001', 3, 'JZ-2025-003', 113000.00, 'System', '2025-01-12 15:00:00', '销售过账');
INSERT INTO `acct_posting_record` VALUES (3, 'Expense', 1, 'EXP-2025-001', 5, 'JZ-2025-005', 3500.00, 'System', '2025-01-22 10:00:00', '费用报销过账');
INSERT INTO `acct_posting_record` VALUES (4, 'Sales', 2, 'INV-2025-002', 6, 'JZ-2025-006', 167500.00, 'System', '2025-01-27 14:00:00', '销售过账');
INSERT INTO `acct_posting_record` VALUES (5, 'Purchase', 2, 'PO-2025-002', 10, 'JZ-2025-010', 89400.00, 'System', '2025-02-12 14:00:00', '采购过账');
INSERT INTO `acct_posting_record` VALUES (6, 'Sales', 3, 'INV-2025-003', 11, 'JZ-2025-011', 134100.00, 'System', '2025-02-17 15:00:00', '销售过账');
INSERT INTO `acct_posting_record` VALUES (7, 'Expense', 2, 'EXP-2025-002', 13, 'JZ-2025-013', 2800.00, 'System', '2025-02-22 10:00:00', '费用报销过账');

-- ----------------------------
-- Table structure for acct_reconciliation
-- ----------------------------
DROP TABLE IF EXISTS `acct_reconciliation`;
CREATE TABLE `acct_reconciliation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '对账记录ID',
  `subject_id` bigint NOT NULL COMMENT '对账科目ID',
  `reconciliation_date` date NOT NULL COMMENT '对账日期',
  `book_balance` decimal(18, 2) NOT NULL COMMENT '账面余额',
  `bank_balance` decimal(18, 2) NULL DEFAULT NULL COMMENT '银行余额',
  `difference_amount` decimal(18, 2) NULL DEFAULT NULL COMMENT '差异金额',
  `status` enum('Pending','Reconciled','Discrepancy') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Pending' COMMENT '对账状态',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `reconciled_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Admin' COMMENT '对账人',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_subject_id`(`subject_id` ASC) USING BTREE,
  INDEX `idx_reconciliation_date`(`reconciliation_date` ASC) USING BTREE,
  CONSTRAINT `acct_reconciliation_ibfk_1` FOREIGN KEY (`subject_id`) REFERENCES `acct_subject` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '对账记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of acct_reconciliation
-- ----------------------------
INSERT INTO `acct_reconciliation` VALUES (1, 2, '2025-01-31', 10220500.00, 10220500.00, 0.00, 'Reconciled', '1月末银行对账', 'Admin', '2025-01-31 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (2, 1, '2025-01-31', 5000.00, 5000.00, 0.00, 'Reconciled', '1月末现金盘点', 'Admin', '2025-01-31 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (3, 2, '2025-01-02', 10000000.00, 10000000.00, 0.00, 'Reconciled', '注册资本到账，账实相符', 'Admin', '2025-01-02 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (4, 2, '2025-01-10', 9943500.00, 9943500.00, 0.00, 'Reconciled', '采购设备付款后对账', 'Admin', '2025-01-10 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (5, 2, '2025-01-15', 10056500.00, 10056500.00, 0.00, 'Reconciled', '收到销售货款', 'Admin', '2025-01-15 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (6, 2, '2025-01-20', 10053000.00, 10053000.00, 0.00, 'Reconciled', '费用报销后对账', 'Admin', '2025-01-20 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (7, 2, '2025-01-30', 10220500.00, 10220500.00, 0.00, 'Reconciled', '收到第二笔销售货款', 'Admin', '2025-01-30 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (8, 2, '2025-01-31', 10220500.00, 10220500.00, 0.00, 'Reconciled', '1月末银行对账，账实相符', 'Admin', '2025-01-31 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (9, 1, '2025-01-31', 5000.00, 5000.00, 0.00, 'Reconciled', '1月末现金盘点，账实相符', 'Admin', '2025-01-31 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (10, 2, '2025-02-10', 10131100.00, 10131100.00, 0.00, 'Reconciled', '采购原材料付款后对账', 'Admin', '2025-02-10 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (11, 2, '2025-02-20', 10262400.00, 10262400.00, 0.00, 'Reconciled', '收到销售货款并支付报销', 'Admin', '2025-02-20 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (12, 2, '2025-02-28', 10262400.00, 10262400.00, 0.00, 'Reconciled', '2月末银行对账，账实相符', 'Admin', '2025-02-28 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (13, 1, '2025-02-28', 5000.00, 5000.00, 0.00, 'Reconciled', '2月末现金盘点，账实相符', 'Admin', '2025-02-28 18:00:00');
INSERT INTO `acct_reconciliation` VALUES (14, 1, '2025-12-20', 0.00, 0.00, 0.00, 'Reconciled', '请手动输入银行余额', 'System', '2025-12-21 01:47:54');
INSERT INTO `acct_reconciliation` VALUES (15, 2, '2025-12-20', 10262400.00, 10262400.00, 0.00, 'Reconciled', '请手动输入银行余额', 'System', '2025-12-21 01:47:54');
INSERT INTO `acct_reconciliation` VALUES (16, 3, '2025-12-20', 0.00, 0.00, 0.00, 'Reconciled', '请手动输入银行余额', 'System', '2025-12-21 01:47:54');

-- ----------------------------
-- Table structure for acct_subject
-- ----------------------------
DROP TABLE IF EXISTS `acct_subject`;
CREATE TABLE `acct_subject`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '科目ID',
  `subject_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科目编码（如1001，1002）',
  `subject_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科目名称',
  `subject_type` enum('Asset','Liability','Equity','Income','Expense') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科目类型',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父科目ID，0表示顶级科目',
  `level` int NULL DEFAULT 1 COMMENT '科目层级（1-顶级，2-二级...）',
  `is_leaf` tinyint(1) NULL DEFAULT 1 COMMENT '是否叶子节点（1-是，0-否）',
  `balance_direction` enum('Debit','Credit') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '余额方向（借方/贷方）',
  `is_enabled` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用（1-启用，0-停用）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '科目说明',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `subject_code`(`subject_code` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_subject_type`(`subject_type` ASC) USING BTREE,
  INDEX `idx_subject_code`(`subject_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '会计科目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of acct_subject
-- ----------------------------
INSERT INTO `acct_subject` VALUES (1, '1001', '库存现金', 'Asset', 0, 1, 1, 'Debit', 1, '企业库存的现金', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (2, '1002', '银行存款', 'Asset', 0, 1, 1, 'Debit', 1, '企业存放在银行的款项', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (3, '1012', '其他货币资金', 'Asset', 0, 1, 1, 'Debit', 1, '企业除现金和银行存款以外的货币资金', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (4, '1101', '短期投资', 'Asset', 0, 1, 1, 'Debit', 1, '企业购入的短期投资', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (5, '1121', '应收票据', 'Asset', 0, 1, 1, 'Debit', 1, '企业持有的应收票据', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (6, '1122', '应收账款', 'Asset', 0, 1, 1, 'Debit', 1, '企业因销售商品、提供劳务等应收的款项', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (7, '1123', '预付账款', 'Asset', 0, 1, 1, 'Debit', 1, '企业按照合同规定预付的款项', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (8, '1231', '其他应收款', 'Asset', 0, 1, 1, 'Debit', 1, '企业除应收票据、应收账款以外的其他应收款项', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (9, '1401', '材料采购', 'Asset', 0, 1, 1, 'Debit', 1, '企业采购材料', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (10, '1402', '在途物资', 'Asset', 0, 1, 1, 'Debit', 1, '企业已购入但尚未运达的物资', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (11, '1403', '原材料', 'Asset', 0, 1, 1, 'Debit', 1, '企业库存的各种材料', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (12, '1405', '库存商品', 'Asset', 0, 1, 1, 'Debit', 1, '企业库存的各种商品', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (13, '1601', '固定资产', 'Asset', 0, 1, 1, 'Debit', 1, '企业持有的使用期限超过一年的房屋、建筑物、机器等', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (14, '1602', '累计折旧', 'Asset', 0, 1, 1, 'Credit', 1, '固定资产的累计折旧额', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (15, '1701', '无形资产', 'Asset', 0, 1, 1, 'Debit', 1, '企业拥有或控制的没有实物形态的可辨认非货币性资产', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (16, '1801', '长期待摊费用', 'Asset', 0, 1, 1, 'Debit', 1, '企业已经支出，但摊销期限在1年以上的各项费用', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (17, '2001', '短期借款', 'Liability', 0, 1, 1, 'Credit', 1, '企业向银行或其他金融机构借入的期限在1年以下的借款', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (18, '2201', '应付票据', 'Liability', 0, 1, 1, 'Credit', 1, '企业购买材料、商品等开出、承兑的商业汇票', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (19, '2202', '应付账款', 'Liability', 0, 1, 1, 'Credit', 1, '企业因购买材料、商品和接受劳务等应付的款项', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (20, '2203', '预收账款', 'Liability', 0, 1, 1, 'Credit', 1, '企业按照合同规定预收的款项', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (21, '2211', '应付职工薪酬', 'Liability', 0, 1, 1, 'Credit', 1, '企业根据有关规定应付给职工的各种薪酬', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (22, '2221', '应交税费', 'Liability', 0, 1, 1, 'Credit', 1, '企业按照税法规定计算应交纳的各种税费', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (23, '2231', '应付利息', 'Liability', 0, 1, 1, 'Credit', 1, '企业按照合同约定应支付的利息', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (24, '2232', '应付利润', 'Liability', 0, 1, 1, 'Credit', 1, '企业应付给投资者的利润', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (25, '2241', '其他应付款', 'Liability', 0, 1, 1, 'Credit', 1, '企业除应付票据、应付账款等以外的其他应付款项', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (26, '2501', '长期借款', 'Liability', 0, 1, 1, 'Credit', 1, '企业向银行或其他金融机构借入的期限在1年以上的借款', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (27, '4001', '实收资本', 'Equity', 0, 1, 1, 'Credit', 1, '投资者按照企业章程或合同、协议的约定，实际投入企业的资本', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (28, '4002', '资本公积', 'Equity', 0, 1, 1, 'Credit', 1, '企业收到投资者出资额超出其在注册资本或股本中所占份额的部分', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (29, '4101', '盈余公积', 'Equity', 0, 1, 1, 'Credit', 1, '企业按规定从净利润中提取的各种积累资金', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (30, '4103', '本年利润', 'Equity', 0, 1, 1, 'Credit', 1, '企业当期实现的净利润或发生的净亏损', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (31, '4104', '利润分配', 'Equity', 0, 1, 1, 'Credit', 1, '企业利润的分配或亏损的弥补', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (32, '6001', '主营业务收入', 'Income', 0, 1, 1, 'Credit', 1, '企业从事主要经营业务取得的收入', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (33, '6051', '其他业务收入', 'Income', 0, 1, 1, 'Credit', 1, '企业除主营业务以外的其他销售或其他业务的收入', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (34, '6111', '投资收益', 'Income', 0, 1, 1, 'Credit', 1, '企业对外投资取得的收益或发生的损失', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (35, '6301', '营业外收入', 'Income', 0, 1, 1, 'Credit', 1, '企业发生的与其生产经营无直接关系的各项收入', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (36, '6401', '主营业务成本', 'Expense', 0, 1, 1, 'Debit', 1, '企业确认主营业务收入时应结转的成本', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (37, '6402', '其他业务成本', 'Expense', 0, 1, 1, 'Debit', 1, '企业确认其他业务收入时应结转的成本', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (38, '6403', '税金及附加', 'Expense', 0, 1, 1, 'Debit', 1, '企业经营活动发生的消费税、城市维护建设税等', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (39, '6601', '销售费用', 'Expense', 0, 1, 1, 'Debit', 1, '企业销售商品和材料、提供劳务的过程中发生的各种费用', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (40, '6602', '管理费用', 'Expense', 0, 1, 1, 'Debit', 1, '企业为组织和管理企业生产经营所发生的费用', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (41, '6603', '财务费用', 'Expense', 0, 1, 1, 'Debit', 1, '企业为筹集生产经营所需资金等而发生的费用', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (42, '6701', '资产减值损失', 'Expense', 0, 1, 1, 'Debit', 1, '企业计提各项资产减值准备所形成的损失', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (43, '6711', '营业外支出', 'Expense', 0, 1, 1, 'Debit', 1, '企业发生的与其生产经营无直接关系的各项支出', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `acct_subject` VALUES (44, '6801', '所得税费用', 'Expense', 0, 1, 1, 'Debit', 1, '企业确认的应从当期利润总额中扣除的所得税费用', '2025-12-21 01:46:23', '2025-12-21 01:46:23');

-- ----------------------------
-- Table structure for acct_tax_record
-- ----------------------------
DROP TABLE IF EXISTS `acct_tax_record`;
CREATE TABLE `acct_tax_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '税务记录ID',
  `tax_period` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '税期（如：2025-01）',
  `tax_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '税种（增值税、企业所得税等）',
  `taxable_amount` decimal(18, 2) NOT NULL COMMENT '应税金额',
  `tax_rate` decimal(5, 2) NOT NULL COMMENT '税率',
  `tax_amount` decimal(18, 2) NOT NULL COMMENT '税额',
  `status` enum('Pending','Declared','Paid') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Pending' COMMENT '状态',
  `declare_date` date NULL DEFAULT NULL COMMENT '申报日期',
  `payment_date` date NULL DEFAULT NULL COMMENT '缴纳日期',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tax_period`(`tax_period` ASC) USING BTREE,
  INDEX `idx_tax_type`(`tax_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '税务处理记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of acct_tax_record
-- ----------------------------
INSERT INTO `acct_tax_record` VALUES (1, '2025-01', '增值税', 250000.00, 13.00, 26000.00, 'Declared', '2025-02-10', NULL, '1月增值税申报', '2025-02-01 09:00:00');
INSERT INTO `acct_tax_record` VALUES (2, '2025-01', '城市维护建设税', 26000.00, 7.00, 1820.00, 'Declared', '2025-02-10', NULL, '基于增值税计算', '2025-02-01 09:15:00');
INSERT INTO `acct_tax_record` VALUES (3, '2025-01', '教育费附加', 26000.00, 3.00, 780.00, 'Declared', '2025-02-10', NULL, '基于增值税计算', '2025-02-01 09:30:00');
INSERT INTO `acct_tax_record` VALUES (4, '2025-01', '企业所得税', 196000.00, 25.00, 49000.00, 'Declared', '2025-02-15', NULL, '1月所得税预缴', '2025-02-01 10:00:00');
INSERT INTO `acct_tax_record` VALUES (5, '2025-02', '增值税', 120000.00, 13.00, 5200.00, 'Declared', '2025-03-10', NULL, '2月增值税申报', '2025-03-01 09:00:00');
INSERT INTO `acct_tax_record` VALUES (6, '2025-02', '城市维护建设税', 5200.00, 7.00, 364.00, 'Paid', '2025-03-10', NULL, '基于增值税计算', '2025-03-01 09:15:00');
INSERT INTO `acct_tax_record` VALUES (7, '2025-02', '教育费附加', 5200.00, 3.00, 156.00, 'Paid', '2025-03-10', NULL, '基于增值税计算', '2025-03-01 09:30:00');
INSERT INTO `acct_tax_record` VALUES (8, '2025-02', '企业所得税', 117000.00, 25.00, 29250.00, 'Paid', '2025-03-15', NULL, '2月所得税预缴', '2025-03-01 10:00:00');

-- ----------------------------
-- Table structure for acct_voucher
-- ----------------------------
DROP TABLE IF EXISTS `acct_voucher`;
CREATE TABLE `acct_voucher`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '凭证ID',
  `voucher_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '凭证编号（如：记-001）',
  `voucher_date` date NOT NULL COMMENT '凭证日期',
  `voucher_type` enum('Manual','Auto') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Manual' COMMENT '凭证类型（手工/自动）',
  `source_type` enum('Manual','Purchase','Sales','Expense') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Manual' COMMENT '来源类型',
  `source_id` bigint NULL DEFAULT NULL COMMENT '来源单据ID（业务单据ID）',
  `total_debit` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '借方总额',
  `total_credit` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '贷方总额',
  `status` enum('Draft','Posted','Cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Draft' COMMENT '状态（草稿/已记账/已作废）',
  `posted_at` datetime NULL DEFAULT NULL COMMENT '记账时间',
  `abstract` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '摘要',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Admin' COMMENT '制单人',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `voucher_no`(`voucher_no` ASC) USING BTREE,
  INDEX `idx_voucher_no`(`voucher_no` ASC) USING BTREE,
  INDEX `idx_voucher_date`(`voucher_date` ASC) USING BTREE,
  INDEX `idx_source`(`source_type` ASC, `source_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '凭证主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of acct_voucher
-- ----------------------------
INSERT INTO `acct_voucher` VALUES (1, 'JZ-2025-001', '2025-01-02', 'Manual', 'Manual', NULL, 10000000.00, 10000000.00, 'Posted', '2025-01-02 10:00:00', '收到股东注册资本', NULL, 'Admin', '2025-01-02 09:30:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (2, 'JZ-2025-002', '2025-01-08', 'Auto', 'Purchase', 1, 56500.00, 56500.00, 'Posted', '2025-01-10 14:00:00', '采购办公设备-PO-2025-001', NULL, 'System', '2025-01-10 14:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (3, 'JZ-2025-003', '2025-01-10', 'Auto', 'Sales', 1, 113000.00, 113000.00, 'Posted', '2025-01-12 15:00:00', '销售产品-INV-2025-001', NULL, 'System', '2025-01-12 15:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (4, 'JZ-2025-004', '2025-01-15', 'Manual', 'Manual', NULL, 113000.00, 113000.00, 'Posted', '2025-01-15 16:00:00', '收到北京科技公司货款', NULL, 'Admin', '2025-01-15 15:30:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (5, 'JZ-2025-005', '2025-01-20', 'Auto', 'Expense', 1, 3500.00, 3500.00, 'Posted', '2025-01-22 10:00:00', '差旅费报销-EXP-2025-001', NULL, 'System', '2025-01-22 10:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (6, 'JZ-2025-006', '2025-01-25', 'Auto', 'Sales', 2, 167500.00, 167500.00, 'Posted', '2025-01-27 14:00:00', '销售产品-INV-2025-002', NULL, 'System', '2025-01-27 14:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (7, 'JZ-2025-007', '2025-01-30', 'Manual', 'Manual', NULL, 167500.00, 167500.00, 'Posted', '2025-01-30 15:00:00', '收到上海贸易公司货款', NULL, 'Admin', '2025-01-30 14:30:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (8, 'JZ-2025-008', '2025-01-31', 'Manual', 'Manual', NULL, 500.00, 500.00, 'Posted', '2025-01-31 17:00:00', '计提1月固定资产折旧', NULL, 'Admin', '2025-01-31 17:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (9, 'JZ-2025-009', '2025-01-31', 'Manual', 'Manual', NULL, 50000.00, 50000.00, 'Posted', '2025-01-31 17:30:00', '计提1月员工工资', NULL, 'Admin', '2025-01-31 17:30:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (10, 'JZ-2025-010', '2025-02-10', 'Auto', 'Purchase', 2, 89400.00, 89400.00, 'Posted', '2025-02-12 14:00:00', '采购原材料-PO-2025-002', NULL, 'System', '2025-02-12 14:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (11, 'JZ-2025-011', '2025-02-15', 'Auto', 'Sales', 3, 134100.00, 134100.00, 'Posted', '2025-02-17 15:00:00', '销售产品-INV-2025-003', NULL, 'System', '2025-02-17 15:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (12, 'JZ-2025-012', '2025-02-20', 'Manual', 'Manual', NULL, 134100.00, 134100.00, 'Posted', '2025-02-20 16:00:00', '收到杭州电商公司货款', NULL, 'Admin', '2025-02-20 15:30:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (13, 'JZ-2025-013', '2025-02-20', 'Auto', 'Expense', 2, 2800.00, 2800.00, 'Posted', '2025-02-22 10:00:00', '差旅费报销-EXP-2025-002', NULL, 'System', '2025-02-22 10:00:00', '2025-12-21 01:46:23');
INSERT INTO `acct_voucher` VALUES (15, '记-002', '2025-03-31', 'Manual', 'Manual', NULL, 5000.00, 5000.00, 'Posted', '2025-12-21 15:42:23', '3月份长期待摊费用摊销', '定期业务自动生成：费用摊销', 'System', '2025-12-21 01:48:57', '2025-12-21 15:42:23');
INSERT INTO `acct_voucher` VALUES (16, '记-003', '2025-03-31', 'Auto', 'Manual', NULL, 80000.00, 80000.00, 'Posted', '2025-12-21 02:57:00', '计提3月份员工工资', '定期业务自动生成：工资计提', 'System', '2025-12-21 01:48:58', '2025-12-21 02:57:00');
INSERT INTO `acct_voucher` VALUES (17, '记-004', '2025-03-31', 'Auto', 'Manual', NULL, 833.33, 833.33, 'Posted', '2025-12-21 02:50:23', '计提3月份固定资产折旧', '定期业务自动生成：计提折旧', 'System', '2025-12-21 01:49:03', '2025-12-21 02:50:23');
INSERT INTO `acct_voucher` VALUES (18, '记-005', '2025-02-28', 'Auto', 'Manual', NULL, 2000.00, 2000.00, 'Posted', '2025-12-21 13:55:05', '计提2月份借款利息', '定期业务自动生成：利息计提', 'System', '2025-12-21 01:49:04', '2025-12-21 13:55:05');
INSERT INTO `acct_voucher` VALUES (19, '记-006', '2025-02-28', 'Auto', 'Manual', NULL, 500.00, 500.00, 'Posted', '2025-12-21 15:37:55', '2月份长期待摊费用摊销', '定期业务自动生成：费用摊销', 'System', '2025-12-21 01:49:06', '2025-12-21 15:37:55');
INSERT INTO `acct_voucher` VALUES (20, '记-007', '2025-02-28', 'Auto', 'Manual', NULL, 85000.00, 85000.00, 'Posted', '2025-12-21 15:37:55', '计提2月份员工工资', '定期业务自动生成：工资计提', 'System', '2025-12-21 01:49:08', '2025-12-21 15:37:55');
INSERT INTO `acct_voucher` VALUES (21, '记-008', '2025-02-28', 'Auto', 'Manual', NULL, 833.33, 833.33, 'Posted', '2025-12-21 15:37:55', '计提2月份固定资产折旧', '定期业务自动生成：计提折旧', 'System', '2025-12-21 01:49:10', '2025-12-21 15:37:55');
INSERT INTO `acct_voucher` VALUES (22, '记-009', '2025-01-31', 'Auto', 'Manual', NULL, 80000.00, 80000.00, 'Draft', NULL, '计提1月份员工工资', '定期业务自动生成：工资计提', 'System', '2025-12-21 01:49:11', '2025-12-21 01:49:11');
INSERT INTO `acct_voucher` VALUES (24, 'JZ-2025-014', '2025-01-31', 'Manual', 'Manual', NULL, 80000.00, 80000.00, 'Posted', '2025-01-31 18:00:00', '支付1月份员工工资', NULL, 'Admin', '2025-01-31 18:00:00', '2025-12-21 02:11:37');
INSERT INTO `acct_voucher` VALUES (25, 'JZ-2025-015', '2025-02-15', 'Manual', 'Manual', NULL, 35000.00, 35000.00, 'Posted', '2025-02-15 18:00:00', '支付1月份增值税及附加', NULL, 'Admin', '2025-02-15 18:00:00', '2025-12-21 02:11:37');
INSERT INTO `acct_voucher` VALUES (26, 'JZ-2025-016', '2025-02-20', 'Manual', 'Manual', NULL, 8000.00, 8000.00, 'Posted', '2025-02-20 18:00:00', '支付办公费用', NULL, 'Admin', '2025-02-20 18:00:00', '2025-12-21 02:11:37');
INSERT INTO `acct_voucher` VALUES (27, 'JZ-2025-017', '2025-02-25', 'Manual', 'Manual', NULL, 30000.00, 30000.00, 'Posted', '2025-02-25 18:00:00', '购买办公家具', NULL, 'Admin', '2025-02-25 18:00:00', '2025-12-21 02:11:37');
INSERT INTO `acct_voucher` VALUES (28, 'JZ-2025-018', '2025-02-10', 'Manual', 'Manual', NULL, 200000.00, 200000.00, 'Posted', '2025-02-10 18:00:00', '取得银行短期借款', NULL, 'Admin', '2025-02-10 18:00:00', '2025-12-21 02:11:37');
INSERT INTO `acct_voucher` VALUES (29, 'JZ-2025-019', '2025-02-28', 'Manual', 'Manual', NULL, 1500.00, 1500.00, 'Posted', '2025-02-28 18:00:00', '支付借款利息', NULL, 'Admin', '2025-02-28 18:00:00', '2025-12-21 02:11:37');
INSERT INTO `acct_voucher` VALUES (30, 'JZ-2025-020', '2025-01-01', 'Manual', 'Manual', NULL, 12000.00, 12000.00, 'Posted', '2025-01-01 10:00:00', 'Prepay decoration expenses for 24 months', NULL, 'Admin', '2025-01-01 10:00:00', '2025-12-21 15:30:00');
INSERT INTO `acct_voucher` VALUES (31, 'JZ-2025-CLOSE', '2025-03-31', 'Manual', 'Manual', NULL, 683799.99, 683799.99, 'Posted', '2025-03-31 23:59:59', 'Period-end closing - transfer all profit and loss accounts', 'Close all revenue and expenses to retained earnings', 'System', '2025-12-21 16:00:00', '2025-12-21 15:21:49');
INSERT INTO `acct_voucher` VALUES (38, '记-010', '2025-12-21', 'Manual', 'Manual', NULL, 100.00, 100.00, 'Posted', '2025-12-21 15:51:28', '测试', '', 'Admin', '2025-12-21 15:48:45', '2025-12-21 15:51:28');
INSERT INTO `acct_voucher` VALUES (39, '记-011', '2025-12-21', 'Auto', 'Purchase', 3, 62.00, 62.00, 'Posted', NULL, '采购订单过账：PO1766303437118', NULL, 'Admin', '2025-12-21 15:51:55', '2025-12-21 15:51:55');

-- ----------------------------
-- Table structure for acct_voucher_entry
-- ----------------------------
DROP TABLE IF EXISTS `acct_voucher_entry`;
CREATE TABLE `acct_voucher_entry`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分录ID',
  `voucher_id` bigint NOT NULL COMMENT '凭证ID',
  `line_number` int NOT NULL COMMENT '分录行号',
  `subject_id` bigint NOT NULL COMMENT '会计科目ID',
  `debit_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '借方金额',
  `credit_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '贷方金额',
  `abstract` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '摘要',
  `auxiliary_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '辅助信息（如：往来单位、项目等）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_voucher_id`(`voucher_id` ASC) USING BTREE,
  INDEX `idx_subject_id`(`subject_id` ASC) USING BTREE,
  CONSTRAINT `acct_voucher_entry_ibfk_1` FOREIGN KEY (`voucher_id`) REFERENCES `acct_voucher` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `acct_voucher_entry_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `acct_subject` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '凭证分录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of acct_voucher_entry
-- ----------------------------
INSERT INTO `acct_voucher_entry` VALUES (1, 1, 1, 2, 10000000.00, 0.00, '银行收到股东投资', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (2, 1, 2, 27, 0.00, 10000000.00, '实收资本-股东投资', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (3, 2, 1, 13, 50000.00, 0.00, '固定资产-办公电脑', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (4, 2, 2, 22, 6500.00, 0.00, '应交税费-增值税进项', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (5, 2, 3, 2, 0.00, 56500.00, '银行存款支付', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (6, 3, 1, 6, 113000.00, 0.00, '应收账款-北京科技公司', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (7, 3, 2, 32, 0.00, 100000.00, '主营业务收入', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (8, 3, 3, 22, 0.00, 13000.00, '应交税费-增值税销项', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (9, 4, 1, 2, 113000.00, 0.00, '银行存款', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (10, 4, 2, 6, 0.00, 113000.00, '应收账款-北京科技公司', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (11, 5, 1, 36, 3500.00, 0.00, '销售费用-差旅费', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (12, 5, 2, 2, 0.00, 3500.00, '银行存款支付', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (13, 6, 1, 6, 167500.00, 0.00, '应收账款-上海贸易公司', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (14, 6, 2, 32, 0.00, 150000.00, '主营业务收入', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (15, 6, 3, 22, 0.00, 19500.00, '应交税费-增值税销项', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (16, 6, 4, 32, 2000.00, 0.00, '销售折扣', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (17, 7, 1, 2, 167500.00, 0.00, '银行存款', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (18, 7, 2, 6, 0.00, 167500.00, '应收账款-上海贸易公司', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (19, 8, 1, 37, 500.00, 0.00, '管理费用-折旧费', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (20, 8, 2, 14, 0.00, 500.00, '累计折旧', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (21, 9, 1, 37, 50000.00, 0.00, '管理费用-工资', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (22, 9, 2, 21, 0.00, 50000.00, '应付职工薪酬', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (23, 10, 1, 11, 80000.00, 0.00, '原材料-钢材', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (24, 10, 2, 22, 10400.00, 0.00, '应交税费-增值税进项', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (25, 10, 3, 2, 0.00, 89400.00, '银行存款支付', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (26, 10, 4, 11, 0.00, 1000.00, '采购折扣', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (27, 11, 1, 6, 134100.00, 0.00, '应收账款-杭州电商公司', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (28, 11, 2, 32, 0.00, 120000.00, '主营业务收入', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (29, 11, 3, 22, 0.00, 15600.00, '应交税费-增值税销项', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (30, 11, 4, 32, 1500.00, 0.00, '销售折扣', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (31, 12, 1, 2, 134100.00, 0.00, '银行存款', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (32, 12, 2, 6, 0.00, 134100.00, '应收账款-杭州电商公司', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (33, 13, 1, 37, 2800.00, 0.00, '管理费用-差旅费', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (34, 13, 2, 2, 0.00, 2800.00, '银行存款支付', NULL, '2025-12-21 01:46:23');
INSERT INTO `acct_voucher_entry` VALUES (39, 16, 1, 40, 80000.00, 0.00, '计提本月工资', NULL, '2025-12-21 01:48:58');
INSERT INTO `acct_voucher_entry` VALUES (40, 16, 2, 21, 0.00, 80000.00, '计提本月工资', NULL, '2025-12-21 01:48:58');
INSERT INTO `acct_voucher_entry` VALUES (41, 17, 1, 40, 833.33, 0.00, '计提本月固定资产折旧', NULL, '2025-12-21 01:49:03');
INSERT INTO `acct_voucher_entry` VALUES (42, 17, 2, 14, 0.00, 833.33, '计提本月固定资产折旧', NULL, '2025-12-21 01:49:03');
INSERT INTO `acct_voucher_entry` VALUES (43, 18, 1, 41, 2000.00, 0.00, '计提本月借款利息', NULL, '2025-12-21 01:49:04');
INSERT INTO `acct_voucher_entry` VALUES (44, 18, 2, 23, 0.00, 2000.00, '计提本月借款利息', NULL, '2025-12-21 01:49:04');
INSERT INTO `acct_voucher_entry` VALUES (45, 19, 1, 40, 500.00, 0.00, '本月费用摊销', NULL, '2025-12-21 01:49:06');
INSERT INTO `acct_voucher_entry` VALUES (46, 19, 2, 16, 0.00, 500.00, '本月费用摊销', NULL, '2025-12-21 01:49:06');
INSERT INTO `acct_voucher_entry` VALUES (47, 20, 1, 40, 85000.00, 0.00, '计提本月工资', NULL, '2025-12-21 01:49:08');
INSERT INTO `acct_voucher_entry` VALUES (48, 20, 2, 21, 0.00, 85000.00, '计提本月工资', NULL, '2025-12-21 01:49:08');
INSERT INTO `acct_voucher_entry` VALUES (49, 21, 1, 40, 833.33, 0.00, '计提本月固定资产折旧', NULL, '2025-12-21 01:49:10');
INSERT INTO `acct_voucher_entry` VALUES (50, 21, 2, 14, 0.00, 833.33, '计提本月固定资产折旧', NULL, '2025-12-21 01:49:10');
INSERT INTO `acct_voucher_entry` VALUES (51, 22, 1, 40, 80000.00, 0.00, '计提本月工资', NULL, '2025-12-21 01:49:11');
INSERT INTO `acct_voucher_entry` VALUES (52, 22, 2, 21, 0.00, 80000.00, '计提本月工资', NULL, '2025-12-21 01:49:11');
INSERT INTO `acct_voucher_entry` VALUES (55, 24, 1, 21, 80000.00, 0.00, '支付应付职工薪酬', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (56, 24, 2, 2, 0.00, 80000.00, '银行存款支付', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (57, 25, 1, 22, 35000.00, 0.00, '支付应交税费', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (58, 25, 2, 2, 0.00, 35000.00, '银行存款支付', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (59, 26, 1, 40, 8000.00, 0.00, '管理费用-办公费', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (60, 26, 2, 2, 0.00, 8000.00, '银行存款支付', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (61, 27, 1, 13, 30000.00, 0.00, '固定资产-办公家具', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (62, 27, 2, 2, 0.00, 30000.00, '银行存款支付', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (63, 28, 1, 2, 200000.00, 0.00, '银行存款', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (64, 28, 2, 19, 0.00, 200000.00, '短期借款', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (65, 29, 1, 41, 1500.00, 0.00, '财务费用-利息支出', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (66, 29, 2, 2, 0.00, 1500.00, '银行存款支付', NULL, '2025-12-21 02:11:37');
INSERT INTO `acct_voucher_entry` VALUES (67, 30, 1, 16, 12000.00, 0.00, 'Long-term prepaid expenses', NULL, '2025-12-21 15:30:00');
INSERT INTO `acct_voucher_entry` VALUES (68, 30, 2, 2, 0.00, 12000.00, 'Bank payment', NULL, '2025-12-21 15:30:00');
INSERT INTO `acct_voucher_entry` VALUES (69, 31, 1, 32, 366500.00, 0.00, 'Close main business revenue', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (70, 31, 2, 30, 0.00, 366500.00, 'Transfer revenue to retained earnings', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (71, 31, 3, 30, 3500.00, 0.00, 'Transfer cost from retained earnings', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (72, 31, 4, 36, 0.00, 3500.00, 'Close main business cost', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (73, 31, 5, 30, 53300.00, 0.00, 'Transfer cost from retained earnings', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (74, 31, 6, 37, 0.00, 53300.00, 'Close other business cost', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (75, 31, 7, 30, 256999.99, 0.00, 'Transfer expense from retained earnings', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (76, 31, 8, 40, 0.00, 256999.99, 'Close management expense', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (77, 31, 9, 30, 3500.00, 0.00, 'Transfer expense from retained earnings', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (78, 31, 10, 41, 0.00, 3500.00, 'Close finance expense', NULL, '2025-12-21 16:00:00');
INSERT INTO `acct_voucher_entry` VALUES (113, 15, 1, 40, 5000.00, 0.00, '本月费用摊销', NULL, '2025-12-21 15:42:16');
INSERT INTO `acct_voucher_entry` VALUES (114, 15, 2, 16, 0.00, 5000.00, '本月费用摊销', NULL, '2025-12-21 15:42:16');
INSERT INTO `acct_voucher_entry` VALUES (115, 38, 1, 40, 100.00, 0.00, '测试', NULL, '2025-12-21 15:48:46');
INSERT INTO `acct_voucher_entry` VALUES (116, 38, 2, 14, 0.00, 100.00, '测试', NULL, '2025-12-21 15:48:46');
INSERT INTO `acct_voucher_entry` VALUES (117, 39, 1, 11, 60.00, 0.00, '采购商品', NULL, '2025-12-21 15:51:55');
INSERT INTO `acct_voucher_entry` VALUES (118, 39, 2, 22, 2.00, 0.00, '进项税额', NULL, '2025-12-21 15:51:55');
INSERT INTO `acct_voucher_entry` VALUES (119, 39, 3, 19, 0.00, 62.00, '应付PO1766303437118', NULL, '2025-12-21 15:51:55');

-- ----------------------------
-- Table structure for base_customer
-- ----------------------------
DROP TABLE IF EXISTS `base_customer`;
CREATE TABLE `base_customer`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `customer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户编号',
  `customer_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `contact_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '税号',
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行账号',
  `credit_limit` decimal(18, 2) NULL DEFAULT NULL COMMENT '信用额度',
  `status` enum('Active','Inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Active' COMMENT '状态',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `customer_code`(`customer_code` ASC) USING BTREE,
  INDEX `idx_customer_code`(`customer_code` ASC) USING BTREE,
  INDEX `idx_customer_name`(`customer_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '客户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_customer
-- ----------------------------
INSERT INTO `base_customer` VALUES (1, 'CUS001', '北京科技有限公司', '刘经理', '010-12345678', NULL, '北京市海淀区', '91110000CUS001', '中国银行海淀支行', '6216611234567890', 1000000.00, 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_customer` VALUES (2, 'CUS002', '上海贸易公司', '周总', '021-87654321', NULL, '上海市浦东新区', '91310000CUS002', '招商银行浦东支行', '6214831234567890', 800000.00, 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_customer` VALUES (3, 'CUS003', '杭州电子商务有限公司', '吴主管', '0571-23456789', NULL, '杭州市西湖区', '91330000CUS003', '交通银行西湖支行', '6222621234567890', 500000.00, 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');

-- ----------------------------
-- Table structure for base_employee
-- ----------------------------
DROP TABLE IF EXISTS `base_employee`;
CREATE TABLE `base_employee`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `employee_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '员工编号',
  `employee_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '员工姓名',
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门',
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职位',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证号',
  `entry_date` date NULL DEFAULT NULL COMMENT '入职日期',
  `status` enum('Active','Inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Active' COMMENT '状态（在职/离职）',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `employee_code`(`employee_code` ASC) USING BTREE,
  INDEX `idx_employee_code`(`employee_code` ASC) USING BTREE,
  INDEX `idx_employee_name`(`employee_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_employee
-- ----------------------------
INSERT INTO `base_employee` VALUES (1, 'EMP001', '张三', '财务部', '财务经理', '13800138001', 'zhangsan@example.com', NULL, '2020-01-01', 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_employee` VALUES (2, 'EMP002', '李四', '销售部', '销售经理', '13800138002', 'lisi@example.com', NULL, '2020-06-01', 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_employee` VALUES (3, 'EMP003', '王五', '采购部', '采购专员', '13800138003', 'wangwu@example.com', NULL, '2021-03-15', 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_employee` VALUES (4, 'EMP004', '赵六', '人事部', '人事主管', '13800138004', 'zhaoliu@example.com', NULL, '2021-08-20', 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_employee` VALUES (5, 'EMP005', '钱七', 'IT部', '系统管理员', '13800138005', 'qianqi@example.com', NULL, '2022-01-10', 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_employee` VALUES (6, 'EMP000', '胡伟浩', '人事部', 'HR', '15223565689', 'huweihao.com', '430102200102037856', '2025-12-04', 'Active', '', '2025-12-21 15:50:02', '2025-12-21 15:50:14');

-- ----------------------------
-- Table structure for base_supplier
-- ----------------------------
DROP TABLE IF EXISTS `base_supplier`;
CREATE TABLE `base_supplier`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `supplier_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商编号',
  `supplier_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '税号',
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行账号',
  `credit_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '信用等级',
  `status` enum('Active','Inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Active' COMMENT '状态',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `supplier_code`(`supplier_code` ASC) USING BTREE,
  INDEX `idx_supplier_code`(`supplier_code` ASC) USING BTREE,
  INDEX `idx_supplier_name`(`supplier_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_supplier
-- ----------------------------
INSERT INTO `base_supplier` VALUES (1, 'SUP001', '深圳市优质办公用品有限公司', '陈经理', '0755-11111111', '', '深圳市福田区', '91440300SUP001', '工商银行福田支行', '6222021234567890', 'C', 'Active', '', '2025-12-21 01:46:23', '2025-12-21 01:48:38');
INSERT INTO `base_supplier` VALUES (2, 'SUP002', '广州科技设备供应商', '林总', '020-22222222', NULL, '广州市天河区', '91440300SUP002', '建设银行天河支行', '6227001234567890', NULL, 'Active', NULL, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `base_supplier` VALUES (3, 'SUP003', '东莞原材料供应公司', '黄主管', '0769-33333333', '', '东莞市南城区', '91440300SUP003', '农业银行南城支行', '6228481234567890', 'A', 'Active', '', '2025-12-21 01:46:23', '2025-12-21 01:48:34');

-- ----------------------------
-- Table structure for biz_expense_claim
-- ----------------------------
DROP TABLE IF EXISTS `biz_expense_claim`;
CREATE TABLE `biz_expense_claim`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报销单ID',
  `claim_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '报销单编号',
  `employee_id` bigint NOT NULL COMMENT '报销员工ID',
  `claim_date` date NOT NULL COMMENT '报销日期',
  `total_amount` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '报销总额',
  `status` enum('Draft','Submitted','Approved','Paid','Posted','Rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Draft' COMMENT '状态',
  `payment_date` date NULL DEFAULT NULL COMMENT '付款日期',
  `is_posted` tinyint(1) NULL DEFAULT 0 COMMENT '是否已过账（1-是，0-否）',
  `posted_at` datetime NULL DEFAULT NULL COMMENT '过账时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `claim_no`(`claim_no` ASC) USING BTREE,
  INDEX `idx_claim_no`(`claim_no` ASC) USING BTREE,
  INDEX `idx_claim_date`(`claim_date` ASC) USING BTREE,
  INDEX `idx_employee_id`(`employee_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_posted`(`is_posted` ASC) USING BTREE,
  CONSTRAINT `biz_expense_claim_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `base_employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '费用报销主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_expense_claim
-- ----------------------------
INSERT INTO `biz_expense_claim` VALUES (1, 'EXP-2025-001', 2, '2025-01-18', 3500.00, 'Posted', '2025-01-20', 1, '2025-01-22 10:00:00', '李四出差北京拜访客户', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `biz_expense_claim` VALUES (2, 'EXP-2025-002', 3, '2025-02-18', 2800.00, 'Posted', '2025-02-20', 1, '2025-02-22 10:00:00', '王五采购材料差旅', '2025-12-21 01:46:23', '2025-12-21 01:46:23');

-- ----------------------------
-- Table structure for biz_expense_claim_item
-- ----------------------------
DROP TABLE IF EXISTS `biz_expense_claim_item`;
CREATE TABLE `biz_expense_claim_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `claim_id` bigint NOT NULL COMMENT '报销单ID',
  `expense_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '费用类型（差旅费、办公费、招待费等）',
  `expense_date` date NOT NULL COMMENT '费用发生日期',
  `amount` decimal(18, 2) NOT NULL COMMENT '金额',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '费用说明',
  `attachment_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '附件URL',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_claim_id`(`claim_id` ASC) USING BTREE,
  CONSTRAINT `biz_expense_claim_item_ibfk_1` FOREIGN KEY (`claim_id`) REFERENCES `biz_expense_claim` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '费用报销明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_expense_claim_item
-- ----------------------------
INSERT INTO `biz_expense_claim_item` VALUES (1, 1, '交通费', '2025-01-18', 1200.00, '北京往返机票', NULL, '经济舱', '2025-12-21 01:46:23');
INSERT INTO `biz_expense_claim_item` VALUES (2, 1, '住宿费', '2025-01-19', 1800.00, '酒店住宿3晚', NULL, '标准间', '2025-12-21 01:46:23');
INSERT INTO `biz_expense_claim_item` VALUES (3, 1, '餐饮费', '2025-01-20', 500.00, '商务用餐', NULL, '客户接待', '2025-12-21 01:46:23');
INSERT INTO `biz_expense_claim_item` VALUES (4, 2, '交通费', '2025-02-18', 800.00, '高铁票', NULL, '二等座', '2025-12-21 01:46:23');
INSERT INTO `biz_expense_claim_item` VALUES (5, 2, '住宿费', '2025-02-19', 1200.00, '酒店2晚', NULL, '标准间', '2025-12-21 01:46:23');
INSERT INTO `biz_expense_claim_item` VALUES (6, 2, '餐饮费', '2025-02-20', 800.00, '日常餐费', NULL, '出差补助', '2025-12-21 01:46:23');

-- ----------------------------
-- Table structure for biz_purchase_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_purchase_order`;
CREATE TABLE `biz_purchase_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `order_date` date NOT NULL COMMENT '订单日期',
  `total_amount` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总额',
  `tax_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '税额',
  `discount_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '折扣金额',
  `final_amount` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '最终金额',
  `status` enum('Draft','Confirmed','Received','Paid','Posted','Cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Draft' COMMENT '状态',
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '付款方式',
  `payment_date` date NULL DEFAULT NULL COMMENT '付款日期',
  `is_posted` tinyint(1) NULL DEFAULT 0 COMMENT '是否已过账（1-是，0-否）',
  `posted_at` datetime NULL DEFAULT NULL COMMENT '过账时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_order_date`(`order_date` ASC) USING BTREE,
  INDEX `idx_supplier_id`(`supplier_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_posted`(`is_posted` ASC) USING BTREE,
  CONSTRAINT `biz_purchase_order_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `base_supplier` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '采购订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_purchase_order
-- ----------------------------
INSERT INTO `biz_purchase_order` VALUES (1, 'PO-2025-001', 1, '2025-01-05', 50000.00, 6500.00, 0.00, 56500.00, 'Posted', '银行转账', '2025-01-08', 1, '2025-01-10 14:00:00', '采购办公电脑及设备', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `biz_purchase_order` VALUES (2, 'PO-2025-002', 3, '2025-02-05', 80000.00, 10400.00, 1000.00, 89400.00, 'Posted', '银行转账', '2025-02-10', 1, '2025-02-12 14:00:00', '采购生产原材料', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `biz_purchase_order` VALUES (3, 'PO1766303437118', 2, '2025-12-21', 60.00, 2.00, 0.00, 62.00, 'Confirmed', '银行转账', NULL, 1, '2025-12-21 15:51:55', '', '2025-12-21 15:50:59', '2025-12-21 15:51:55');

-- ----------------------------
-- Table structure for biz_purchase_order_item
-- ----------------------------
DROP TABLE IF EXISTS `biz_purchase_order_item`;
CREATE TABLE `biz_purchase_order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '采购订单ID',
  `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品/服务名称',
  `specification` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(18, 4) NOT NULL COMMENT '数量',
  `unit_price` decimal(18, 2) NOT NULL COMMENT '单价',
  `amount` decimal(18, 2) NOT NULL COMMENT '金额（数量*单价）',
  `tax_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '税率（%）',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  CONSTRAINT `biz_purchase_order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `biz_purchase_order` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '采购订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_purchase_order_item
-- ----------------------------
INSERT INTO `biz_purchase_order_item` VALUES (1, 1, '办公电脑', 'Dell OptiPlex 7090', '台', 10.0000, 5000.00, 50000.00, 13.00, '含显示器、键盘、鼠标', '2025-12-21 01:46:23');
INSERT INTO `biz_purchase_order_item` VALUES (2, 2, '钢材', 'Q235', '吨', 10.0000, 8000.00, 80000.00, 13.00, '建筑用钢材', '2025-12-21 01:46:23');
INSERT INTO `biz_purchase_order_item` VALUES (3, 3, 'A4', '', '个', 1.0000, 60.00, 60.00, 0.00, NULL, '2025-12-21 15:50:59');

-- ----------------------------
-- Table structure for biz_sales_invoice
-- ----------------------------
DROP TABLE IF EXISTS `biz_sales_invoice`;
CREATE TABLE `biz_sales_invoice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售发票ID',
  `invoice_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发票编号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `invoice_date` date NOT NULL COMMENT '开票日期',
  `total_amount` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '销售总额',
  `tax_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '税额',
  `discount_amount` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '折扣金额',
  `final_amount` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '最终金额',
  `status` enum('Draft','Confirmed','Delivered','Received','Posted','Cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Draft' COMMENT '状态',
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收款方式',
  `payment_date` date NULL DEFAULT NULL COMMENT '收款日期',
  `is_posted` tinyint(1) NULL DEFAULT 0 COMMENT '是否已过账（1-是，0-否）',
  `posted_at` datetime NULL DEFAULT NULL COMMENT '过账时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `invoice_no`(`invoice_no` ASC) USING BTREE,
  INDEX `idx_invoice_no`(`invoice_no` ASC) USING BTREE,
  INDEX `idx_invoice_date`(`invoice_date` ASC) USING BTREE,
  INDEX `idx_customer_id`(`customer_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_posted`(`is_posted` ASC) USING BTREE,
  CONSTRAINT `biz_sales_invoice_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `base_customer` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '销售订单/发票主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_sales_invoice
-- ----------------------------
INSERT INTO `biz_sales_invoice` VALUES (1, 'INV-2025-001', 1, '2025-01-10', 100000.00, 13000.00, 0.00, 113000.00, 'Posted', '银行转账', '2025-01-15', 1, '2025-01-12 15:00:00', '销售标准产品A给北京科技公司', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `biz_sales_invoice` VALUES (2, 'INV-2025-002', 2, '2025-01-25', 150000.00, 19500.00, 2000.00, 167500.00, 'Posted', '银行转账', '2025-01-30', 1, '2025-01-27 14:00:00', '销售定制产品B给上海贸易公司', '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `biz_sales_invoice` VALUES (3, 'INV-2025-003', 3, '2025-02-15', 120000.00, 15600.00, 1500.00, 134100.00, 'Posted', '银行转账', '2025-02-20', 1, '2025-02-17 15:00:00', '销售高端产品C给杭州电商公司', '2025-12-21 01:46:23', '2025-12-21 01:46:23');

-- ----------------------------
-- Table structure for biz_sales_invoice_item
-- ----------------------------
DROP TABLE IF EXISTS `biz_sales_invoice_item`;
CREATE TABLE `biz_sales_invoice_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `invoice_id` bigint NOT NULL COMMENT '销售发票ID',
  `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品/服务名称',
  `specification` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(18, 4) NOT NULL COMMENT '数量',
  `unit_price` decimal(18, 2) NOT NULL COMMENT '单价',
  `amount` decimal(18, 2) NOT NULL COMMENT '金额（数量*单价）',
  `tax_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '税率（%）',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_invoice_id`(`invoice_id` ASC) USING BTREE,
  CONSTRAINT `biz_sales_invoice_item_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `biz_sales_invoice` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '销售订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_sales_invoice_item
-- ----------------------------
INSERT INTO `biz_sales_invoice_item` VALUES (1, 1, '标准产品A', 'A-2025', '件', 100.0000, 1000.00, 100000.00, 13.00, '批量销售优惠', '2025-12-21 01:46:23');
INSERT INTO `biz_sales_invoice_item` VALUES (2, 2, '定制产品B', 'B-Custom-2025', '套', 30.0000, 5000.00, 150000.00, 13.00, '定制规格，含安装服务', '2025-12-21 01:46:23');
INSERT INTO `biz_sales_invoice_item` VALUES (3, 3, '高端产品C', 'C-Premium-2025', '台', 15.0000, 8000.00, 120000.00, 13.00, '旗舰型号，含3年质保', '2025-12-21 01:46:23');

-- ----------------------------
-- Table structure for sys_company
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '企业名称',
  `company_scale` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '企业规模（小型/中型/大型）',
  `registered_capital` decimal(18, 2) NULL DEFAULT NULL COMMENT '注册资金',
  `business_license` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营业执照号',
  `legal_representative` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '法人代表',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '企业地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '税号',
  `fiscal_year_start` int NULL DEFAULT 1 COMMENT '会计年度起始月份（1-12）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_company_name`(`company_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '企业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_company
-- ----------------------------
INSERT INTO `sys_company` VALUES (1, '深圳市示范科技有限公司', '中型', 10000000.00, '91440300XXXXX', '张三', '0755-12345678', '深圳市南山区科技园', '91440300XXXXX', 1, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `sys_company` VALUES (2, 'CSU', '小型', 100.00, '', '', '', '', '', 1, '2025-12-21 15:49:18', '2025-12-21 15:49:18');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置值',
  `config_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'String' COMMENT '配置类型',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置说明',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统配置（1-是，0-否）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `config_key`(`config_key` ASC) USING BTREE,
  INDEX `idx_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'system.name', 'FS_System 财务会计管理系统', 'String', '系统名称', 1, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `sys_config` VALUES (2, 'system.version', '2.0.0', 'String', '系统版本', 1, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `sys_config` VALUES (3, 'fiscal.year.start', '1', 'Integer', '会计年度起始月份', 1, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `sys_config` VALUES (4, 'voucher.auto.number', 'true', 'Boolean', '凭证自动编号', 1, '2025-12-21 01:46:23', '2025-12-21 01:46:23');
INSERT INTO `sys_config` VALUES (5, 'decimal.precision', '2', 'Integer', '金额小数位数', 1, '2025-12-21 01:46:23', '2025-12-21 01:46:23');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `operation_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作模块',
  `operation_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '操作描述',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求URL',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `response_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '响应结果',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `operation_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `duration` int NULL DEFAULT NULL COMMENT '执行时长（毫秒）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Success' COMMENT '状态',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operator`(`operator` ASC) USING BTREE,
  INDEX `idx_operation_time`(`operation_time` ASC) USING BTREE,
  INDEX `idx_operation_module`(`operation_module` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

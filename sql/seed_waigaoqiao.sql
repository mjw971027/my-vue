-- ============================================================
-- 上海外高桥造船有限公司 - 种子数据脚本
-- 公司代码: COMP003
-- ============================================================

-- ============================================================
-- 1. 公司主体
-- ============================================================
INSERT IGNORE INTO T_COMPANY (CODE, DESC_CHN, DESC_ENG, STATUS, CREATE_USER_ID, CREATE_DATE) VALUES
('COMP003', '上海外高桥造船有限公司', 'Shanghai Waigaoqiao Shipbuilding Co., Ltd.', '01', '1', '2026-01-01');

-- ============================================================
-- 2. 部门组织（外高桥造船）
-- ============================================================
INSERT IGNORE INTO T_DEPT (ORGN_CD, ORGN_DESC, SUPER_ORGN_CD, ORGN_TYPE, STATUS, CREATE_USER_ID, CREATE_DATE) VALUES
('DEPT007', '技术部',   'COMP003', '02', '01', '1', '2026-01-01'),
('DEPT008', '生产部',   'COMP003', '02', '01', '1', '2026-01-01'),
('DEPT009', '质量部',   'COMP003', '02', '01', '1', '2026-01-01'),
('DEPT010', '设计部',   'COMP003', '02', '01', '1', '2026-01-01'),
('DEPT011', '采购部',   'COMP003', '02', '01', '1', '2026-01-01'),
('DEPT012', '安全环保部', 'COMP003', '02', '01', '1', '2026-01-01');

-- ============================================================
-- 3. 工程号/项目（外高桥造船）
-- ============================================================
INSERT IGNORE INTO T_PROJECT (CODE, DESC_CHN, DESC_ENG, COMPANY_NO, STATUS, START_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('PROJ005', '40万吨矿砂船建造工程',          '400K VLOC Bulk Carrier',   'COMP003', '01', '2026-03-01', '1', '2026-03-01'),
('PROJ006', '豪华邮轮建造工程',              'Luxury Cruise Ship',       'COMP003', '01', '2026-04-15', '1', '2026-04-15'),
('PROJ007', 'FPSO海上浮式生产储卸装置建造',   'FPSO Construction',        'COMP003', '01', '2026-05-01', '1', '2026-05-01'),
('PROJ008', '双燃料动力集装箱船建造工程',     'Dual-Fuel Container Ship', 'COMP003', '01', '2026-06-01', '1', '2026-06-01');

-- ============================================================
-- 4. 工装申请主表（外高桥造船）
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS (GUID, BILL_NO, COMPANY_NO, DEPT_NO, COMPONENTS_NAME, PROJ_NO, DIV_CD,
    NUMBER_NO, FINAL_NUMBER_NO, APPOINT_DUTY_DEPT_YN, DWGNO, MATERIAL_TOTAL_COST, MH_BDGT,
    CREATE_USER_ID, CREATE_DATE, NEED_DATE, REMARK, MA_STATUS, TEL, APP_DATE) VALUES

-- ① 40万吨矿砂船 - 分段组装工装（已完成）
('CP-2026-0005', 'COMP0032026060100001', 'COMP003', 'DEPT010', 'VLOC分段组装定位工装',     'PROJ005', '02',
    4, 4, 'Y', 'DWG-VLOC-ZJ-001', '22000', '160',
    '2', '2026-06-01', '2026-08-01', '40万吨矿砂船分段组装定位用', '03', '13800000002', '2026-06-01'),

-- ② 豪华邮轮 - 薄板焊接工装
('CP-2026-0006', 'COMP0032026060300001', 'COMP003', 'DEPT007', '邮轮薄板焊接防变形工装',    'PROJ006', '01',
    8, 8, 'Y', 'DWG-CRUISE-HB-001', '35000', '280',
    '2', '2026-06-03', '2026-08-15', '豪华邮轮薄板焊接防变形专用', '03', '13800000002', '2026-06-03'),

-- ③ FPSO - 管道预制工装（审批中）
('CP-2026-0007', 'COMP0032026060500001', 'COMP003', 'DEPT008', 'FPSO管道预制专用工装',      'PROJ007', '02',
    6, 6, 'Y', 'DWG-FPSO-GD-005', '42000', '320',
    '3', '2026-06-05', '2026-09-01', 'FPSO模块管道预制装配专用', '02', '13800000003', '2026-06-05'),

-- ④ 双燃料集装箱船 - 安全防护工装
('CP-2026-0008', 'COMP0032026060800001', 'COMP003', 'DEPT012', '双燃料船防爆安全工装',      'PROJ008', '03',
    10, 0, 'Y', 'DWG-DF-FB-002', '15000', '100',
    '2', '2026-06-08', '2026-07-15', '双燃料动力船防爆区域作业安全防护', '01', '13800000002', '2026-06-08'),

-- ⑤ 40万吨矿砂船 - 涂装工装
('CP-2026-0009', 'COMP0032026061000001', 'COMP003', 'DEPT008', 'VLOC涂装作业专用工装',      'PROJ005', '01',
    5, 5, 'N', NULL, '12000', '80',
    '2', '2026-06-10', '2026-07-30', '矿砂船涂装作业平台及防护', '03', '13800000004', '2026-06-10'),

-- ⑥ 豪华邮轮 - 内装工装（退回状态）
('CP-2026-0010', 'COMP0032026061200001', 'COMP003', 'DEPT010', '邮轮内装吊顶安装工装',      'PROJ006', '05',
    3, 0, 'Y', 'DWG-CRUISE-NZ-003', '18000', '150',
    '3', '2026-06-12', '2026-08-01', '邮轮舱室内装吊顶安装专用', '00', '13800000003', '2026-06-12'),

-- ⑦ FPSO - 电仪安装工装
('CP-2026-0011', 'COMP0032026061500001', 'COMP003', 'DEPT008', 'FPSO模块电仪安装工装',      'PROJ007', '04',
    4, 4, 'N', 'DWG-FPSO-DY-002', '9000', '70',
    '2', '2026-06-15', '2026-08-20', 'FPSO模块电气仪表安装专用', '04', '13800000004', '2026-06-15'),

-- ⑧ 双燃料集装箱船 - 管路试压工装（已出图）
('CP-2026-0012', 'COMP0032026061800001', 'COMP003', 'DEPT007', '双燃料管路试压检测工装',    'PROJ008', '02',
    2, 2, 'Y', 'DWG-DF-SY-001', '28000', '200',
    '3', '2026-06-18', '2026-08-01', '双燃料系统管路压力测试专用', '05', '13800000002', '2026-06-18');

-- ============================================================
-- 5. 工装申请明细表
-- ============================================================

-- CP-2026-0005 VLOC分段组装定位工装明细
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0005-01', 'CP-2026-0005', 'Y', 'MAT-Q345-040', 'Q345定位支撑座',     '件', 8,  8,  2800, '01', 'Q345', 25,  NULL, 300,  NULL, 400,  '2', '2026-06-01'),
('LINE-0005-02', 'CP-2026-0005', 'Y', 'MAT-45-041',   '45#定位销轴',        '根', 16, 16, 600,  '01', '45#',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-01'),
('LINE-0005-03', 'CP-2026-0005', 'Y', 'MAT-Q235-042', 'Q235调节垫片',       '件', 32, 32, 200,  '01', 'Q235', 10,  NULL, 100,  NULL, 100,  '2', '2026-06-01'),
('LINE-0005-04', 'CP-2026-0005', 'Y', 'MAT-304-043',  '304不锈钢紧固件套件', '套', 16, 16, 800,  '02', '304',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-01');

-- CP-2026-0006 邮轮薄板焊接防变形工装明细
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0006-01', 'CP-2026-0006', 'Y', 'MAT-Q235-050', 'Q235防变形压板',     '件', 20, 20, 500,  '01', 'Q235', 12,  NULL, 150,  NULL, 600,  '2', '2026-06-03'),
('LINE-0006-02', 'CP-2026-0006', 'Y', 'MAT-45-051',   '45#支撑螺杆',        '根', 40, 40, 350,  '03', '45#',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-03'),
('LINE-0006-03', 'CP-2026-0006', 'Y', 'MAT-304-052',  '304冷却水管接头',    '套', 16, 16, 400,  '01', '304',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-03'),
('LINE-0006-04', 'CP-2026-0006', 'Y', 'MAT-Q345-053', 'Q345背衬垫板',       '件', 20, 20, 700,  '01', 'Q345', 8,   NULL, 200,  NULL, 800,  '2', '2026-06-03');

-- CP-2026-0007 FPSO管道预制专用工装明细
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0007-01', 'CP-2026-0007', 'Y', 'MAT-Q345-060', 'Q345管道固定支架',   '件', 12, 12, 3500, '01', 'Q345', 20,  NULL, 400,  NULL, 600,  '3', '2026-06-05'),
('LINE-0007-02', 'CP-2026-0007', 'Y', 'MAT-40Cr-061', '40Cr对中夹具',       '套', 6,  6,  1800, '01', '40Cr', NULL, NULL, NULL, NULL, NULL, '3', '2026-06-05'),
('LINE-0007-03', 'CP-2026-0007', 'Y', 'MAT-304-062',  '304不锈钢调节螺杆',   '根', 24, 24, 450,  '01', '304',  NULL, NULL, NULL, NULL, NULL, '3', '2026-06-05'),
('LINE-0007-04', 'CP-2026-0007', 'Y', 'MAT-H62-063',  'H62铜衬垫',           '件', 12, 12, 600,  '02', 'H62',  5,   NULL, 80,   NULL, 150,  '3', '2026-06-05');

-- CP-2026-0008 双燃料船防爆安全工装明细
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0008-01', 'CP-2026-0008', 'Y', 'MAT-304-070',  '304防爆隔离板',       '件', 10, 0,  800,  '01', '304',  4,   NULL, 1200, NULL, 2000, '2', '2026-06-08'),
('LINE-0008-02', 'CP-2026-0008', 'Y', 'MAT-Q235-071', 'Q235防爆工具箱体',    '件', 5,  0,  1200, '01', 'Q235', 3,   NULL, 600,  NULL, 800,  '2', '2026-06-08');

-- CP-2026-0009 VLOC涂装作业专用工装明细
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0009-01', 'CP-2026-0009', 'Y', 'MAT-Q235-080', 'Q235喷涂作业平台',    '件', 5,  5,  2000, '01', 'Q235', 8,   NULL, 1500, NULL, 3000, '2', '2026-06-10'),
('LINE-0009-02', 'CP-2026-0009', 'Y', 'MAT-304-081',  '304喷枪支架组件',     '套', 10, 10, 400,  '01', '304',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-10');

-- CP-2026-0010 邮轮内装吊顶安装工装明细（退回，无最终数量）
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0010-01', 'CP-2026-0010', 'Y', 'MAT-2A12-090', '2A12铝合金吊顶导轨',   '根', 6,  0,  1500, '01', '2A12', NULL, NULL, 50,   NULL, 3000, '3', '2026-06-12'),
('LINE-0010-02', 'CP-2026-0010', 'Y', 'MAT-304-091',  '304不锈钢吊挂件',     '套', 24, 0,  350,  '02', '304',  NULL, NULL, NULL, NULL, NULL, '3', '2026-06-12');

-- CP-2026-0011 FPSO模块电仪安装工装明细
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0011-01', 'CP-2026-0011', 'Y', 'MAT-Q345-100', 'Q345电缆桥架支撑架',   '件', 8,  8,  1200, '01', 'Q345', 10,  NULL, 300,  NULL, 500,  '2', '2026-06-15'),
('LINE-0011-02', 'CP-2026-0011', 'Y', 'MAT-304-101',  '304仪表管夹持座',      '件', 16, 16, 300,  '01', '304',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-15'),
('LINE-0011-03', 'CP-2026-0011', 'Y', 'MAT-316L-102', '316L耐腐蚀接线盒座',   '件', 8,  8,  500,  '01', '316L', 6,   NULL, 200,  NULL, 200,  '2', '2026-06-15');

-- CP-2026-0012 双燃料管路试压检测工装明细
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0012-01', 'CP-2026-0012', 'Y', 'MAT-316L-110', '316L试压法兰盲板',     '件', 4,  4,  2500, '01', '316L', 30,  NULL, 400,  NULL, NULL, '3', '2026-06-18'),
('LINE-0012-02', 'CP-2026-0012', 'Y', 'MAT-304-111',  '304压力表接头组件',    '套', 4,  4,  1200, '01', '304',  NULL, NULL, NULL, NULL, NULL, '3', '2026-06-18'),
('LINE-0012-03', 'CP-2026-0012', 'Y', 'MAT-40Cr-112', '40Cr高压密封压盖',    '件', 4,  4,  1800, '01', '40Cr', NULL, NULL, 350,  NULL, NULL, '3', '2026-06-18'),
('LINE-0012-04', 'CP-2026-0012', 'Y', 'MAT-45-113',   '45#试压堵头',          '根', 8,  8,  400,  '03', '45#',  NULL, NULL, NULL, NULL, NULL, '3', '2026-06-18');

-- ============================================================
-- 6. 工装申请附件表
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_ATT (GUID, COMPONENTS_ID, FILE_NAME, FILE_PATH, FILE_SIZE, FILE_TYPE,
    CREATE_USER_ID, CREATE_DATE) VALUES
('ATT-0005-01', 'CP-2026-0005', 'VLOC定位工装设计图.pdf',     '/uploads/2026/06/VLOC定位工装设计图.pdf',     '3.8MB', '01', '2', '2026-06-01'),
('ATT-0005-02', 'CP-2026-0005', 'VLOC定位工装材料清单.xlsx',  '/uploads/2026/06/VLOC定位工装材料清单.xlsx',  '210KB', '02', '2', '2026-06-01'),
('ATT-0006-01', 'CP-2026-0006', '邮轮薄板焊接工装设计图.pdf',  '/uploads/2026/06/邮轮薄板焊接工装设计图.pdf',  '5.2MB', '01', '2', '2026-06-03'),
('ATT-0006-02', 'CP-2026-0006', '薄板焊接工艺规范.docx',      '/uploads/2026/06/薄板焊接工艺规范.docx',      '1.1MB', '03', '2', '2026-06-03'),
('ATT-0007-01', 'CP-2026-0007', 'FPSO管道工装设计图.pdf',     '/uploads/2026/06/FPSO管道工装设计图.pdf',     '2.6MB', '01', '3', '2026-06-05'),
('ATT-0009-01', 'CP-2026-0009', 'VLOC涂装工装技术规范.docx',  '/uploads/2026/06/VLOC涂装工装技术规范.docx',  '780KB', '03', '2', '2026-06-10'),
('ATT-0011-01', 'CP-2026-0011', 'FPSO电仪工装设计图.pdf',     '/uploads/2026/06/FPSO电仪工装设计图.pdf',     '1.5MB', '01', '2', '2026-06-15'),
('ATT-0012-01', 'CP-2026-0012', '双燃料试压工装设计图.pdf',    '/uploads/2026/06/双燃料试压工装设计图.pdf',    '4.1MB', '01', '3', '2026-06-18'),
('ATT-0012-02', 'CP-2026-0012', '管路试压工艺规范.docx',      '/uploads/2026/06/管路试压工艺规范.docx',      '950KB', '03', '3', '2026-06-18');

-- ============================================================
-- 7. 工装申请审批意见表
-- ============================================================

-- CP-2026-0005 VLOC分段组装定位工装（3步，已完成）
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0005-01', 'CP-2026-0005', '01', '同意，VLOC分段定位方案可行',            '4', '王五',  '2026-06-02', '4', '2026-06-02'),
('OPN-0005-02', 'CP-2026-0005', '02', '技术方案合理，批准制造',                '2', '张三',  '2026-06-04', '2', '2026-06-04'),
('OPN-0005-03', 'CP-2026-0005', '03', '同意，按节点推进',                      '1', 'admin', '2026-06-05', '1', '2026-06-05');

-- CP-2026-0006 邮轮薄板焊接防变形工装（3步，已完成）
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0006-01', 'CP-2026-0006', '01', '邮轮薄板焊接是关键工艺，同意申请',       '4', '王五',  '2026-06-04', '4', '2026-06-04'),
('OPN-0006-02', 'CP-2026-0006', '02', '防变形方案经过验证可行，同意',           '2', '张三',  '2026-06-06', '2', '2026-06-06'),
('OPN-0006-03', 'CP-2026-0006', '03', '批准，重点保障邮轮项目进度',             '1', 'admin', '2026-06-07', '1', '2026-06-07');

-- CP-2026-0007 FPSO管道预制专用工装（1步，审批中）
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0007-01', 'CP-2026-0007', '01', 'FPSO管道预制需求紧急，建议优先审批',     '4', '王五',  '2026-06-06', '4', '2026-06-06');

-- CP-2026-0009 VLOC涂装作业专用工装（3步，已完成）
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0009-01', 'CP-2026-0009', '01', '涂装工装为常规需求，同意',               '4', '王五',  '2026-06-11', '4', '2026-06-11'),
('OPN-0009-02', 'CP-2026-0009', '02', '技术方案简单可行，同意',                 '2', '张三',  '2026-06-12', '2', '2026-06-12'),
('OPN-0009-03', 'CP-2026-0009', '03', '同意，按期交付',                        '1', 'admin', '2026-06-13', '1', '2026-06-13');

-- CP-2026-0010 邮轮内装吊顶安装工装（退回）
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0010-01', 'CP-2026-0010', '01', '材料规格不符合邮轮防火要求，退回修改',   '4', '王五',  '2026-06-13', '4', '2026-06-13');

-- CP-2026-0011 FPSO模块电仪安装工装（已完成，设计出图阶段）
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0011-01', 'CP-2026-0011', '01', '同意，电仪安装工装需求明确',             '4', '王五',  '2026-06-16', '4', '2026-06-16');

-- CP-2026-0012 双燃料管路试压检测工装（已出图，出图审批中）
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0012-01', 'CP-2026-0012', '01', '双燃料系统试压至关重要，同意申请',       '4', '王五',  '2026-06-19', '4', '2026-06-19'),
('OPN-0012-02', 'CP-2026-0012', '02', '试压工装设计符合规范要求，同意',         '2', '张三',  '2026-06-20', '2', '2026-06-20');

-- ============================================================
-- 8. 工装申请引用/报价表
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_QUOTE (GUID, COMPONENT_ID, IS_USED, IS_QUOTED, COM_DATE,
    CREATE_USER_ID, CREATE_DATE, REMARK) VALUES
('QT-0004', 'CP-2026-0005', '01', '02', '2026-06-15', '1', '2026-06-05', 'VLOC定位工装已投入制造'),
('QT-0005', 'CP-2026-0006', '01', '02', '2026-06-20', '1', '2026-06-07', '邮轮薄板焊接工装正在制造'),
('QT-0006', 'CP-2026-0007', '02', '01', NULL,          '3', '2026-06-05', 'FPSO管道工装审批中'),
('QT-0007', 'CP-2026-0009', '01', '02', '2026-06-22', '1', '2026-06-13', '涂装工装已制造完成'),
('QT-0008', 'CP-2026-0010', '02', '01', NULL,          '3', '2026-06-12', '内装工装已被退回'),
('QT-0009', 'CP-2026-0011', '02', '01', NULL,          '1', '2026-06-15', '电仪工装设计中'),
('QT-0010', 'CP-2026-0012', '02', '01', NULL,          '1', '2026-06-18', '试压工装已出图');

-- ============================================================
-- 9. 工装台账主表（外高桥造船）
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_LEDGER (GUID, COMPONENT_GUID, BILL_NO, TYPE_CODE, COMPANY_NO, DEPT_NO,
    COMPONENTS_NAME, SPECIFICATION, QUANTITY, UNIT, LOCATION, STATUS,
    BORROW_DEPT, BORROW_PERSON, BORROW_DATE, EXPECTED_RETURN_DATE,
    CREATE_USER_ID, CREATE_DATE) VALUES

-- VLOC分段组装定位工装（已入库，在库）
('LDG-006', 'CP-2026-0005', 'TZ-COMP003-20260615-001', 'TYPE02', 'COMP003', 'DEPT010',
    'VLOC分段组装定位工装', 'DWG-VLOC-ZJ-001 / 4套', 4, '套', '外高桥1号库房-A区-01', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-06-15'),

-- 邮轮薄板焊接防变形工装（在库）
('LDG-007', 'CP-2026-0006', 'TZ-COMP003-20260620-001', 'TYPE01', 'COMP003', 'DEPT007',
    '邮轮薄板焊接防变形工装', 'DWG-CRUISE-HB-001 / 8套', 8, '套', '外高桥2号库房-B区-01', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-06-20'),

-- VLOC涂装作业专用工装（已入库）
('LDG-008', 'CP-2026-0009', 'TZ-COMP003-20260622-001', 'TYPE01', 'COMP003', 'DEPT008',
    'VLOC涂装作业专用工装', '5套涂装平台及喷枪支架', 5, '套', '外高桥3号库房-C区-01', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-06-22'),

-- 外高桥原有老旧工装（无关联申请，直接录入台账）
('LDG-009', NULL, 'TZ-COMP003-20260101-001', 'TYPE02', 'COMP003', 'DEPT008',
    '船体分段翻身胎架', 'DWG-FS-2025-001 / 3套', 3, '套', '外高桥1号库房-A区-02', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-01-01'),

('LDG-010', NULL, 'TZ-COMP003-20260201-001', 'TYPE04', 'COMP003', 'DEPT008',
    '320吨龙门吊吊具', 'DWG-LM-2025-012 / 2套', 2, '套', '外高桥重型堆场-A区', '02',
    'DEPT010', '赵六', '2026-06-10', '2026-07-10',
    '1', '2026-02-01'),

('LDG-011', NULL, 'TZ-COMP003-20260301-001', 'TYPE03', 'COMP003', 'DEPT009',
    '超声波探伤检测工装', 'DWG-JC-2025-008 / 5台', 5, '台', '外高桥检测中心-01', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-03-01'),

('LDG-012', NULL, 'TZ-COMP003-20260401-001', 'TYPE01', 'COMP003', 'DEPT007',
    'CO2气体保护焊枪支架', 'DWG-HJ-2025-022 / 20套', 20, '套', '外高桥2号库房-B区-02', '03',
    NULL, NULL, NULL, NULL,
    '1', '2026-04-01'),

('LDG-013', NULL, 'TZ-COMP003-20260501-001', 'TYPE01', 'COMP003', 'DEPT008',
    '大型拼板焊接平台', 'DWG-PB-2025-005 / 6块', 6, '块', '外高桥分段制造车间-A区', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-05-01');

-- ============================================================
-- 10. 台账明细表
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_LEDGER_LIST (GUID, LEDGER_GUID, ITEM_NO, COMPONENTS_NAME, SPECIFICATION,
    QUANTITY, UNIT, REMARK, CREATE_USER_ID, CREATE_DATE) VALUES
-- LDG-006 VLOC定位工装
('LST-006-01', 'LDG-006', 1, '定位支撑座',   'Q345/300×400×25',   8, '件', '承力底座',           '1', '2026-06-15'),
('LST-006-02', 'LDG-006', 2, '定位销轴',     '45#/定制',          16, '根', '淬火处理',           '1', '2026-06-15'),
('LST-006-03', 'LDG-006', 3, '调节垫片',     'Q235/100×100×10',  32, '件', '精度调节用',         '1', '2026-06-15'),

-- LDG-007 邮轮薄板焊接工装
('LST-007-01', 'LDG-007', 1, '防变形压板',   'Q235/150×600×12',  20, '件', '薄板压紧防变形',     '1', '2026-06-20'),
('LST-007-02', 'LDG-007', 2, '支撑螺杆',     '45#/M30×400',      40, '根', '高度调节支撑',       '1', '2026-06-20'),
('LST-007-03', 'LDG-007', 3, '冷却水管接头', '304/定制',          16, '套', '水冷循环用',         '1', '2026-06-20'),

-- LDG-009 船体分段翻身胎架
('LST-009-01', 'LDG-009', 1, '主翻身梁',     'Q345/H型钢500×300', 6, '根', '胎架主梁',           '1', '2026-01-01'),
('LST-009-02', 'LDG-009', 2, '支撑垫块',     '45#/300×200×80',   12, '件', '分段支撑',           '1', '2026-01-01'),

-- LDG-010 320吨龙门吊吊具
('LST-010-01', 'LDG-010', 1, '主吊梁',       'Q345/箱型梁',       2, '件', '320t级主吊梁',        '1', '2026-02-01'),
('LST-010-02', 'LDG-010', 2, '吊索具组件',   '高强度钢丝绳',      4, '套', '含卸扣及吊环',       '1', '2026-02-01'),
('LST-010-03', 'LDG-010', 3, '平衡梁',       'Q345/定制',         2, '件', '四吊点平衡',         '1', '2026-02-01'),

-- LDG-011 超声波探伤检测工装
('LST-011-01', 'LDG-011', 1, '探头扫查器',   '40Cr/定制',         5, '台', '含编码器及导轨',     '1', '2026-03-01'),
('LST-011-02', 'LDG-011', 2, '标准试块组',   'Q235/CSK系列',      5, '套', '含CSK-IA/IIA',      '1', '2026-03-01'),

-- LDG-012 CO2焊枪支架（维修中）
('LST-012-01', 'LDG-012', 1, '焊枪夹持器',   '40Cr/定制',         20, '套', '含调节机构',         '1', '2026-04-01'),
('LST-012-02', 'LDG-012', 2, '送丝管支架',   'Q235/定制',         20, '件', '送丝管导向',         '1', '2026-04-01'),

-- LDG-013 大型拼板焊接平台
('LST-013-01', 'LDG-013', 1, '拼接平台面板', 'Q345/3000×1500×20', 6, '块', '带T型槽',            '1', '2026-05-01'),
('LST-013-02', 'LDG-013', 2, '平台支撑座',   'Q235/H型钢',        12, '件', '高度可调',           '1', '2026-05-01');

-- ============================================================
-- 11. 台账汇总表（外高桥造船数据）
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_LEDGER_GATHER (GUID, TYPE_CODE, COMPANY_NO, TOTAL_COUNT, BORROWED_COUNT,
    AVAILABLE_COUNT, MAINTENANCE_COUNT, SCRAPPED_COUNT, STATISTICS_DATE, CREATE_DATE) VALUES
('GTH-006', 'TYPE01', 'COMP003', 39, 0, 28, 20, 0, '2026-06-22', '2026-06-22'),
('GTH-007', 'TYPE02', 'COMP003', 7,  0, 7,  0,  0, '2026-06-22', '2026-06-22'),
('GTH-008', 'TYPE03', 'COMP003', 5,  0, 5,  0,  0, '2026-06-22', '2026-06-22'),
('GTH-009', 'TYPE04', 'COMP003', 2,  2, 0,  0,  0, '2026-06-22', '2026-06-22'),
('GTH-010', 'TYPE05', 'COMP003', 0,  0, 0,  0,  0, '2026-06-22', '2026-06-22');

-- ============================================================
-- 12. 台账单据表（外高桥造船）
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_LEDGER_BILL (GUID, BILL_NO, TYPE_CODE, COMPANY_NO, BILL_TYPE, BILL_DATE,
    BORROW_DEPT, BORROW_PERSON, ALLOT_DEPT, HANDLER, STATUS, REMARK, CREATE_USER_ID, CREATE_DATE) VALUES

-- VLOC定位工装入库
('BILL-006', 'RK-COMP003-20260615-001', 'TYPE02', 'COMP003', '01', '2026-06-15',
    NULL, NULL, NULL, '张三', '03', 'VLOC定位工装入库', '2', '2026-06-15'),

-- 邮轮薄板焊接工装入库
('BILL-007', 'RK-COMP003-20260620-001', 'TYPE01', 'COMP003', '01', '2026-06-20',
    NULL, NULL, NULL, '张三', '03', '邮轮薄板焊接工装入库', '2', '2026-06-20'),

-- 涂装工装入库
('BILL-008', 'RK-COMP003-20260622-001', 'TYPE01', 'COMP003', '01', '2026-06-22',
    NULL, NULL, NULL, '张三', '02', '涂装工装入库(待验收)', '2', '2026-06-22'),

-- 龙门吊吊具借出（借出至设计部）
('BILL-009', 'CK-COMP003-20260610-001', 'TYPE04', 'COMP003', '02', '2026-06-10',
    'DEPT010', '赵六', NULL, '张三', '03', '320吨龙门吊吊具借出至设计部', '2', '2026-06-10'),

-- 船体分段翻身胎架入库
('BILL-010', 'RK-COMP003-20260101-001', 'TYPE02', 'COMP003', '01', '2026-01-01',
    NULL, NULL, NULL, '张三', '03', '船体分段翻身胎架入库', '2', '2026-01-01'),

-- 超声波检测工装入库
('BILL-011', 'RK-COMP003-20260301-001', 'TYPE03', 'COMP003', '01', '2026-03-01',
    NULL, NULL, NULL, '张三', '03', '超声波探伤检测工装入库', '2', '2026-03-01'),

-- CO2焊枪支架入库
('BILL-012', 'RK-COMP003-20260401-001', 'TYPE01', 'COMP003', '01', '2026-04-01',
    NULL, NULL, NULL, '张三', '03', 'CO2气体保护焊枪支架入库', '2', '2026-04-01'),

-- 大型拼板焊接平台入库
('BILL-013', 'RK-COMP003-20260501-001', 'TYPE01', 'COMP003', '01', '2026-05-01',
    NULL, NULL, NULL, '张三', '03', '大型拼板焊接平台入库', '2', '2026-05-01');

-- ============================================================
-- 13. 台账审批意见表（外高桥造船）
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_LEDGER_OPINION (GUID, BILL_GUID, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, AUDIT_RESULT, CREATE_DATE) VALUES
('LOPN-006', 'BILL-006', 'VLOC定位工装验收合格，同意入库',           '1', 'admin', '2026-06-15', '01', '2026-06-15'),
('LOPN-007', 'BILL-007', '薄板焊接工装质量合格，同意入库',           '1', 'admin', '2026-06-20', '01', '2026-06-20'),
('LOPN-008', 'BILL-008', '涂装工装待质量部验收',                     '4', '王五',  '2026-06-22', '01', '2026-06-22'),
('LOPN-009', 'BILL-009', '同意借出，请7月10日前归还',                '1', 'admin', '2026-06-10', '01', '2026-06-10'),
('LOPN-010', 'BILL-010', '翻身胎架验收通过，同意入库',               '1', 'admin', '2026-01-01', '01', '2026-01-01'),
('LOPN-011', 'BILL-011', '检测工装符合标准，同意入库',               '1', 'admin', '2026-03-01', '01', '2026-03-01'),
('LOPN-012', 'BILL-012', '焊枪支架验收合格，同意入库',               '1', 'admin', '2026-04-01', '01', '2026-04-01'),
('LOPN-013', 'BILL-013', '拼板焊接平台验收合格，同意入库',           '1', 'admin', '2026-05-01', '01', '2026-05-01');

-- ============================================================
-- 14. 台账附件表（外高桥造船）
-- ============================================================
INSERT IGNORE INTO T_COMPONENTS_LEDGER_ATT (GUID, BILL_GUID, FILE_NAME, FILE_PATH, FILE_SIZE,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LATT-004', 'BILL-006', 'VLOC定位工装入库验收单.pdf',       '/uploads/ledger/2026/06/入库验收单-WGQ-006.pdf', '520KB', '2', '2026-06-15'),
('LATT-005', 'BILL-007', '薄板焊接工装入库验收单.pdf',       '/uploads/ledger/2026/06/入库验收单-WGQ-007.pdf', '480KB', '2', '2026-06-20'),
('LATT-006', 'BILL-009', '龙门吊吊具借出登记表.pdf',         '/uploads/ledger/2026/06/借出登记-WGQ-009.pdf',   '350KB', '2', '2026-06-10'),
('LATT-007', 'BILL-010', '翻身胎架入库验收单.pdf',           '/uploads/ledger/2026/03/入库验收单-WGQ-010.pdf', '410KB', '2', '2026-01-01');

-- ============================================================
-- 完成提示
-- ============================================================
SELECT '上海外高桥造船有限公司(COMP003)种子数据已添加完成!' AS MESSAGE;

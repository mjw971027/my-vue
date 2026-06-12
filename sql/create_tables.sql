-- ============================================================
-- 工装管理系统 - 全部数据表创建SQL + 基础种子数据
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- 用途: 可导入其他环境直接使用，含建表及关联基础数据
-- 生成日期: 2026-06-12
-- ============================================================

-- ============================================================
-- 1. 系统用户表
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    email VARCHAR(100) COMMENT '邮箱',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色(ADMIN/USER)',
    status VARCHAR(10) DEFAULT '在线' COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 密码均为 123456 的BCrypt加密值
INSERT IGNORE INTO sys_user (id, username, password, email, role, status) VALUES
(1, 'admin',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@jsjg.com',    'ADMIN', '在线'),
(2, 'zhangsan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'zhangsan@jsjg.com', 'USER',  '在线'),
(3, 'lisi',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'lisi@jsjg.com',     'USER',  '在线'),
(4, 'wangwu',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'wangwu@jsjg.com',   'USER',  '在线');

-- ============================================================
-- 2. 用户页面权限表
-- ============================================================
CREATE TABLE IF NOT EXISTS user_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '关联用户表',
    permission_key VARCHAR(50) NOT NULL COMMENT '权限标识(page1,page2,page3,page4)',
    UNIQUE KEY uk_user_perm (user_id, permission_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户页面权限表';


-- ============================================================
-- 3. 公司主体表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPANY (
    CODE VARCHAR(50) NOT NULL COMMENT '公司代码',
    DESC_CHN VARCHAR(200) COMMENT '公司中文描述',
    DESC_ENG VARCHAR(200) COMMENT '公司英文描述',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (CODE),
    KEY IDX_STATUS (STATUS)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司主体表';

INSERT IGNORE INTO T_COMPANY (CODE, DESC_CHN, DESC_ENG, STATUS, CREATE_USER_ID, CREATE_DATE) VALUES
('COMP001', '江南造船集团有限公司', 'Jiangnan Shipbuilding Group', '01', '1', '2026-01-01'),
('COMP002', '沪东中华造船集团有限公司', 'Hudong-Zhonghua Shipbuilding', '01', '1', '2026-01-01');

-- ============================================================
-- 4. 部门组织表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_DEPT (
    ORGN_CD VARCHAR(50) NOT NULL COMMENT '组织代码',
    ORGN_DESC VARCHAR(200) COMMENT '组织描述',
    SUPER_ORGN_CD VARCHAR(50) COMMENT '上级组织代码(公司代码)',
    ORGN_TYPE VARCHAR(2) COMMENT '组织类型(01:公司,02:部门,03:小组)',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (ORGN_CD),
    KEY IDX_SUPER_ORGN_CD (SUPER_ORGN_CD),
    KEY IDX_STATUS (STATUS)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门组织表';

INSERT IGNORE INTO T_DEPT (ORGN_CD, ORGN_DESC, SUPER_ORGN_CD, ORGN_TYPE, STATUS, CREATE_USER_ID, CREATE_DATE) VALUES
('DEPT001', '技术部',   'COMP001', '02', '01', '1', '2026-01-01'),
('DEPT002', '生产部',   'COMP001', '02', '01', '1', '2026-01-01'),
('DEPT003', '质量部',   'COMP001', '02', '01', '1', '2026-01-01'),
('DEPT004', '设计部',   'COMP001', '02', '01', '1', '2026-01-01'),
('DEPT005', '技术部',   'COMP002', '02', '01', '1', '2026-01-01'),
('DEPT006', '生产部',   'COMP002', '02', '01', '1', '2026-01-01');

-- ============================================================
-- 5. 工程号/项目表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_PROJECT (
    CODE VARCHAR(50) NOT NULL COMMENT '项目代码/工程号',
    DESC_CHN VARCHAR(200) COMMENT '项目中文描述',
    DESC_ENG VARCHAR(200) COMMENT '项目英文描述',
    COMPANY_NO VARCHAR(50) COMMENT '所属公司代码',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)',
    START_DATE DATE COMMENT '开始日期',
    END_DATE DATE COMMENT '结束日期',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (CODE),
    KEY IDX_COMPANY_NO (COMPANY_NO),
    KEY IDX_STATUS (STATUS)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工程号表';

INSERT IGNORE INTO T_PROJECT (CODE, DESC_CHN, DESC_ENG, COMPANY_NO, STATUS, START_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('PROJ001', 'H2400集装箱船建造工程',   'H2400 Container Ship',         'COMP001', '01', '2026-01-15', '1', '2026-01-15'),
('PROJ002', '30万吨VLCC改装工程',       '300K VLCC Conversion',         'COMP001', '01', '2026-02-01', '1', '2026-02-01'),
('PROJ003', '海洋平台模块建造工程',     'Offshore Platform Module',      'COMP002', '01', '2026-03-01', '1', '2026-03-01'),
('PROJ004', 'LNG运输船液货舱建造工程',  'LNG Carrier Cargo Tank',       'COMP002', '01', '2026-04-01', '1', '2026-04-01');

-- ============================================================
-- 6. 单位表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_UNIT (
    GUID VARCHAR(64) PRIMARY KEY COMMENT 'GUID',
    UNT_DESC VARCHAR(50) NOT NULL COMMENT '单位描述',
    UNT_TYPE VARCHAR(20) COMMENT '单位类型',
    ACTIVATION VARCHAR(2) DEFAULT 'Y' COMMENT '是否有效(Y:是,N:否)',
    CREATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单位表';

INSERT IGNORE INTO T_UNIT (GUID, UNT_DESC, UNT_TYPE, ACTIVATION) VALUES
('UNIT-001', '个',     'QUANTITY', 'Y'),
('UNIT-002', '件',     'QUANTITY', 'Y'),
('UNIT-003', '套',     'QUANTITY', 'Y'),
('UNIT-004', '台',     'QUANTITY', 'Y'),
('UNIT-005', '千克',   'WEIGHT',   'Y'),
('UNIT-006', '米',     'LENGTH',   'Y'),
('UNIT-007', '平方米', 'AREA',     'Y');

-- ============================================================
-- 7. 物资来源字典表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_MATERIAL_SOURCES (
    CODE VARCHAR(2) NOT NULL PRIMARY KEY COMMENT '来源编码(01:采购,02:库存,03:自制)',
    NAME VARCHAR(50) NOT NULL COMMENT '来源名称',
    SORT_ORDER INT DEFAULT 0 COMMENT '排序',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物资来源字典表';

INSERT IGNORE INTO T_MATERIAL_SOURCES (CODE, NAME, SORT_ORDER, STATUS) VALUES
('01', '采购', 1, '01'),
('02', '库存', 2, '01'),
('03', '自制', 3, '01');

-- ============================================================
-- 8. 钢材材质字典表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_BASIC_STEEL_QUALITY (
    CODE VARCHAR(50) NOT NULL PRIMARY KEY COMMENT '材质编码',
    NAME VARCHAR(200) NOT NULL COMMENT '材质名称',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)',
    CREATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钢材材质字典表';

INSERT IGNORE INTO T_BASIC_STEEL_QUALITY (CODE, NAME, STATUS) VALUES
('Q235',  'Q235碳素结构钢',      '01'),
('Q345',  'Q345低合金高强度钢',  '01'),
('45#',   '45#优质碳素钢',        '01'),
('40Cr',  '40Cr合金结构钢',      '01'),
('304',   '304不锈钢',           '01'),
('316L',  '316L不锈钢',          '01'),
('2A12',  '2A12硬铝合金',        '01'),
('H62',   'H62黄铜',             '01');

-- ============================================================
-- 9. 附件类型字典表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_ATTACHMENT_TYPE (
    CODE VARCHAR(2) NOT NULL PRIMARY KEY COMMENT '类型编码',
    NAME VARCHAR(50) NOT NULL COMMENT '类型名称',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件类型字典表';

INSERT IGNORE INTO T_ATTACHMENT_TYPE (CODE, NAME, STATUS) VALUES
('01', '设计图纸', '01'),
('02', '材料清单', '01'),
('03', '技术规范', '01'),
('04', '审批文件', '01'),
('99', '其他',     '01');

-- ============================================================
-- 10. 工装申请主表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS (
    GUID VARCHAR(64) NOT NULL COMMENT '唯一标识GUID',
    BILL_NO VARCHAR(50) NOT NULL COMMENT '申请单号',
    COMPANY_NO VARCHAR(50) COMMENT '公司主体代码',
    DEPT_NO VARCHAR(50) COMMENT '申请部门代码',
    COMPONENTS_NAME VARCHAR(200) COMMENT '项目名称',
    PROJ_NO VARCHAR(50) COMMENT '工程号',
    DIV_CD VARCHAR(2) COMMENT '工装类别代码(01:生产通用,02:生产专用,03:安措,04:科研通用,05:科研专用)',
    NUMBER_NO DECIMAL(10,2) DEFAULT 0 COMMENT '申请数量',
    FINAL_NUMBER_NO DECIMAL(10,2) DEFAULT 0 COMMENT '最终审核数',
    APPOINT_DUTY_DEPT_YN VARCHAR(1) COMMENT '是否指定责任部门',
    DWGNO VARCHAR(100) COMMENT '图号',
    MATERIAL_TOTAL_COST VARCHAR(100) COMMENT '材料总成本',
    MH_BDGT VARCHAR(100) COMMENT '人工预算',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    NEED_DATE DATE COMMENT '需求日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    MA_PROCESS_ID VARCHAR(100) COMMENT '流程ID(BPM流程实例ID)',
    MA_STATUS VARCHAR(2) DEFAULT '01' COMMENT '申请状态(00:退回,01:编制,02:审批,03:完成,04:设计出图,05:设计出图审批)',
    TEL VARCHAR(50) COMMENT '联系电话',
    APP_DATE DATE COMMENT '申请日期',
    DESIGN_STATUS VARCHAR(2) COMMENT '设计状态',
    BO_ID VARCHAR(100) COMMENT '业务对象ID',
    PRIMARY KEY (GUID),
    UNIQUE KEY UK_BILL_NO (BILL_NO),
    KEY IDX_COMPANY_NO (COMPANY_NO),
    KEY IDX_DEPT_NO (DEPT_NO),
    KEY IDX_PROJ_NO (PROJ_NO),
    KEY IDX_MA_STATUS (MA_STATUS),
    KEY IDX_CREATE_DATE (CREATE_DATE),
    KEY IDX_CREATE_USER_ID (CREATE_USER_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装申请表';

INSERT IGNORE INTO T_COMPONENTS (GUID, BILL_NO, COMPANY_NO, DEPT_NO, COMPONENTS_NAME, PROJ_NO, DIV_CD,
    NUMBER_NO, FINAL_NUMBER_NO, APPOINT_DUTY_DEPT_YN, DWGNO, MATERIAL_TOTAL_COST, MH_BDGT,
    CREATE_USER_ID, CREATE_DATE, NEED_DATE, REMARK, MA_STATUS, TEL, APP_DATE) VALUES
('CP-2026-0001', 'COMP0012026060100001', 'COMP001', 'DEPT001', 'H2400分段定位工装',       'PROJ001', '01',
    3, 3, 'Y', 'DWG-H2400-ZJ-001', '15000', '120',
    '2', '2026-06-01', '2026-07-15', 'H2400集装箱船分段装配定位用', '03', '13800000002', '2026-06-01'),
('CP-2026-0002', 'COMP0012026060200001', 'COMP001', 'DEPT002', 'VLCC管系安装专用工装',    'PROJ002', '02',
    2, 2, 'Y', 'DWG-VLCC-GX-003', '28000', '200',
    '3', '2026-06-02', '2026-08-01', '30万吨VLCC管路安装专用', '02', '13800000003', '2026-06-02'),
('CP-2026-0003', 'COMP0012026060500001', 'COMP001', 'DEPT001', '焊接安全防护工装',         'PROJ001', '03',
    5, 5, 'N', NULL, '8000', '60',
    '2', '2026-06-05', '2026-07-01', '焊接作业安全防护用', '03', '13800000002', '2026-06-05'),
('CP-2026-0004', 'COMP0022026060800001', 'COMP002', 'DEPT005', '平台模块吊装工装',         'PROJ003', '02',
    2, 0, 'Y', 'DWG-PT-DZ-007', '35000', '240',
    '2', '2026-06-08', '2026-09-01', '海洋平台模块吊装转运专用', '01', '13800000002', '2026-06-08');

-- ============================================================
-- 11. 工装申请明细表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LINE (
    GUID VARCHAR(64) NOT NULL COMMENT '唯一标识GUID',
    COMPONENTS_ID VARCHAR(64) NOT NULL COMMENT '工装申请ID(外键->T_COMPONENTS.GUID)',
    ACTIVATION VARCHAR(100) COMMENT '激活/生效标识',
    MATERIAL_NO VARCHAR(50) COMMENT '物资编码',
    MATERIAL_NAME VARCHAR(200) COMMENT '物资名称',
    UNIT VARCHAR(50) COMMENT '单位',
    DEMAND_QTY DECIMAL(10,2) DEFAULT 0 COMMENT '需求数量',
    FINAL_DEMAND_QTY DECIMAL(10,2) DEFAULT 0 COMMENT '最终需求数量',
    MATERIAL_COST DECIMAL(10,2) COMMENT '材料成本',
    MATERIAL_SOURCES VARCHAR(50) COMMENT '物资来源',
    REMARK VARCHAR(500) COMMENT '备注',
    QUALITY VARCHAR(100) COMMENT '材质/质量等级',
    THK1 DECIMAL(10,2) COMMENT '厚度1',
    THK2 DECIMAL(10,2) COMMENT '厚度2',
    W1 DECIMAL(10,2) COMMENT '宽度1',
    W2 DECIMAL(10,2) COMMENT '宽度2',
    L DECIMAL(10,2) COMMENT '长度',
    CREATE_NODE VARCHAR(100) COMMENT '创建节点',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    BO_ID VARCHAR(100) COMMENT '业务对象ID',
    PRIMARY KEY (GUID),
    KEY IDX_COMPONENTS_ID (COMPONENTS_ID),
    KEY IDX_MATERIAL_NO (MATERIAL_NO),
    KEY IDX_CREATE_DATE (CREATE_DATE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装申请明细表';

-- CP-2026-0001 的明细(H2400分段定位工装)
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0001-01', 'CP-2026-0001', 'Y', 'MAT-Q235-001', 'Q235定位销轴',       '件', 6,  6,  800,  '01', 'Q235',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-01'),
('LINE-0001-02', 'CP-2026-0001', 'Y', 'MAT-Q345-002', 'Q345定位底座板材',    '件', 6,  6,  1200, '01', 'Q345',  20,  NULL, 200,  NULL, 300,  '2', '2026-06-01'),
('LINE-0001-03', 'CP-2026-0001', 'Y', 'MAT-304-003',  '304不锈钢调节螺栓',   '套', 12, 12, 500,  '02', '304',   NULL, NULL, NULL, NULL, NULL, '2', '2026-06-01');

-- CP-2026-0002 的明细(VLCC管系安装专用工装)
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0002-01', 'CP-2026-0002', 'Y', 'MAT-Q345-010', 'Q345管路支撑架',      '件', 4,  4,  3500, '01', 'Q345',  16,  NULL, 300,  NULL, 800,  '3', '2026-06-02'),
('LINE-0002-02', 'CP-2026-0002', 'Y', 'MAT-40Cr-011', '40Cr夹紧法兰',        '套', 8,  8,  2100, '01', '40Cr',  NULL, NULL, NULL, NULL, NULL, '3', '2026-06-02'),
('LINE-0002-03', 'CP-2026-0002', 'Y', 'MAT-45-012',   '45#调节螺杆',         '根', 16, 16, 400,  '03', '45#',   NULL, NULL, NULL, NULL, NULL, '3', '2026-06-02');

-- CP-2026-0003 的明细(焊接安全防护工装)
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0003-01', 'CP-2026-0003', 'Y', 'MAT-Q235-020', 'Q235防护挡板',        '件', 10, 10, 400,  '01', 'Q235',  6,   NULL, 1000, NULL, 1500, '2', '2026-06-05'),
('LINE-0003-02', 'CP-2026-0003', 'Y', 'MAT-304-021',  '304隔热手柄',         '套', 5,  5,  200,  '02', '304',   NULL, NULL, NULL, NULL, NULL, '2', '2026-06-05');

-- CP-2026-0004 的明细(平台模块吊装工装)
INSERT IGNORE INTO T_COMPONENTS_LINE (GUID, COMPONENTS_ID, ACTIVATION, MATERIAL_NO, MATERIAL_NAME, UNIT,
    DEMAND_QTY, FINAL_DEMAND_QTY, MATERIAL_COST, MATERIAL_SOURCES, QUALITY, THK1, THK2, W1, W2, L,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LINE-0004-01', 'CP-2026-0004', 'Y', 'MAT-Q345-030', 'Q345吊耳板',          '件', 8,  0,  4200, '01', 'Q345',  30,  NULL, 400,  NULL, 600,  '2', '2026-06-08'),
('LINE-0004-02', 'CP-2026-0004', 'Y', 'MAT-40Cr-031', '40Cr吊装卸扣',        '套', 4,  0,  3600, '01', '40Cr',  NULL, NULL, NULL, NULL, NULL, '2', '2026-06-08');

-- ============================================================
-- 12. 工装申请附件表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_ATT (
    GUID VARCHAR(64) NOT NULL COMMENT '唯一标识GUID',
    COMPONENTS_ID VARCHAR(64) COMMENT '工装申请ID(外键->T_COMPONENTS.GUID)',
    FILE_NAME VARCHAR(200) COMMENT '文件名',
    FILE_PATH VARCHAR(500) COMMENT '文件路径',
    FILE_SIZE VARCHAR(50) COMMENT '文件大小',
    FILE_TYPE VARCHAR(50) COMMENT '文件类型',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (GUID),
    KEY IDX_COMPONENTS_ID (COMPONENTS_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装申请附件表';

INSERT IGNORE INTO T_COMPONENTS_ATT (GUID, COMPONENTS_ID, FILE_NAME, FILE_PATH, FILE_SIZE, FILE_TYPE,
    CREATE_USER_ID, CREATE_DATE) VALUES
('ATT-0001-01', 'CP-2026-0001', 'H2400定位工装设计图.pdf',  '/uploads/2026/06/H2400定位工装设计图.pdf',  '2.4MB', '01', '2', '2026-06-01'),
('ATT-0001-02', 'CP-2026-0001', 'H2400定位工装材料清单.xlsx', '/uploads/2026/06/H2400定位工装材料清单.xlsx', '156KB', '02', '2', '2026-06-01'),
('ATT-0002-01', 'CP-2026-0002', 'VLCC管系工装设计图.pdf',   '/uploads/2026/06/VLCC管系工装设计图.pdf',   '3.1MB', '01', '3', '2026-06-02'),
('ATT-0003-01', 'CP-2026-0003', '焊接防护工装技术规范.docx', '/uploads/2026/06/焊接防护工装技术规范.docx', '890KB', '03', '2', '2026-06-05');

-- ============================================================
-- 13. 工装申请附件数据表(二进制存储)
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_ATT_DATA (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    ATT_ID VARCHAR(64) COMMENT '附件ID(FK->T_COMPONENTS_ATT.GUID)',
    FILE_DATA LONGBLOB COMMENT '文件二进制数据',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建人',
    CREATE_DATE DATE COMMENT '创建日期',
    KEY IDX_ATT_ID (ATT_ID),
    CONSTRAINT FK_ATTDATA_ATT FOREIGN KEY (ATT_ID) REFERENCES T_COMPONENTS_ATT(GUID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装申请附件数据表';

-- 附件二进制数据在实际使用时由上传接口写入，此处不插入BLOB数据

-- ============================================================
-- 14. 工装申请审批意见表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_OPINION (
    GUID VARCHAR(64) NOT NULL COMMENT '唯一标识GUID',
    COMPONENTS_ID VARCHAR(64) COMMENT '工装申请ID(外键->T_COMPONENTS.GUID)',
    OPINION_TYPE VARCHAR(2) COMMENT '意见类型',
    OPINION_CONTENT VARCHAR(1000) COMMENT '意见内容',
    AUDIT_USER_ID VARCHAR(50) COMMENT '审核人ID',
    AUDIT_USER_NAME VARCHAR(100) COMMENT '审核人姓名',
    AUDIT_DATE DATE COMMENT '审核日期',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    PRIMARY KEY (GUID),
    KEY IDX_COMPONENTS_ID (COMPONENTS_ID),
    KEY IDX_AUDIT_USER_ID (AUDIT_USER_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装申请审批意见表';

-- CP-2026-0001 已完成审批(3步)
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0001-01', 'CP-2026-0001', '01', '同意，工装设计合理，可执行',         '4', '王五', '2026-06-02', '4', '2026-06-02'),
('OPN-0001-02', 'CP-2026-0001', '02', '技术方案可行，批准制造',             '2', '张三', '2026-06-03', '2', '2026-06-03'),
('OPN-0001-03', 'CP-2026-0001', '03', '同意，按计划执行',                   '1', 'admin', '2026-06-04', '1', '2026-06-04');

-- CP-2026-0002 审批中(1步)
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0002-01', 'CP-2026-0002', '01', '同意，管系工装需求明确',             '4', '王五', '2026-06-03', '4', '2026-06-03');

-- CP-2026-0003 已完成审批(3步)
INSERT IGNORE INTO T_COMPONENTS_OPINION (GUID, COMPONENTS_ID, OPINION_TYPE, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('OPN-0003-01', 'CP-2026-0003', '01', '安全防护工装为安措项目，优先安排',   '4', '王五', '2026-06-06', '4', '2026-06-06'),
('OPN-0003-02', 'CP-2026-0003', '02', '同意，材质和规格符合要求',           '2', '张三', '2026-06-07', '2', '2026-06-07'),
('OPN-0003-03', 'CP-2026-0003', '03', '批准，尽快安排制造',                 '1', 'admin', '2026-06-08', '1', '2026-06-08');

-- ============================================================
-- 15. 工装申请引用/报价表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_QUOTE (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    COMPONENT_ID VARCHAR(64) COMMENT '工装ID(FK->T_COMPONENTS.GUID)',
    IS_USED VARCHAR(2) COMMENT '是否使用(01:是,02:否)',
    IS_QUOTED VARCHAR(2) COMMENT '是否被引用(01:否,02:是)',
    COM_DATE DATE COMMENT '完成日期',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建人',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新人',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    KEY IDX_COMPONENT_ID (COMPONENT_ID),
    CONSTRAINT FK_QUOTE_COMPONENTS FOREIGN KEY (COMPONENT_ID) REFERENCES T_COMPONENTS(GUID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装申请引用/报价表';

INSERT IGNORE INTO T_COMPONENTS_QUOTE (GUID, COMPONENT_ID, IS_USED, IS_QUOTED, COM_DATE,
    CREATE_USER_ID, CREATE_DATE, REMARK) VALUES
('QT-0001', 'CP-2026-0001', '01', '02', '2026-06-10', '1', '2026-06-04', 'H2400定位工装已制造完成并投入使用'),
('QT-0002', 'CP-2026-0002', '02', '01', NULL,          '3', '2026-06-02', 'VLCC管系工装制造中'),
('QT-0003', 'CP-2026-0003', '01', '02', '2026-06-20', '1', '2026-06-08', '焊接防护工装已投入制造');

-- ============================================================
-- 16. 工装台账主表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LEDGER (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    COMPONENT_GUID VARCHAR(64) COMMENT '关联工装申请ID(FK->T_COMPONENTS.GUID)',
    BILL_NO VARCHAR(50) COMMENT '台账单据号',
    TYPE_CODE VARCHAR(50) COMMENT '台账类型(FK->T_COMPONENTS_LEDGER_TYPE.CODE)',
    COMPANY_NO VARCHAR(50) COMMENT '公司编码',
    DEPT_NO VARCHAR(50) COMMENT '部门编码',
    COMPONENTS_NAME VARCHAR(200) COMMENT '工装名称',
    SPECIFICATION VARCHAR(200) COMMENT '规格型号',
    QUANTITY DECIMAL(10,2) DEFAULT 0 COMMENT '数量',
    UNIT VARCHAR(50) COMMENT '单位',
    LOCATION VARCHAR(200) COMMENT '存放位置',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:在库,02:借出,03:维修,04:报废)',
    BORROW_DEPT VARCHAR(100) COMMENT '借用部门',
    BORROW_PERSON VARCHAR(50) COMMENT '借用人',
    BORROW_DATE DATE COMMENT '借出日期',
    EXPECTED_RETURN_DATE DATE COMMENT '预计归还日期',
    ACTUAL_RETURN_DATE DATE COMMENT '实际归还日期',
    REMARK VARCHAR(500) COMMENT '备注',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建人',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新人',
    UPDATE_DATE DATE COMMENT '更新日期',
    KEY IDX_COMPONENT_GUID (COMPONENT_GUID),
    KEY IDX_BILL_NO (BILL_NO),
    KEY IDX_TYPE_CODE (TYPE_CODE),
    KEY IDX_STATUS (STATUS),
    KEY IDX_BORROW_DEPT (BORROW_DEPT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工装台账主表';

INSERT IGNORE INTO T_COMPONENTS_LEDGER (GUID, COMPONENT_GUID, BILL_NO, TYPE_CODE, COMPANY_NO, DEPT_NO,
    COMPONENTS_NAME, SPECIFICATION, QUANTITY, UNIT, LOCATION, STATUS,
    BORROW_DEPT, BORROW_PERSON, BORROW_DATE, EXPECTED_RETURN_DATE,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LDG-001', 'CP-2026-0001', 'TZ-COMP001-20260610-001', 'TYPE01', 'COMP001', 'DEPT001',
    'H2400分段定位工装', 'DWG-H2400-ZJ-001 / 3套', 3, '套', '1号库房-A区-01', '02',
    'DEPT002', '李四', '2026-06-12', '2026-07-12',
    '1', '2026-06-10'),
('LDG-002', NULL, 'TZ-COMP001-20260301-001', 'TYPE02', 'COMP001', 'DEPT002',
    '船体合拢装配胎架', 'DWG-ZP-2025-045 / 2套', 2, '套', '2号库房-B区-03', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-03-01'),
('LDG-003', NULL, 'TZ-COMP001-20260401-001', 'TYPE03', 'COMP001', 'DEPT003',
    '焊缝超声波检测工装', 'DWG-JC-2026-012 / 4台', 4, '台', '3号库房-C区-05', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-04-01'),
('LDG-004', 'CP-2026-0003', 'TZ-COMP001-20260620-001', 'TYPE01', 'COMP001', 'DEPT001',
    '焊接安全防护工装', 'DWG-AQ-2026-008 / 5套', 5, '套', '1号库房-A区-02', '03',
    NULL, NULL, NULL, NULL,
    '1', '2026-06-20'),
('LDG-005', NULL, 'TZ-COMP002-20260501-001', 'TYPE04', 'COMP002', 'DEPT005',
    '30吨吊梁工装', 'DWG-DD-2026-003 / 2套', 2, '套', '5号库房-A区-01', '01',
    NULL, NULL, NULL, NULL,
    '1', '2026-05-01');

-- ============================================================
-- 17. 台账明细表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LEDGER_LIST (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    LEDGER_GUID VARCHAR(64) NOT NULL COMMENT '台账主表ID(FK->T_COMPONENTS_LEDGER.GUID)',
    ITEM_NO INT DEFAULT 0 COMMENT '序号',
    COMPONENTS_NAME VARCHAR(200) COMMENT '工装名称',
    SPECIFICATION VARCHAR(200) COMMENT '规格型号',
    QUANTITY DECIMAL(10,2) DEFAULT 0 COMMENT '数量',
    UNIT VARCHAR(50) COMMENT '单位',
    REMARK VARCHAR(500) COMMENT '备注',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建人',
    CREATE_DATE DATE COMMENT '创建日期',
    KEY IDX_LEDGER_GUID (LEDGER_GUID),
    CONSTRAINT FK_LIST_LEDGER FOREIGN KEY (LEDGER_GUID) REFERENCES T_COMPONENTS_LEDGER(GUID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账明细表';

INSERT IGNORE INTO T_COMPONENTS_LEDGER_LIST (GUID, LEDGER_GUID, ITEM_NO, COMPONENTS_NAME, SPECIFICATION,
    QUANTITY, UNIT, REMARK, CREATE_USER_ID, CREATE_DATE) VALUES
('LST-001-01', 'LDG-001', 1, '定位销轴组件',   'Q235/Φ30×150',     6, '件', '含定位销及衬套',       '1', '2026-06-10'),
('LST-001-02', 'LDG-001', 2, '定位底座',       'Q345/200×300×20',  6, '件', '焊接底座',             '1', '2026-06-10'),
('LST-001-03', 'LDG-001', 3, '调节螺栓组件',   '304/M16×80',       12, '套', '不锈钢调节件',         '1', '2026-06-10'),
('LST-002-01', 'LDG-002', 1, '主支撑架',       'Q345/H型钢400×200', 4, '件', '胎架主承力结构',     '1', '2026-03-01'),
('LST-002-02', 'LDG-002', 2, '调整垫块',       '45#/200×150×50',   8, '件', '高度调节用',           '1', '2026-03-01'),
('LST-003-01', 'LDG-003', 1, '探头扫查机构',   '40Cr/定制',        4, '台', '含探头夹持及扫查轨道', '1', '2026-04-01'),
('LST-003-02', 'LDG-003', 2, '标定试块',       'Q235/100×50×25',   4, '件', 'CSK-IA标准试块',       '1', '2026-04-01');

-- ============================================================
-- 18. 台账汇总表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LEDGER_GATHER (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    TYPE_CODE VARCHAR(50) COMMENT '台账类型(FK->T_COMPONENTS_LEDGER_TYPE.CODE)',
    COMPANY_NO VARCHAR(50) COMMENT '公司编码',
    TOTAL_COUNT DECIMAL(10,2) DEFAULT 0 COMMENT '总数量',
    BORROWED_COUNT DECIMAL(10,2) DEFAULT 0 COMMENT '已借出数量',
    AVAILABLE_COUNT DECIMAL(10,2) DEFAULT 0 COMMENT '可用数量',
    MAINTENANCE_COUNT DECIMAL(10,2) DEFAULT 0 COMMENT '维修中数量',
    SCRAPPED_COUNT DECIMAL(10,2) DEFAULT 0 COMMENT '已报废数量',
    STATISTICS_DATE DATE COMMENT '统计日期',
    CREATE_DATE DATE COMMENT '创建日期',
    KEY IDX_TYPE_CODE (TYPE_CODE),
    KEY IDX_STATISTICS_DATE (STATISTICS_DATE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账汇总表';

INSERT IGNORE INTO T_COMPONENTS_LEDGER_GATHER (GUID, TYPE_CODE, COMPANY_NO, TOTAL_COUNT, BORROWED_COUNT,
    AVAILABLE_COUNT, MAINTENANCE_COUNT, SCRAPPED_COUNT, STATISTICS_DATE, CREATE_DATE) VALUES
('GTH-001', 'TYPE01', 'COMP001', 8,  3, 3, 2, 0, '2026-06-10', '2026-06-10'),
('GTH-002', 'TYPE02', 'COMP001', 2,  0, 2, 0, 0, '2026-06-10', '2026-06-10'),
('GTH-003', 'TYPE03', 'COMP001', 4,  0, 4, 0, 0, '2026-06-10', '2026-06-10'),
('GTH-004', 'TYPE04', 'COMP002', 2,  0, 2, 0, 0, '2026-06-10', '2026-06-10'),
('GTH-005', 'TYPE05', 'COMP002', 0,  0, 0, 0, 0, '2026-06-10', '2026-06-10');

-- ============================================================
-- 19. 台账单据表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LEDGER_BILL (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    BILL_NO VARCHAR(50) NOT NULL COMMENT '单据号',
    TYPE_CODE VARCHAR(50) COMMENT '台账类型(FK->T_COMPONENTS_LEDGER_TYPE.CODE)',
    COMPANY_NO VARCHAR(50) COMMENT '公司编码',
    BILL_TYPE VARCHAR(2) COMMENT '单据类型(01:入库,02:出库,03:退库)',
    BILL_DATE DATE COMMENT '单据日期',
    BORROW_DEPT VARCHAR(100) COMMENT '借用部门',
    BORROW_PERSON VARCHAR(50) COMMENT '借用人',
    ALLOT_DEPT VARCHAR(100) COMMENT '调拨部门',
    HANDLER VARCHAR(50) COMMENT '经办人',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:待审批,02:已审批,03:已完成)',
    REMARK VARCHAR(500) COMMENT '备注',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建人',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新人',
    UPDATE_DATE DATE COMMENT '更新日期',
    KEY IDX_BILL_NO (BILL_NO),
    KEY IDX_TYPE_CODE (TYPE_CODE),
    KEY IDX_BILL_TYPE (BILL_TYPE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账单据表';

INSERT IGNORE INTO T_COMPONENTS_LEDGER_BILL (GUID, BILL_NO, TYPE_CODE, COMPANY_NO, BILL_TYPE, BILL_DATE,
    BORROW_DEPT, BORROW_PERSON, ALLOT_DEPT, HANDLER, STATUS, REMARK, CREATE_USER_ID, CREATE_DATE) VALUES
('BILL-001', 'RK-COMP001-20260610-001', 'TYPE01', 'COMP001', '01', '2026-06-10',
    NULL, NULL, NULL, '张三', '03', 'H2400定位工装入库',  '2', '2026-06-10'),
('BILL-002', 'CK-COMP001-20260612-001', 'TYPE01', 'COMP001', '02', '2026-06-12',
    'DEPT002', '李四', NULL, '张三', '03', 'H2400定位工装借出至生产部', '2', '2026-06-12'),
('BILL-003', 'RK-COMP001-20260620-001', 'TYPE01', 'COMP001', '01', '2026-06-20',
    NULL, NULL, NULL, '张三', '02', '焊接安全防护工装入库(待验收)', '2', '2026-06-20'),
('BILL-004', 'RK-COMP001-20260301-001', 'TYPE02', 'COMP001', '01', '2026-03-01',
    NULL, NULL, NULL, '张三', '03', '船体合拢装配胎架入库', '2', '2026-03-01'),
('BILL-005', 'RK-COMP002-20260501-001', 'TYPE04', 'COMP002', '01', '2026-05-01',
    NULL, NULL, NULL, '张三', '03', '30吨吊梁工装入库',    '2', '2026-05-01');

-- ============================================================
-- 20. 台账类型表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LEDGER_TYPE (
    CODE VARCHAR(50) NOT NULL PRIMARY KEY COMMENT '类型编码',
    NAME VARCHAR(200) NOT NULL COMMENT '类型名称',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)',
    SORT_ORDER INT DEFAULT 0 COMMENT '排序',
    CREATE_DATE DATE COMMENT '创建日期',
    REMARK VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账类型表';

INSERT IGNORE INTO T_COMPONENTS_LEDGER_TYPE (CODE, NAME, STATUS, SORT_ORDER, CREATE_DATE) VALUES
('TYPE01', '焊接工装',   '01', 1, '2026-01-01'),
('TYPE02', '装配工装',   '01', 2, '2026-01-01'),
('TYPE03', '检测工装',   '01', 3, '2026-01-01'),
('TYPE04', '吊具工装',   '01', 4, '2026-01-01'),
('TYPE05', '机加工工装', '01', 5, '2026-01-01');

-- ============================================================
-- 21. 台账审批意见表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LEDGER_OPINION (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    BILL_GUID VARCHAR(64) COMMENT '单据ID(FK->T_COMPONENTS_LEDGER_BILL.GUID)',
    OPINION_CONTENT VARCHAR(1000) COMMENT '意见内容',
    AUDIT_USER_ID VARCHAR(50) COMMENT '审核人ID',
    AUDIT_USER_NAME VARCHAR(100) COMMENT '审核人姓名',
    AUDIT_DATE DATE COMMENT '审核日期',
    AUDIT_RESULT VARCHAR(2) COMMENT '审核结果(01:同意,02:驳回)',
    CREATE_DATE DATE COMMENT '创建日期',
    KEY IDX_BILL_GUID (BILL_GUID),
    CONSTRAINT FK_OPINION_BILL FOREIGN KEY (BILL_GUID) REFERENCES T_COMPONENTS_LEDGER_BILL(GUID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账审批意见表';

INSERT IGNORE INTO T_COMPONENTS_LEDGER_OPINION (GUID, BILL_GUID, OPINION_CONTENT,
    AUDIT_USER_ID, AUDIT_USER_NAME, AUDIT_DATE, AUDIT_RESULT, CREATE_DATE) VALUES
('LOPN-001', 'BILL-001', '入库审核通过，数量及规格符合设计要求',   '1', 'admin', '2026-06-10', '01', '2026-06-10'),
('LOPN-002', 'BILL-002', '同意借出，请于7月12日前归还',           '1', 'admin', '2026-06-12', '01', '2026-06-12'),
('LOPN-003', 'BILL-003', '同意入库，待质量部验收后正式入库',       '4', '王五',   '2026-06-20', '01', '2026-06-20'),
('LOPN-004', 'BILL-004', '入库审核通过',                           '1', 'admin', '2026-03-01', '01', '2026-03-01'),
('LOPN-005', 'BILL-005', '入库审核通过',                           '1', 'admin', '2026-05-01', '01', '2026-05-01');

-- ============================================================
-- 22. 台账附件表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_COMPONENTS_LEDGER_ATT (
    GUID VARCHAR(64) NOT NULL PRIMARY KEY COMMENT 'GUID',
    BILL_GUID VARCHAR(64) COMMENT '单据ID(FK->T_COMPONENTS_LEDGER_BILL.GUID)',
    FILE_NAME VARCHAR(200) NOT NULL COMMENT '文件名称',
    FILE_PATH VARCHAR(500) NOT NULL COMMENT '文件路径',
    FILE_SIZE VARCHAR(50) COMMENT '文件大小',
    CREATE_USER_ID VARCHAR(50) COMMENT '上传人',
    CREATE_DATE DATE COMMENT '上传日期',
    KEY IDX_BILL_GUID (BILL_GUID),
    CONSTRAINT FK_ATT_BILL FOREIGN KEY (BILL_GUID) REFERENCES T_COMPONENTS_LEDGER_BILL(GUID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账附件表';

INSERT IGNORE INTO T_COMPONENTS_LEDGER_ATT (GUID, BILL_GUID, FILE_NAME, FILE_PATH, FILE_SIZE,
    CREATE_USER_ID, CREATE_DATE) VALUES
('LATT-001', 'BILL-001', 'H2400定位工装入库验收单.pdf',  '/uploads/ledger/2026/06/入库验收单-001.pdf',  '450KB', '2', '2026-06-10'),
('LATT-002', 'BILL-002', 'H2400定位工装借出登记表.pdf',  '/uploads/ledger/2026/06/借出登记-002.pdf',  '320KB', '2', '2026-06-12'),
('LATT-003', 'BILL-004', '装配胎架入库验收单.pdf',       '/uploads/ledger/2026/03/入库验收单-004.pdf', '380KB', '2', '2026-03-01');

-- ============================================================
-- 23. 审批流程配置表
-- ============================================================
CREATE TABLE IF NOT EXISTS T_APPROVAL_FLOW (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    FLOW_NAME VARCHAR(100) NOT NULL COMMENT '流程名称',
    STEP_ORDER INT NOT NULL COMMENT '步骤顺序',
    STEP_NAME VARCHAR(100) NOT NULL COMMENT '步骤名称',
    APPROVER_ROLE VARCHAR(50) COMMENT '审批人角色',
    APPROVER_ID VARCHAR(50) COMMENT '审批人ID',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态(01:有效,02:无效)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流程配置表';

INSERT IGNORE INTO T_APPROVAL_FLOW (FLOW_NAME, STEP_ORDER, STEP_NAME, APPROVER_ROLE, APPROVER_ID, STATUS) VALUES
('工装申请审批', 1, '部门主管审批', 'DEPT_MANAGER',    '4',     '01'),
('工装申请审批', 2, '技术主管审批', 'TECH_MANAGER',    '2',     '01'),
('工装申请审批', 3, '总经理审批',   'GENERAL_MANAGER', '1',     '01'),
('台账入库审批', 1, '库管员审核',   'STOREKEEPER',     NULL,    '01'),
('台账入库审批', 2, '部门主管审批', 'DEPT_MANAGER',    '4',     '01'),
('台账出库审批', 1, '库管员审核',   'STOREKEEPER',     NULL,    '01'),
('台账出库审批', 2, '部门主管审批', 'DEPT_MANAGER',    '4',     '01'),
('台账出库审批', 3, '总经理审批',   'GENERAL_MANAGER', '1',     '01');

-- ============================================================
-- 完成提示
-- ============================================================
SELECT '所有23张数据表及基础种子数据创建完成!' AS MESSAGE;

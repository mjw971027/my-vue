-- =====================================================
-- 基础数据表创建SQL
-- 数据库: DCM
-- 包含：公司表、部门表、项目表
-- =====================================================


-- =====================================================
-- 1. 公司表: T_COMPANY (公司主体表)
-- =====================================================

DROP TABLE IF EXISTS T_COMPANY;

CREATE TABLE T_COMPANY (
    CODE VARCHAR(50) NOT NULL COMMENT '公司代码',
    DESC_CHN VARCHAR(200) COMMENT '公司中文描述',
    DESC_ENG VARCHAR(200) COMMENT '公司英文描述',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态: 01-有效, 02-无效',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (CODE),
    KEY IDX_STATUS (STATUS)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司主体表';

-- 插入示例数据
INSERT INTO T_COMPANY (CODE, DESC_CHN, DESC_ENG, STATUS, CREATE_USER_ID, CREATE_DATE) VALUES
('COMP001', '公司A', 'Company A', '01', 'admin', CURDATE()),
('COMP002', '公司B', 'Company B', '01', 'admin', CURDATE()),
('COMP003', '公司C', 'Company C', '01', 'admin', CURDATE());

-- =====================================================
-- 2. 部门表: T_DEPT (部门组织表)
-- =====================================================

DROP TABLE IF EXISTS T_DEPT;

CREATE TABLE T_DEPT (
    ORGN_CD VARCHAR(50) NOT NULL COMMENT '组织代码',
    ORGN_DESC VARCHAR(200) COMMENT '组织描述',
    SUPER_ORGN_CD VARCHAR(50) COMMENT '上级组织代码(公司代码)',
    ORGN_TYPE VARCHAR(2) COMMENT '组织类型: 01-公司, 02-部门, 03-小组',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态: 01-有效, 02-无效',
    CREATE_USER_ID VARCHAR(50) COMMENT '创建用户ID',
    CREATE_DATE DATE COMMENT '创建日期',
    UPDATE_USER_ID VARCHAR(50) COMMENT '更新用户ID',
    UPDATE_DATE DATE COMMENT '更新日期',
    REMARK VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (ORGN_CD),
    KEY IDX_SUPER_ORGN_CD (SUPER_ORGN_CD),
    KEY IDX_STATUS (STATUS)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门组织表';

-- 插入示例数据
INSERT INTO T_DEPT (ORGN_CD, ORGN_DESC, SUPER_ORGN_CD, ORGN_TYPE, STATUS, CREATE_USER_ID, CREATE_DATE) VALUES
('DEPT001', '技术部', 'COMP001', '02', '01', 'admin', CURDATE()),
('DEPT002', '生产部', 'COMP001', '02', '01', 'admin', CURDATE()),
('DEPT003', '财务部', 'COMP001', '02', '01', 'admin', CURDATE()),
('DEPT004', '技术部', 'COMP002', '02', '01', 'admin', CURDATE()),
('DEPT005', '生产部', 'COMP002', '02', '01', 'admin', CURDATE());

-- =====================================================
-- 3. 项目表: T_PROJECT (工程号表)
-- =====================================================

DROP TABLE IF EXISTS T_PROJECT;

CREATE TABLE T_PROJECT (
    CODE VARCHAR(50) NOT NULL COMMENT '项目代码/工程号',
    DESC_CHN VARCHAR(200) COMMENT '项目中文描述',
    DESC_ENG VARCHAR(200) COMMENT '项目英文描述',
    COMPANY_NO VARCHAR(50) COMMENT '所属公司代码',
    STATUS VARCHAR(2) DEFAULT '01' COMMENT '状态: 01-有效, 02-无效',
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

-- 插入示例数据
INSERT INTO T_PROJECT (CODE, DESC_CHN, DESC_ENG, COMPANY_NO, STATUS, START_DATE, CREATE_USER_ID, CREATE_DATE) VALUES
('PROJ001', '项目A', 'Project A', 'COMP001', '01', CURDATE(), 'admin', CURDATE()),
('PROJ002', '项目B', 'Project B', 'COMP001', '01', CURDATE(), 'admin', CURDATE()),
('PROJ003', '项目C', 'Project C', 'COMP002', '01', CURDATE(), 'admin', CURDATE());

-- =====================================================
-- 创建成功提示
-- =====================================================

SELECT '基础数据表创建成功!' AS MESSAGE;

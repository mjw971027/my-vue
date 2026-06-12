-- 创建用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色(ADMIN/USER)',
    status VARCHAR(10) DEFAULT '在线' COMMENT '状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入测试用户（密码：123456，已加密）
-- 密码明文是 123456，加密后的值需要用 BCrypt 生成
-- 你可以先不插入，通过注册接口创建用户

-- 用户页面权限表
CREATE TABLE IF NOT EXISTS user_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '关联用户表',
    permission_key VARCHAR(50) NOT NULL COMMENT '权限标识，如 page1, page2, page3, page4',
    UNIQUE KEY uk_user_perm (user_id, permission_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户页面权限表';

package com.mjw.vueback.demos.web.entity;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 系统设置实体类
 * 对应前端：src/api/types.ts → 设置相关接口
 */
public class Settings {

    private Long id;
    private String category;       // 设置分类（system/notification/security）
    private Map<String, Object> settings;  // 设置键值对
    private String description;    // 分类描述
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间

    // 构造函数
    public Settings() {}

    public Settings(Long id, String category, Map<String, Object> settings) {
        this.id = id;
        this.category = category;
        this.settings = settings;
    }

    // getter 和 setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Map<String, Object> getSettings() { return settings; }
    public void setSettings(Map<String, Object> settings) { this.settings = settings; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

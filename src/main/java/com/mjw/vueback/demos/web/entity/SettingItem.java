package com.mjw.vueback.demos.web.entity;

/**
 * 系统设置项实体类
 * 对应前端：src/api/types.ts → SettingItem 接口
 */
public class SettingItem {

    private Long id;
    private String name;       // 设置名称（如"深色模式"）
    private Boolean enabled;    // 是否开启

    public SettingItem() {}

    public SettingItem(Long id, String name, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }

    // getter / setter

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}

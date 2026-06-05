package com.mjw.vueback.demos.web.entity;

/**
 * 系统版本信息实体类
 * 对应前端：src/api/types.ts → SystemInfo 接口
 */
public class SystemInfo {

    private String version;     // 系统版本（如 "v2.1.0"）
    private String lastUpdate;  // 最后更新日期（如 "2026-06-02"）

    public SystemInfo() {}

    public SystemInfo(String version, String lastUpdate) {
        this.version = version;
        this.lastUpdate = lastUpdate;
    }

    // getter / setter

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
}

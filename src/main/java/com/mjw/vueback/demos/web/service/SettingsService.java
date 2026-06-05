package com.mjw.vueback.demos.web.service;

import com.mjw.vueback.demos.web.entity.SettingItem;
import com.mjw.vueback.demos.web.entity.SystemInfo;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置服务类
 * 处理系统设置相关的业务逻辑
 */
@Service
public class SettingsService {

    /**
     * 模拟设置数据存储
     */
    private final List<SettingItem> settingsDatabase = new ArrayList<>();

    /**
     * 系统版本信息
     */
    private SystemInfo systemInfo;

    /**
     * 构造函数：初始化模拟数据
     */
    public SettingsService() {
        initMockData();
    }

    /**
     * 初始化模拟设置数据
     */
    private void initMockData() {
        // 初始化设置项
        settingsDatabase.add(new SettingItem(1L, "深色模式", true));
        settingsDatabase.add(new SettingItem(2L, "消息通知", true));
        settingsDatabase.add(new SettingItem(3L, "自动保存", false));
        settingsDatabase.add(new SettingItem(4L, "数据同步", true));

        // 初始化系统信息
        systemInfo = new SystemInfo();
        systemInfo.setVersion("v2.1.0");
        systemInfo.setLastUpdate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    /**
     * 获取所有设置项
     * 对应前端：src/api/settings.ts → getSettings()
     * 对应 Controller：SettingsController.getSettings()
     *
     * @return 设置项列表
     */
    public List<SettingItem> getAllSettings() {
        // 实际项目中应从数据库查询
        // 这里返回模拟数据
        return new ArrayList<>(settingsDatabase);
    }

    /**
     * 切换设置项状态
     * 对应前端：src/api/settings.ts → toggleSetting()
     * 对应 Controller：SettingsController.toggleSetting()
     *
     * @param id 设置项ID
     */
    public void toggle(Long id) {
        // 查找并设置项
        for (SettingItem item : settingsDatabase) {
            if (item.getId().equals(id)) {
                // 切换开关状态
                item.setEnabled(!item.getEnabled());
                return;
            }
        }

        // 如果没找到，抛出异常
        throw new RuntimeException("设置项不存在: " + id);
    }

    /**
     * 获取系统版本信息
     * 对应前端：src/api/settings.ts → getSystemInfo()
     * 对应 Controller：SettingsController.getSystemInfo()
     *
     * @return 系统信息
     */
    public SystemInfo getSystemInfo() {
        // 实际项目中应从配置文件或数据库读取
        // 这里返回模拟数据
        return systemInfo;
    }

    /**
     * 添加设置项（扩展方法）
     * @param item 设置项
     * @return 添加后的设置项
     */
    public SettingItem addSetting(SettingItem item) {
        // 生成新ID
        Long newId = settingsDatabase.stream()
                .mapToLong(SettingItem::getId)
                .max()
                .orElse(0L) + 1;
        item.setId(newId);

        settingsDatabase.add(item);
        return item;
    }

    /**
     * 更新设置项（扩展方法）
     * @param id 设置项ID
     * @param item 更新后的设置项
     * @return 更新后的设置项
     */
    public SettingItem updateSetting(Long id, SettingItem item) {
        for (int i = 0; i < settingsDatabase.size(); i++) {
            if (settingsDatabase.get(i).getId().equals(id)) {
                item.setId(id);
                settingsDatabase.set(i, item);
                return item;
            }
        }
        throw new RuntimeException("设置项不存在: " + id);
    }

    /**
     * 删除设置项（扩展方法）
     * @param id 设置项ID
     */
    public void deleteSetting(Long id) {
        settingsDatabase.removeIf(item -> item.getId().equals(id));
    }
}

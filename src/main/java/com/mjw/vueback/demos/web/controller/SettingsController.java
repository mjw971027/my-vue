package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.SettingItem;
import com.mjw.vueback.demos.web.entity.SystemInfo;
import com.mjw.vueback.demos.web.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统设置相关 Controller
 * 对应前端：src/api/settings.ts → getSettings() / toggleSetting() / getSystemInfo()
 */
@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "http://localhost:5173")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    /**
     * GET /api/settings/list
     * 获取所有设置项
     */
    @GetMapping("/list")
    public ApiResponse<List<SettingItem>> getSettings() {
        List<SettingItem> list = settingsService.getAllSettings();
        return ApiResponse.success(list);
    }

    /**
     * PUT /api/settings/toggle/{id}
     * 切换指定设置项的开关状态
     */
    @PutMapping("/toggle/{id}")
    public ApiResponse<Void> toggleSetting(@PathVariable Long id) {
        settingsService.toggle(id);
        return ApiResponse.success(null);
    }

    /**
     * GET /api/settings/system-info
     * 获取系统版本信息
     */
    @GetMapping("/system-info")
    public ApiResponse<SystemInfo> getSystemInfo() {
        SystemInfo info = settingsService.getSystemInfo();
        return ApiResponse.success(info);
    }
}

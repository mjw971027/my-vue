package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.MonthlyStats;
import com.mjw.vueback.demos.web.entity.StatsSummary;
import com.mjw.vueback.demos.web.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 统计数据相关 Controller
 * 对应前端：src/api/stats.ts → getMonthlyStats() / getStatsSummary()
 */
@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:5173")
public class StatsController {

    @Autowired
    private StatsService statsService;

    /**
     * GET /api/stats/monthly
     * 获取月度统计数据（柱状图用）
     */
    @GetMapping("/monthly")
    public ApiResponse<List<MonthlyStats>> getMonthlyStats() {
        List<MonthlyStats> data = statsService.getMonthlyData();
        return ApiResponse.success(data);
    }

    /**
     * GET /api/stats/summary
     * 获取统计摘要（总收入、最高月份）
     */
    @GetMapping("/summary")
    public ApiResponse<StatsSummary> getSummary() {
        StatsSummary summary = statsService.getSummary();
        return ApiResponse.success(summary);
    }
}

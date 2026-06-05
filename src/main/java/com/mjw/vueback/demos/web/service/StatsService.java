package com.mjw.vueback.demos.web.service;

import com.mjw.vueback.demos.web.entity.DailyStats;
import com.mjw.vueback.demos.web.entity.MonthlyStats;
import com.mjw.vueback.demos.web.entity.StatsSummary;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 统计服务类
 * 处理统计相关的业务逻辑
 */
@Service
public class StatsService {

    /**
     * 模拟月度统计数据存储
     */
    private final Map<String, MonthlyStats> monthlyStatsDatabase = new ConcurrentHashMap<>();

    /**
     * 模拟每日统计数据存储
     */
    private final Map<String, DailyStats> dailyStatsDatabase = new ConcurrentHashMap<>();

    /**
     * 构造函数：初始化模拟数据
     */
    public StatsService() {
        initMockData();
    }

    /**
     * 初始化模拟统计数据
     */
    private void initMockData() {
        // 初始化近6个月的月度统计
        for (int i = 5; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            String monthKey = date.getYear() + "-" + String.format("%02d", date.getMonthValue());

            MonthlyStats stats = new MonthlyStats();
            stats.setMonth(monthKey);
            stats.setSales(1000 + (int)(Math.random() * 2000));
            stats.setOrders(50 + (int)(Math.random() * 150));
            stats.setCustomers(30 + (int)(Math.random() * 100));
            stats.setGrowthRate(-10 + (Math.random() * 30)); // -10% 到 20%
            stats.setCreateTime(LocalDateTime.now().minusMonths(i));

            monthlyStatsDatabase.put(monthKey, stats);
        }

        // 初始化近7天的每日统计
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateKey = date.toString();

            DailyStats dailyStats = new DailyStats();
            dailyStats.setDate(dateKey);
            dailyStats.setSales(100 + (int)(Math.random() * 500));
            dailyStats.setOrders(5 + (int)(Math.random() * 30));
            dailyStats.setCustomers(3 + (int)(Math.random() * 20));
            dailyStats.setCreateTime(LocalDateTime.now().minusDays(i));

            dailyStatsDatabase.put(dateKey, dailyStats);
        }
    }

    /**
     * 获取月度统计数据（用于图表展示）
     * 对应前端：src/api/stats.ts → getMonthlyStats()
     * 对应 Controller：StatsController.getMonthlyStats()
     *
     * @return 月度统计列表
     */
    public List<MonthlyStats> getMonthlyData() {
        List<MonthlyStats> result = new ArrayList<>(monthlyStatsDatabase.values());

        // 按月份排序
        result.sort((a, b) -> a.getMonth().compareTo(b.getMonth()));

        return result;
    }

    /**
     * 获取统计汇总数据
     * 对应前端：src/api/stats.ts → getStatsSummary()
     * 对应 Controller：StatsController.getSummary()
     *
     * @return 统计汇总
     */
    public StatsSummary getSummary() {
        StatsSummary summary = new StatsSummary();

        // 计算汇总数据
        List<MonthlyStats> allMonthly = getMonthlyData();

        // 总销售额
        int totalSales = allMonthly.stream()
                .mapToInt(MonthlyStats::getSales)
                .sum();
        summary.setTotalSales(totalSales);

        // 总订单数
        int totalOrders = allMonthly.stream()
                .mapToInt(MonthlyStats::getOrders)
                .sum();
        summary.setTotalOrders(totalOrders);

        // 总客户数（简单求和，实际应去重）
        int totalCustomers = allMonthly.stream()
                .mapToInt(MonthlyStats::getCustomers)
                .sum();
        summary.setTotalCustomers(totalCustomers);

        // 平均增长率
        double avgGrowth = allMonthly.stream()
                .mapToDouble(MonthlyStats::getGrowthRate)
                .average()
                .orElse(0.0);
        summary.setAverageGrowthRate(Math.round(avgGrowth * 100.0) / 100.0);

        // 今日销售额和订单数
        String today = LocalDate.now().toString();
        DailyStats todayStats = dailyStatsDatabase.get(today);
        if (todayStats != null) {
            summary.setTodaySales(todayStats.getSales());
            summary.setTodayOrders(todayStats.getOrders());
        } else {
            summary.setTodaySales(0);
            summary.setTodayOrders(0);
        }

        summary.setUpdateTime(LocalDateTime.now());

        return summary;
    }

    /**
     * 获取每日统计数据
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @return 每日统计列表
     */
    public List<DailyStats> getDailyData(String startDate, String endDate) {
        List<DailyStats> result = new ArrayList<>(dailyStatsDatabase.values());

        // 日期过滤
        if (startDate != null && !startDate.isEmpty()) {
            final String finalStartDate = startDate;
            result = result.stream()
                    .filter(stats -> stats.getDate().compareTo(finalStartDate) >= 0)
                    .collect(Collectors.toList());
        }

        if (endDate != null && !endDate.isEmpty()) {
            final String finalEndDate = endDate;
            result = result.stream()
                    .filter(stats -> stats.getDate().compareTo(finalEndDate) <= 0)
                    .collect(Collectors.toList());
        }

        // 按日期排序
        result.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        return result;
    }

    /**
     * 添加月度统计（扩展方法）
     * @param stats 月度统计实体
     * @return 添加后的实体
     */
    public MonthlyStats addMonthlyStats(MonthlyStats stats) {
        stats.setCreateTime(LocalDateTime.now());
        monthlyStatsDatabase.put(stats.getMonth(), stats);

        // 更新汇总
        return stats;
    }

    /**
     * 添加每日统计（扩展方法）
     * @param stats 每日统计实体
     * @return 添加后的实体
     */
    public DailyStats addDailyStats(DailyStats stats) {
        stats.setCreateTime(LocalDateTime.now());
        dailyStatsDatabase.put(stats.getDate(), stats);
        return stats;
    }
}

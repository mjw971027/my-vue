package com.mjw.vueback.demos.web.entity;

import java.time.LocalDateTime;

/**
 * 统计汇总数据实体类
 * 对应前端：src/api/types.ts → StatsSummary 接口
 */
public class StatsSummary {

    private Integer totalSales;        // 总销售额
    private Integer totalOrders;       // 总订单数
    private Integer totalCustomers;    // 总客户数
    private Double averageGrowthRate;  // 平均增长率
    private Integer todaySales;        // 今日销售额
    private Integer todayOrders;       // 今日订单数
    private LocalDateTime updateTime;  // 更新时间

    // 构造函数
    public StatsSummary() {}

    // getter 和 setter 方法
    public Integer getTotalSales() { return totalSales; }
    public void setTotalSales(Integer totalSales) { this.totalSales = totalSales; }

    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }

    public Integer getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(Integer totalCustomers) { this.totalCustomers = totalCustomers; }

    public Double getAverageGrowthRate() { return averageGrowthRate; }
    public void setAverageGrowthRate(Double averageGrowthRate) { this.averageGrowthRate = averageGrowthRate; }

    public Integer getTodaySales() { return todaySales; }
    public void setTodaySales(Integer todaySales) { this.todaySales = todaySales; }

    public Integer getTodayOrders() { return todayOrders; }
    public void setTodayOrders(Integer todayOrders) { this.todayOrders = todayOrders; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

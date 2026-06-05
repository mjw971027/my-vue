package com.mjw.vueback.demos.web.entity;

import java.time.LocalDateTime;

/**
 * 月度统计数据实体类
 * 对应前端：src/api/types.ts → MonthlyStats 接口
 */
public class MonthlyStats {

    private String month;          // 月份（格式：yyyy-MM）
    private Integer sales;         // 销售额
    private Integer orders;        // 订单数
    private Integer customers;     // 客户数
    private Double growthRate;     // 增长率（百分比）
    private LocalDateTime createTime;  // 创建时间

    // 构造函数
    public MonthlyStats() {}

    public MonthlyStats(String month, Integer sales, Integer orders) {
        this.month = month;
        this.sales = sales;
        this.orders = orders;
    }

    // getter 和 setter 方法
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Integer getSales() { return sales; }
    public void setSales(Integer sales) { this.sales = sales; }

    public Integer getOrders() { return orders; }
    public void setOrders(Integer orders) { this.orders = orders; }

    public Integer getCustomers() { return customers; }
    public void setCustomers(Integer customers) { this.customers = customers; }

    public Double getGrowthRate() { return growthRate; }
    public void setGrowthRate(Double growthRate) { this.growthRate = growthRate; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

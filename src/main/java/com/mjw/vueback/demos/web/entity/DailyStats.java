package com.mjw.vueback.demos.web.entity;

import java.time.LocalDateTime;

/**
 * 每日统计数据实体类
 * 用于前端图表展示
 */
public class DailyStats {

    private String date;           // 日期（格式：yyyy-MM-dd）
    private Integer sales;         // 销售额
    private Integer orders;        // 订单数
    private Integer customers;     // 客户数
    private LocalDateTime createTime;  // 创建时间

    // 构造函数
    public DailyStats() {}

    public DailyStats(String date, Integer sales, Integer orders) {
        this.date = date;
        this.sales = sales;
        this.orders = orders;
    }

    // getter 和 setter 方法
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Integer getSales() { return sales; }
    public void setSales(Integer sales) { this.sales = sales; }

    public Integer getOrders() { return orders; }
    public void setOrders(Integer orders) { this.orders = orders; }

    public Integer getCustomers() { return customers; }
    public void setCustomers(Integer customers) { this.customers = customers; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

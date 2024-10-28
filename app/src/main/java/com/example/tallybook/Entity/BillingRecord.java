package com.example.tallybook.Entity;

import java.util.ArrayList;
import java.util.List;

public class BillingRecord {
    private double amount; // 金额
    private String category; // 类别
    private String date; // 日期
    private String remark; // 备注
    private String time; // 时间

    public BillingRecord(double amount, String category, String date, String remark, String time) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.remark = remark;
        this.time = time;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public static List<BillingRecord> getSimpleRecords() {
        List<BillingRecord> records = new ArrayList<>();
        records.add(new BillingRecord(100, "餐饮", "2021-01-01", "早餐safasfsafsafasfasfsafasfasfsafasfasf", "08:00"));
        records.add(new BillingRecord(200, "交通", "2021-01-01", "地铁safasfasfsafasfasfasf", "09:00"));
        records.add(new BillingRecord(300, "购物", "2021-01-01", "衣服萨芬萨芬阿斯弗萨芬萨芬safasfasfasfafasfasfasfasfasfasafasfasfafs", "10:00"));
        records.add(new BillingRecord(400, "餐饮", "2021-01-01", "午餐", "12:00"));
        records.add(new BillingRecord(500, "交通", "2021-01-01", "公交", "13:00"));
        records.add(new BillingRecord(600, "购物", "2021-01-01", "鞋子", "14:00"));
        records.add(new BillingRecord(700, "餐饮", "2021-01-01", "晚餐", "18:00"));
        records.add(new BillingRecord(800, "交通", "2021-01-01", "出租车", "19:00"));
        records.add(new BillingRecord(900, "购物", "2021-01-01", "包包", "20:00"));
        return records;
    }
}

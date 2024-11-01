package com.example.tallybook.Entity;

import java.util.ArrayList;
import java.util.List;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName="BillingRecords")
public class BillingRecord implements Serializable {
    @PrimaryKey
    private int id; // 主键
    private double amount; // 金额
    private String category; // 类别
    private String remark; // 备注
    private String year; // 年份
    private String month; // 月份
    private String day; // 日期
    private String time; // 时间

    public BillingRecord(int id, double amount, String category, String year, String month, String day, String time, String remark) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.remark = remark;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public static List<BillingRecord> getSimpleRecords() {
        List<BillingRecord> records = new ArrayList<>();
        records.add(new BillingRecord(1, 100, "餐饮", "2021", "01", "01", "12:00", "吃饭"));
        records.add(new BillingRecord(2, 200, "交通", "2021", "01", "02", "12:00", "打车"));
        records.add(new BillingRecord(3, 300, "购物", "2021", "01", "03", "12:00", "买衣服"));
        records.add(new BillingRecord(4, 400, "餐饮", "2021", "01", "04", "12:00", "吃饭"));
        records.add(new BillingRecord(5, 500, "交通", "2021", "01", "05", "12:00", "打车"));
        records.add(new BillingRecord(6, 600, "购物", "2021", "01", "06", "12:00", "买衣服"));
        records.add(new BillingRecord(7, 700, "餐饮", "2021", "01", "07", "12:00", "吃饭"));
        return records;
    }
}

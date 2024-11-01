package com.example.tallybook.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import com.example.tallybook.MainApplication;

public class CalendarDay {
    String day;
    String amount;
    public CalendarDay(String day, String amount) {
        this.day = day;
        this.amount = amount;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    //返回指定年，月的日历
    public static List<CalendarDay> getCalendarDays(int year, int month) {
        List<CalendarDay> days = new ArrayList<>();
        // 获取指定年，月的第一天是星期几
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(firstDayOfWeek == 0) {
            firstDayOfWeek = 7;
        }
        // 获取指定年，月的最后一天是几号
        calendar.set(year, month, 0);
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);
        // 添加指定年，月的日历
        for(int i = 0; i < firstDayOfWeek - 1; i++) {
            days.add(new CalendarDay("null", "null"));
        }
        for(int i = 1; i <= lastDay; i++) {
            double amount = 0;
            amount = MainApplication.getInstance().getRecordDB().getBillingRecordDao().getAmountByDay(String.valueOf(year), String.valueOf(month), String.valueOf(i));
            days.add(new CalendarDay(String.valueOf(i), String.valueOf(amount)));
        }
        for(int i = 0; i < 42 - lastDay - firstDayOfWeek + 1; i++) {
            days.add(new CalendarDay("null", "null"));
        }
        return days;
    }
}

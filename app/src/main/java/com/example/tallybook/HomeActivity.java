package com.example.tallybook;

import java.util.Calendar;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.Entity.CalendarDay;
import com.example.tallybook.Adapter.SimpleRecordsAdapter;
import com.example.tallybook.Adapter.CalendarAdapter;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitRecyclerViewOfSimpleBillingHistory();   // 初始化简易账单历史的循环视图
        InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
    }

    // 初始化简易账单历史的循环视图
    private void InitRecyclerViewOfSimpleBillingHistory() {
        RecyclerView rvSimpleRecords = findViewById(R.id.home_history_recycler_view);   // 获取循环视图

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);   // 创建线性布局管理器
        rvSimpleRecords.setLayoutManager(layoutManager);   // 设置循环视图的布局管理器

        SimpleRecordsAdapter adapter = new SimpleRecordsAdapter(this, BillingRecord.getSimpleRecords());   // 创建简单记录适配器
        rvSimpleRecords.setAdapter(adapter);   // 设置循环视图的适配器
    }

    // 初始化日历的循环视图
    private void InitRecyclerViewOfCalendat() {
        Calendar calendar = Calendar.getInstance();   // 创建日历对象
        int currentYear = calendar.get(Calendar.YEAR);   // 获取当前年份
        int currentMonth = calendar.get(Calendar.MONTH) + 1;   // 获取当前月份

        RecyclerView rvCalendar = findViewById(R.id.home_calendar_recycler_view);   // 获取循环视图

        LinearLayoutManager gridManager = new GridLayoutManager(this, 7);   // 创建网格布局管理器
        rvCalendar.setLayoutManager(gridManager);   // 设置循环视图的布局管理器

        CalendarAdapter adapter = new CalendarAdapter(this, CalendarDay.getCalendarDays(currentYear, currentMonth));   // 创建日历适配器
        rvCalendar.setAdapter(adapter);   // 设置循环视图的适配器
    }
}
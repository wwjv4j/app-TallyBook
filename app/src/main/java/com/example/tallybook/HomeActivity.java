package com.example.tallybook;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import android.widget.TextView;
import android.content.Intent;

import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.Entity.CalendarDay;
import com.example.tallybook.Adapter.SimpleRecordsAdapter;
import com.example.tallybook.Adapter.CalendarAdapter;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Database.RecordDatabase;

public class HomeActivity extends AppCompatActivity {
    private BillingRecordDao billingRecordDao = MainApplication.getInstance().getRecordDB().getBillingRecordDao();   // 声明一个账单记录数据访问对象
    private List<BillingRecord> billingRecords = new ArrayList<>();   // 声明一个账单记录列表
    private SimpleRecordsAdapter simpleRecordsAdapter;   // 声明一个简单记录适配器
    private CalendarAdapter calendarAdapter;   // 声明一个日历适配器
    public static int currentYear = Calendar.getInstance().get(Calendar.YEAR);   // 当前年份
    public static int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;   // 当前月份
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
        InitOverview();   // 初始化概览
        InitRecyclerViewOfSimpleBillingHistory();   // 初始化简易账单历史的循环视图
        InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
        InitButtonEvent();   // 初始化按钮事件
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        List<BillingRecord> newBillingRecords = billingRecordDao.getAllRecords();   // 获取账单记录列表
        Collections.reverse(newBillingRecords);   // 反转账单记录列表
        billingRecords.clear();   // 清空账单记录列表
        billingRecords.addAll(newBillingRecords);   // 添加新的账单记录
        simpleRecordsAdapter.notifyDataSetChanged();   // 通知适配器数据发生变化

        InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
    }

    // 初始化概览
    private void InitOverview() {
        TextView tvTotalAmount = findViewById(R.id.home_total_amount);   // 获取总金额文本
        double amountByMounth = billingRecordDao.getAmountByMonth(String.valueOf(currentYear),String.valueOf(currentMonth));
        tvTotalAmount.setText("本月总账单: " + String.valueOf(amountByMounth));   // 设置总金额文本
    }

    // 初始化按钮事件
    private void InitButtonEvent() {
        TextView tvAdd = findViewById(R.id.home_add_record);   // 获取添加按钮
        tvAdd.setOnClickListener(v -> {
            // 跳转到添加账单页面
            Intent intent = new Intent(this, AddRecordActivity.class);
            startActivity(intent);
        });
    }
    // 初始化简易账单历史的循环视图
    private void InitRecyclerViewOfSimpleBillingHistory() {
        billingRecords = billingRecordDao.getAllRecords();   // 获取账单记录列表
        Collections.reverse(billingRecords);   // 反转账单记录列表
        RecyclerView rvSimpleRecords = findViewById(R.id.home_history_recycler_view);   // 获取循环视图
        // 创建线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);   // 创建线性布局管理器
        rvSimpleRecords.setLayoutManager(layoutManager);   // 设置循环视图的布局管理器
        // 创建简单记录适配器
        simpleRecordsAdapter = new SimpleRecordsAdapter(this, billingRecords);   // 创建简单记录适配器
        rvSimpleRecords.setAdapter(simpleRecordsAdapter);   // 设置循环视图的适配器
    }

    // 初始化日历的循环视图
    private void InitRecyclerViewOfCalendat() {
        RecyclerView rvCalendar = findViewById(R.id.home_calendar_recycler_view);   // 获取循环视图
        // 创建网格布局管理器
        LinearLayoutManager gridManager = new GridLayoutManager(this, 7);   // 创建网格布局管理器
        rvCalendar.setLayoutManager(gridManager);   // 设置循环视图的布局管理器
        // 创建日历适配器
        calendarAdapter = new CalendarAdapter(this, CalendarDay.getCalendarDays(currentYear, currentMonth));   // 创建日历适配器
        rvCalendar.setAdapter(calendarAdapter);   // 设置循环视图的适配器
    }
}
package com.example.tallybook;

import java.util.Calendar;
import java.util.List;
import java.util.Locale.Category;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

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
import android.widget.RadioButton;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.LimitLine;

import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.Entity.CalendarDay;
import com.example.tallybook.Adapter.SimpleRecordsAdapter;
import com.example.tallybook.Adapter.CalendarAdapter;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Database.RecordDatabase;
import com.example.tallybook.Entity.CategoryAmount;
import com.example.tallybook.AnalyseActivity;
import com.example.tallybook.MineActivity;

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
        InitNavigation();   // 初始化导航
        InitLineChart();   // 初始化折线图
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        List<BillingRecord> newBillingRecords = billingRecordDao.getAllRecords();   // 获取账单记录列表
        Collections.reverse(newBillingRecords);   // 反转账单记录列表
        billingRecords.clear();   // 清空账单记录列表
        billingRecords.addAll(newBillingRecords);   // 添加新的账单记录
        simpleRecordsAdapter.notifyDataSetChanged();   // 通知适配器数据发生变化
        InitOverview();   // 初始化概览
        InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
    }
    
    // 初始化折线图
    private void InitLineChart() {
        List<BillingRecord> records = billingRecordDao.getMonthRecords(String.valueOf(currentYear), String.valueOf(currentMonth));   // 获取指定年份、月份的账单记录
        List<Entry> entries = new ArrayList<>();   // 声明一个条目列表
        float days[] = new float[32];   // 声明一个天数数组
        for (int i = 0; i < records.size(); i++) {
            BillingRecord record = records.get(i);   // 获取账单记录
            days[Integer.parseInt(record.getDay())] = (float)record.getAmount();   // 标记天数
        }
        for (int i = 1; i < 32; i++) {
            entries.add(new Entry(i, days[i]));   // 添加条目
        }
        LineChart lineChart = findViewById(R.id.home_line_chart);   // 获取折线图
        LineDataSet dataSet = new LineDataSet(entries, "");   // 创建折线数据集
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);   // 设置颜色
        LineData data = new LineData(dataSet);   // 创建折线数据
        lineChart.setData(data);   // 设置折线图数据
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // 设置最小间隔为 1
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(15, true); // 设置显示的标签数量，这里的 10 可以根据数据量调整
        
        lineChart.invalidate();   // 刷新折线图
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
        // billingRecordDao.deleteAllRecords();
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

    // 初始化导航
    private void InitNavigation() {
        RadioButton rbAnalyse = findViewById(R.id.navigation_analyse);   // 获取分析按钮
        RadioButton rbMine = findViewById(R.id.navigation_mine);   // 获取我的按钮
        rbAnalyse.setOnClickListener(v -> {
            // 跳转到分析页面
            Intent intent = new Intent(this, AnalyseActivity.class);
            startActivity(intent);
        });
        rbMine.setOnClickListener(v -> {
            // 跳转到我的页面
            Intent intent = new Intent(this, MineActivity.class);
            startActivity(intent);
        });
    }
}
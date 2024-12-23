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
import android.widget.ImageView;
import android.content.Intent;
import android.widget.RadioButton;
import android.graphics.Color;
import android.content.SharedPreferences;

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
        InitLaunchBooting();   // 初始化启动引导
        InitOverview();   // 初始化概览
        InitRecyclerViewOfSimpleBillingHistory();   // 初始化简易账单历史的循环视图
        InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
        InitButtonEvent();   // 初始化按钮事件
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
        InitLineChart();
        InitNavigation();   // 初始化导航
    }
    @Override
    protected void onPause() {
        super.onPause();
        UpdateCalendar();
    }

    // 初始化启动引导
    private void InitLaunchBooting() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // 显示特殊页面
            Intent intent = new Intent(this, LaunchBootingActivity.class);   // 创建一个意图
            startActivity(intent);   // 启动意图

            // 设置第一次运行标志为 false
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }

        
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

        ImageView ivLastMonth = findViewById(R.id.home_last_month);   // 获取上个月按钮
        ivLastMonth.setOnClickListener(v->{
            if(currentMonth == 1) {
                currentYear--;
                currentMonth = 12;
            } else {
                currentMonth--;
            }
            InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
            TextView tvCalendarDate = findViewById(R.id.home_calendar_date);   // 获取日历日期文本
            tvCalendarDate.setText(String.format("%d-%d", currentYear, currentMonth));   // 设置日历日期文本
        });
        ImageView ivNextMonth = findViewById(R.id.home_next_month);   // 获取下个月按钮
        ivNextMonth.setOnClickListener(v->{
            if(currentMonth == 12) {
                currentYear++;
                currentMonth = 1;
            } else {
                currentMonth++;
            }
            InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
            TextView tvCalendarDate = findViewById(R.id.home_calendar_date);   // 获取日历日期文本
            tvCalendarDate.setText(String.format("%d-%d", currentYear, currentMonth));   // 设置日历日期文本
        });
        ImageView ivNextYear = findViewById(R.id.home_next_year);   // 获取下一年按钮
        ivNextYear.setOnClickListener(v->{
            currentYear++;
            InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
            TextView tvCalendarDate = findViewById(R.id.home_calendar_date);   // 获取日历日期文本
            tvCalendarDate.setText(String.format("%d-%d", currentYear, currentMonth));   // 设置日历日期文本
        });
        ImageView ivLastYear = findViewById(R.id.home_last_year);   // 获取上一年按钮
        ivLastYear.setOnClickListener(v->{
            currentYear--;
            InitRecyclerViewOfCalendat();   // 初始化日历的循环视图
            TextView tvCalendarDate = findViewById(R.id.home_calendar_date);   // 获取日历日期文本
            tvCalendarDate.setText(String.format("%d-%d", currentYear, currentMonth));   // 设置日历日期文本
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
        if(billingRecords.size() > 10) {
            billingRecords = billingRecords.subList(0, 10);
        }
        // 按照日期排序
        Collections.sort(billingRecords, (o1, o2) -> {
            return o2.getDate().compareTo(o1.getDate());
        });
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
        RadioButton rbHome = findViewById(R.id.navigation_home);   // 获取首页按钮
        RadioButton rbAnalyse = findViewById(R.id.navigation_analyse);   // 获取分析按钮
        RadioButton rbMine = findViewById(R.id.navigation_mine);   // 获取我的按钮
        rbHome.setChecked(true);   // 设置首页按钮为选中状态
        
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

    void UpdateCalendar() {
        TextView tvCalendarDate = findViewById(R.id.home_calendar_date);   // 获取日历日期文本
        currentYear = Calendar.getInstance().get(Calendar.YEAR);   // 当前年份
        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;   // 当前月份
        tvCalendarDate.setText(String.format("%d-%d", currentYear, currentMonth));   // 设置日历日期文本
    }
}
package com.example.tallybook;

import android.os.Bundle;

import java.util.Calendar;
import java.util.List;
import java.util.Locale.Category;
import java.lang.reflect.Array;
import java.util.ArrayList;
import android.os.Bundle;
import java.util.Collections;
import java.util.stream.Collectors;

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


import com.example.tallybook.Entity.CategoryAmount;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.Entity.CalendarDay;
import com.example.tallybook.Entity.CategoryAmount;
import com.example.tallybook.Adapter.SimpleRecordsAdapter;
import com.example.tallybook.Adapter.CalendarAdapter;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Database.RecordDatabase;
import com.example.tallybook.Entity.CategoryAmount;
import com.example.tallybook.HomeActivity;
import com.example.tallybook.MineActivity;

public class AnalyseActivity extends AppCompatActivity {
    private BillingRecordDao billingRecordDao = MainApplication.getInstance().getRecordDB().getBillingRecordDao();   // 声明一个账单记录数据访问对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analyse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        InitPieChart();   // 初始化饼图
        InitButtonEvent();   // 初始化按钮
        UpdateCalendar();   // 更新日历
        InitAnalyse();   // 初始化分析
    }
    @Override
    protected void onResume() {
        super.onResume();
        InitNavigation();   // 初始化导航栏
    }
    @Override
    protected void onPause() {
        super.onPause();
        UpdateCalendar();   // 更新日历
    }

    // 初始化分析
    private void InitAnalyse() {
        // 获取总金额文本
        TextView tvTotalAmount = findViewById(R.id.analyse_total_amount);   
        tvTotalAmount.setText("本月消费：" + String.valueOf(billingRecordDao.getAmountByMonth(String.valueOf(HomeActivity.currentYear), String.valueOf(HomeActivity.currentMonth))));   // 设置总金额文本
        
        List<CategoryAmount> categoryAmountList = billingRecordDao.getCategoryAmountByMonth(String.valueOf(HomeActivity.currentYear), String.valueOf(HomeActivity.currentMonth));
        // 设置第一消费类别文本
        TextView tvFirstCategory = findViewById(R.id.analyse_first_cost);
        if(categoryAmountList.size() == 0) {
            tvFirstCategory.setText("本月最大支出 无");
        } else {
            tvFirstCategory.setText("本月最大支出 " + categoryAmountList.get(0).category + ":  " + categoryAmountList.get(0).allAmount);
        }
        // 设置第二消费类别文本
        TextView tvSecondCategory = findViewById(R.id.analyse_second_cost);
        if(categoryAmountList.size() < 2) {
            tvSecondCategory.setText("本月第二大支出 无");
        } else {
            tvSecondCategory.setText("本月第二大支出 " + categoryAmountList.get(1).category + ":  " + categoryAmountList.get(1).allAmount);
        }
            // 设置第三消费类别文本
        TextView tvThirdCategory = findViewById(R.id.analyse_third_cost);
        if(categoryAmountList.size() < 3) {
            tvThirdCategory.setText("本月第三大支出 无");
        } else {
            tvThirdCategory.setText("本月第三大支出 " + categoryAmountList.get(2).category + ":  " + categoryAmountList.get(2).allAmount);
        }
    }
    private void InitButtonEvent() {
        ImageView ivLastMonth = findViewById(R.id.analyse_last_month);   // 获取上个月按钮
        ivLastMonth.setOnClickListener(v->{
            if(HomeActivity.currentMonth == 1) {
                HomeActivity.currentYear--;
                HomeActivity.currentMonth = 12;
            } else {
                HomeActivity.currentMonth--;
            }
            InitAnalyse();
            InitPieChart();
            TextView tvCalendarDate = findViewById(R.id.analyse_calendar_date);   // 获取日历日期文本
            tvCalendarDate.setText(String.format("%d-%d", HomeActivity.currentYear, HomeActivity.currentMonth));   // 设置日历日期文本
        });
        ImageView ivNextMonth = findViewById(R.id.analyse_next_month);   // 获取下个月按钮
        ivNextMonth.setOnClickListener(v->{
            if(HomeActivity.currentMonth == 12) {
                HomeActivity.currentYear++;
                HomeActivity.currentMonth = 1;
            } else {
                HomeActivity.currentMonth++;
            }
            InitAnalyse();
            InitPieChart();
            TextView tvCalendarDate = findViewById(R.id.analyse_calendar_date);   // 获取日历日期文本
            tvCalendarDate.setText(String.format("%d-%d", HomeActivity.currentYear, HomeActivity.currentMonth));   // 设置日历日期文本
        });
    }
    // 初始化饼图
    private void InitPieChart() {
        // 获取饼图视图
        PieChart pieChartView = findViewById(R.id.analyse_pie_chart);
        List<CategoryAmount> categoryAmountList = billingRecordDao.getCategoryAmountByMonth(String.valueOf(HomeActivity.currentYear), String.valueOf(HomeActivity.currentMonth));
        ArrayList<PieEntry> entries = categoryAmountList.stream().map(CategoryAmount::toPieEntry).collect(Collectors.toCollection(ArrayList::new));
        
        if(entries.size() == 0) {
            pieChartView.clear();
            pieChartView.setNoDataText("暂无数据");
        }else {
            // 设置饼图数据
            PieDataSet dataSet = new PieDataSet(entries, "类别");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);
            pieChartView.setData(data);
            pieChartView.getDescription().setEnabled(false);
            pieChartView.setDrawHoleEnabled(false);
            pieChartView.invalidate();
        }
    }
    
    // 初始化导航栏
    private void InitNavigation() {
        RadioButton rbHome = findViewById(R.id.navigation_home);
        RadioButton rbMine = findViewById(R.id.navigation_mine);
        RadioButton rbAnalyse = findViewById(R.id.navigation_analyse);
        rbAnalyse.setChecked(true);

        rbHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
        rbMine.setOnClickListener(v -> {
            Intent intent = new Intent(this, MineActivity.class);
            startActivity(intent);
        });
    }

    void UpdateCalendar() {
        TextView tvCalendarDate = findViewById(R.id.analyse_calendar_date);   // 获取日历日期文本
        HomeActivity.currentYear = Calendar.getInstance().get(Calendar.YEAR);   // 当前年份
        HomeActivity.currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;   // 当前月份
        tvCalendarDate.setText(String.format("%d-%d", HomeActivity.currentYear, HomeActivity.currentMonth));   // 设置日历日期文本
    }
}
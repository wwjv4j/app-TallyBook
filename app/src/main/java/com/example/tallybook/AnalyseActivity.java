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


import com.example.tallybook.Entity.CategoryAmount;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.Entity.CalendarDay;
import com.example.tallybook.Adapter.SimpleRecordsAdapter;
import com.example.tallybook.Adapter.CalendarAdapter;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Database.RecordDatabase;
import com.example.tallybook.Entity.CategoryAmount;
import com.example.tallybook.HomeActivity;
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
    }

    // 初始化饼图
    private void InitPieChart() {
        // 获取饼图视图
        PieChart pieChartView = findViewById(R.id.analyse_pie_chart);
        List<CategoryAmount> categoryAmountList = billingRecordDao.getCategoryAmountByMonth(String.valueOf(HomeActivity.currentYear), String.valueOf(HomeActivity.currentMonth));
        ArrayList<PieEntry> entries = categoryAmountList.stream().map(CategoryAmount::toPieEntry).collect(Collectors.toCollection(ArrayList::new));
        // 设置饼图数据
        PieDataSet dataSet = new PieDataSet(entries, "类别");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        pieChartView.setData(data);
        pieChartView.invalidate();
    }
}
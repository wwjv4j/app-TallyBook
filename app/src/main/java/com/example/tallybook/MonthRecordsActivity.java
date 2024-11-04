package com.example.tallybook;

import java.util.List;
import java.util.Collections;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tallybook.Adapter.SimpleRecordsAdapter;
import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.MainApplication;
import com.example.tallybook.HomeActivity;

public class MonthRecordsActivity extends AppCompatActivity {
    private List<BillingRecord> monthBillingRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_month_records);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitMonthRecords();
        InitReturnButton();
    }
    // 初始化返回按钮
    private void InitReturnButton() {
        findViewById(R.id.month_records_return_button).setOnClickListener(v-> {
            finish();
        });
    }
    void InitMonthRecords() {
        monthBillingRecords = MainApplication.getInstance().getRecordDB().getBillingRecordDao().getMonthRecords(String.valueOf(HomeActivity.currentYear), String.valueOf(HomeActivity.currentMonth));
        Collections.reverse(monthBillingRecords);

        RecyclerView rvMonthRecords = findViewById(R.id.month_records_recycler_view);   // 获取循环视图
        // 创建线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);   // 创建线性布局管理器
        rvMonthRecords.setLayoutManager(layoutManager);   // 设置循环视图的布局管理器
        // 创建简单记录适配器
        SimpleRecordsAdapter simpleRecordsAdapter = new SimpleRecordsAdapter(this, monthBillingRecords);   // 创建简单记录适配器
        rvMonthRecords.setAdapter(simpleRecordsAdapter);   // 设置循环视图的适配器
    }
}
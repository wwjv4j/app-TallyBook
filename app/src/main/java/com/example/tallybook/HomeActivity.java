package com.example.tallybook;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.Adapter.SimpleRecordsAdapter;

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

        InitRecyclerLinear();   // 初始化线性布局的循环视图
    }

    // 初始化线性布局的循环视图
    private void InitRecyclerLinear() {
        RecyclerView rvSimpleRecords = findViewById(R.id.home_recycler_view);   // 获取循环视图

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);   // 创建线性布局管理器
        rvSimpleRecords.setLayoutManager(layoutManager);   // 设置循环视图的布局管理器

        SimpleRecordsAdapter adapter = new SimpleRecordsAdapter(this, BillingRecord.getSimpleRecords());   // 创建简单记录适配器
        rvSimpleRecords.setAdapter(adapter);   // 设置循环视图的适配器
    }
}
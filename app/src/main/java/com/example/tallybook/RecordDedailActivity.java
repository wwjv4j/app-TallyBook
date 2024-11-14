package com.example.tallybook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import com.example.tallybook.Entity.BillingRecord;

public class RecordDedailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_dedail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 根据home页面的消费记录初始化页面
        BillingRecord record = (BillingRecord)getIntent().getSerializableExtra("record");
        InitPage(record);
        InitReturnButton();
    }

    // 初始化返回按钮
    private void InitReturnButton() {
        findViewById(R.id.record_detail_return_button).setOnClickListener(v-> {
            finish();
        });
    }

    // 根据传入的消费记录初始化页面
    private void InitPage(BillingRecord record) {
        ListView lv = findViewById(R.id.record_detail_listview);

        // 创建数据源
        ArrayList<String> data = new ArrayList<>();

        data.add("消费金额：" + record.getAmount());
        data.add("消费类型：" + record.getCategory());
        data.add("消费备注：" + record.getRemark());
        data.add("消费日期：" + record.getYear() + "年" + record.getMonth() + "月" + record.getDay() + "日" + record.getTime());

        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        // 设置适配器
        lv.setAdapter(adapter);
    }
}
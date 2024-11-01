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

import org.w3c.dom.Text;

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
    }

    // 根据传入的消费记录初始化页面
    private void InitPage(BillingRecord record) {
        TextView tvAmount = findViewById(R.id.record_detail_amount);
        TextView tvCategory = findViewById(R.id.record_detail_category);
        TextView tvRemark = findViewById(R.id.record_detail_remark);
        TextView tvTime = findViewById(R.id.record_detail_date);

        tvAmount.setText(String.valueOf(record.getAmount()));
        tvCategory.setText(record.getCategory());
        tvRemark.setText(record.getRemark());
        tvTime.setText(record.getYear() + "年" + record.getMonth() + "月" + record.getDay() + "日 " + record.getTime());
    }
}
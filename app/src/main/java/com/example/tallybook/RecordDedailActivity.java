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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Dao.BillingRecordDao;

public class RecordDedailActivity extends AppCompatActivity {
    BillingRecordDao billingRecordDao = MainApplication.getInstance().getRecordDB().getBillingRecordDao();
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
        data.add("" + record.getAmount());
        data.add("" + record.getCategory());
        data.add("" + record.getRemark());
        data.add("" + record.getYear() + "年" + record.getMonth() + "月" + record.getDay() + "日" + record.getTime());
    
        // 创建自定义适配器，并传入 record
        RecordAdapter adapter = new RecordAdapter(this, data, record);
    
        // 设置适配器
        lv.setAdapter(adapter);
        
    }
    
    private class RecordAdapter extends ArrayAdapter<String> {
        private ArrayList<String> data;
        private BillingRecord record;
    
        public RecordAdapter(Context context, ArrayList<String> data, BillingRecord record) {
            super(context, R.layout.item_record_detail, data);
            this.data = data;
            this.record = record;
        }
    
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 判断是否需要重用视图
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_record_detail, parent, false);
            }
    
            EditText editText = convertView.findViewById(R.id.editTextRecordDetail);
            TextView label = convertView.findViewById(R.id.labelRecordDetail);
            editText.setText(data.get(position));  // 设置现有的内容
    
            // 根据不同的字段动态设置标签
            switch (position) {
                case 0:
                    label.setText("消费金额：");
                    break;
                case 1:
                    label.setText("消费类型：");
                    break;
                case 2:
                    label.setText("消费备注：");
                    break;
                case 3:
                    label.setText("消费日期：");
                    break;
            }
    
            // 设置焦点变化监听器，保存修改后的数据`
            editText.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    String newText = editText.getText().toString();
                    data.set(position, newText); // 更新数据源
    
                    // 更新 BillingRecord 对象的对应字段
                    switch (position) {
                        case 0:
                            record.setAmount(Double.parseDouble(newText));
                            break;
                        case 1:
                            record.setCategory(newText);
                            break;
                        case 2:
                            record.setRemark(newText);
                            break;
                        case 3:
                            record.setYear(newText.substring(0, 4));
                            record.setMonth(newText.substring(5, 7));
                            record.setDay(newText.substring(8, 10));
                            record.setTime(newText.substring(11)); // 假设日期和时间是这样格式的
                            break;
                    }
                    billingRecordDao.deleteRecordById(record.getId());
                    billingRecordDao.insertRecord(record);
                    notifyDataSetChanged(); // 更新适配器
                }
            });
    
            return convertView;
        }
    }
    
}
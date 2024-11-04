package com.example.tallybook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.flexbox.FlexboxLayout;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.Entity.BillingRecord;
import android.widget.Toast;
import android.widget.EditText;
import java.util.Calendar;

public class AddRecordActivity extends AppCompatActivity {
    private int lastSelected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitCategory();
        InitConfirmButton();
        InitReturnButton();
    }

    // 初始化返回按钮
    private void InitReturnButton() {
        findViewById(R.id.add_record_return_button).setOnClickListener(v-> {
            finish();
        });
    }
    // 初始化类别按钮
    private void InitCategory() {
        FlexboxLayout flexboxLayout = findViewById(R.id.add_record_category);
        // 设置类别中所有按钮的点击事件,使得只能有一个被选中
        for(int i = 0; i < flexboxLayout.getChildCount(); i++) {
            final int index = i;
            TextView tvButton = (TextView)flexboxLayout.getChildAt(i);
            tvButton.setOnClickListener(v-> {
                if(tvButton.isSelected()) {
                    tvButton.setSelected(false);
                } else {
                    tvButton.setSelected(true);
                    if(lastSelected != -1) {
                        TextView lastButton = (TextView)flexboxLayout.getChildAt(lastSelected);
                        lastButton.setSelected(false);
                    }
                    lastSelected = index;
                }
            });
        }
    }

    // 初始化确认按钮
    private void InitConfirmButton() {
        findViewById(R.id.add_record_confirm).setOnClickListener(v-> {
            // 获取输入的金额并判断是否为空
            String amountString = ((EditText)findViewById(R.id.add_record_amount)).getText().toString();
            if(amountString.isEmpty()) {
                Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
                return;
            }
            if(lastSelected == -1) {
                Toast.makeText(this, "请选择类别", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(amountString);
            FlexboxLayout flexboxLayout = findViewById(R.id.add_record_category);
            String category = ((TextView) flexboxLayout.getChildAt(lastSelected)).getText().toString();
            String remark = ((EditText)findViewById(R.id.add_record_remark)).getText().toString();
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));
            String second = String.valueOf(calendar.get(Calendar.SECOND));
            String time = hour + ":" + minute + ":" + second;
            BillingRecordDao billingRecordDao = MainApplication.getInstance().getRecordDB().getBillingRecordDao();
            // 获取最大id并加1
            int id = billingRecordDao.getMaxId() + 1;
            BillingRecord billingRecord = new BillingRecord(id, amount, category, year, month, day, time, remark);
            // 插入账单记录
            billingRecordDao.insertRecord(billingRecord);
            finish();
        });
    }

}


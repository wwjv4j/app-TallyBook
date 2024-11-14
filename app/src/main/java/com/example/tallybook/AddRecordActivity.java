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
import android.widget.NumberPicker;
import android.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;


public class AddRecordActivity extends AppCompatActivity {
    private int lastSelected = -1;
    private int year, month, day;
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

        InitDatePicker();
        InitCategory();
        InitConfirmButton();
        InitReturnButton();

        // 限制输入金额的范围
        EditText etAmount = findViewById(R.id.add_record_amount);
        etAmount.setFilters(new InputFilter[]{new InputFilterMinMax(0,99999)});
        
    }
    // 初始化日期选择器
    private void InitDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        TextView tvDate = findViewById(R.id.add_record_date);
        tvDate.setText(year + "年" + month + "月" + day + "日");

        findViewById(R.id.add_record_date).setOnClickListener(v-> {
            // 创建自定义对话框布局
            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogView = inflater.inflate(R.layout.dialog_day_picker, null);

            final NumberPicker npYear = dialogView.findViewById(R.id.np_year);
            final NumberPicker npMonth = dialogView.findViewById(R.id.np_month);
            final NumberPicker npDay = dialogView.findViewById(R.id.np_day);
            // 设置年份选择器的范围
            npYear.setMinValue(2000);
            npYear.setMaxValue(2100);
            npYear.setValue(year);

            // 设置月份选择器的范围
            npMonth.setMinValue(1);
            npMonth.setMaxValue(12);
            npMonth.setValue(month);

            // 设置日期选择器的范围
            npDay.setMinValue(1);
            npDay.setMaxValue(31);
            npDay.setValue(day);

            // 创建并显示对话框
            new AlertDialog.Builder(this)
                .setTitle("选择日期")
                .setView(dialogView)
                .setPositiveButton("确定", (dialog, which) -> {
                    year = npYear.getValue();
                    month = npMonth.getValue();
                    day = npDay.getValue();
                    tvDate.setText(year + "年" + month + "月" + day + "日");
                })
                .setNegativeButton("取消", null)
                .show();
            });
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
            String year1 = String.valueOf(year);
            String month1 = String.valueOf(month);
            String day1 = String.valueOf(day);
            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));
            String second = String.valueOf(calendar.get(Calendar.SECOND));
            if(year != Calendar.getInstance().get(Calendar.YEAR) || month != Calendar.getInstance().get(Calendar.MONTH) + 1 || day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                hour = "00";
                minute = "00";
                second = "00";
            }
            String time = hour + ":" + minute + ":" + second;
            BillingRecordDao billingRecordDao = MainApplication.getInstance().getRecordDB().getBillingRecordDao();
            // 获取最大id并加1
            int id = billingRecordDao.getMaxId() + 1;
            BillingRecord billingRecord = new BillingRecord(id, amount, category, year1, month1, day1, time, remark);
            // 插入账单记录
            billingRecordDao.insertRecord(billingRecord);
            finish();
        });
    }

    public class InputFilterMinMax implements InputFilter {

        private double min, max;
    
        public InputFilterMinMax(double min, double max) {
            this.min = min;
            this.max = max;
        }
    
        public InputFilterMinMax(String min, String max) {
            this.min = Double.parseDouble(min);
            this.max = Double.parseDouble(max);
        }
    
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String inputString = dest.toString() + source.toString();
                double input = Double.parseDouble(inputString);
    
                // 检查是否在范围内
                if (!isInRange(min, max, input)) {
                    return "";
                }
    
                // 检查小数位数
                if (inputString.contains(".")) {
                    int decimalIndex = inputString.indexOf(".");
                    int decimalDigits = inputString.length() - decimalIndex - 1;
                    if (decimalDigits > 2) {
                        return "";
                    }
                }
    
                return null;
            } catch (NumberFormatException nfe) {
                return "";
            }
        }
    
        private boolean isInRange(double a, double b, double c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

}


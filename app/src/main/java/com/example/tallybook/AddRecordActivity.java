package com.example.tallybook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.WindowInsetsCompat;
import android.view.ViewGroup;
import com.google.android.flexbox.FlexboxLayout;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.FileHelper.CategoryFile;
import android.widget.Toast;
import android.widget.EditText;
import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Text;

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
        InitAddCategoryButton();

        // 限制输入金额的范围
        EditText etAmount = findViewById(R.id.add_record_amount);
        etAmount.setFilters(new InputFilter[]{new InputFilterMinMax(0,99999)});
        
    }
    @Override
    public void onPause() {
        super.onPause();
        // 保存消费类别列表到文件
        CategoryFile.saveArrayToFile(this, "category", CategoryFile.categoryList);
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

    // 初始化添加类别按钮
    private void InitAddCategoryButton() {
        TextView tvAddCategory = findViewById(R.id.add_record_add_category);
        tvAddCategory.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("添加类别");

            // 设置输入框
            final EditText input = new EditText(this);
            input.setHint("输入类别名称");
            builder.setView(input);

            // 设置按钮
            builder.setPositiveButton("确定", (dialog, which) -> {
                String category = input.getText().toString().trim();
                if (!category.isEmpty()) {
                    CategoryFile.addCategory(category);
                    CategoryFile.saveArrayToFile(this, "category", CategoryFile.categoryList);
                    InitCategory();
                } else {
                    Toast.makeText(this, "类别名称不能为空", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }
    // 初始化类别按钮
    private void InitCategory() {
        List<String> categoryList = CategoryFile.readArrayFromFile(this, "category");

        FlexboxLayout flexboxLayout = findViewById(R.id.add_record_category);
        flexboxLayout.removeAllViews();
        // 动态添加类别按钮
        for(String category : categoryList) {
            TextView tvButton = (TextView) getLayoutInflater().inflate(R.layout.item_category, null);
            tvButton.setText(category);

            // 设置布局参数
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16); // 设置外边距，可以根据需要调整

            tvButton.setLayoutParams(layoutParams);
            flexboxLayout.addView(tvButton);
        }
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
            //长按
            tvButton.setOnLongClickListener(v->{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择操作")
                .setItems(new String[]{"删除"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // 删除操作
                            Toast.makeText(this, "已删除"+CategoryFile.categoryList.get(index), Toast.LENGTH_SHORT).show();
                            CategoryFile.categoryList.remove(CategoryFile.categoryList.get(index));
                            CategoryFile.saveArrayToFile(this, "category", CategoryFile.categoryList);
                            InitCategory();
                            break;
                    }
                });
                builder.create().show();
                return true;
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


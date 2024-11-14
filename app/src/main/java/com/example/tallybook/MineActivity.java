package com.example.tallybook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import com.example.tallybook.MainApplication;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.HomeActivity;
import java.util.Calendar;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.widget.NumberPicker;
import android.app.AlertDialog;


import java.util.ArrayList;
import android.view.View;


public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mine);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        InitNavigation();
        InitListView();
    }

    // 初始化ListView
    private void InitListView() {
        // 获取 ListView
        ListView listView = findViewById(R.id.mine_list);

        // 创建数据源
        ArrayList<String> data = new ArrayList<>();
        data.add("清空全部记录");
        data.add("清空本年记录");
        data.add("清空本月记录");
        data.add("点击选择清空月份");

        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        // 设置适配器
        listView.setAdapter(adapter);

        // 设置点击事件
        BillingRecordDao billingRecordDao = MainApplication.getInstance().getRecordDB().getBillingRecordDao();   // 声明一个账单记录数据访问对象
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 获取点击的数据
            String item = data.get(position);
            // 根据点击的数据进行操作
            switch (item) {
                case "清空全部记录":
                    // 清空全部记录
                    billingRecordDao.deleteAllRecords();
                    Toast.makeText(this, "清空全部记录成功", Toast.LENGTH_SHORT).show();
                    break;
                case "清空本年记录":
                    // 清空本年记录
                    billingRecordDao.deleteRecordsByYear(String.valueOf(HomeActivity.currentYear));
                    Toast.makeText(this, "清空本年记录成功", Toast.LENGTH_SHORT).show();
                    break;
                case "清空本月记录":
                    // 清空本月记录
                    billingRecordDao.deleteRecordsByMonth(String.valueOf(HomeActivity.currentYear), String.valueOf(HomeActivity.currentMonth));
                    Toast.makeText(this, "清空本月记录成功", Toast.LENGTH_SHORT).show();
                    break;
                case "点击选择清空月份":
                    // 点击旋转清空月份
                    showMonthPickerDialog();
                    break;
            }
        });
        
    }
    // 初始化导航
    private void InitNavigation() {
        RadioButton rbHome = findViewById(R.id.navigation_home);   // 获取首页按钮
        RadioButton rbAnalyse = findViewById(R.id.navigation_analyse);   // 获取分析按钮
        RadioButton rbMine = findViewById(R.id.navigation_mine);   // 获取我的按钮
        rbMine.setChecked(true);   // 设置首页按钮为选中状态
        
        rbAnalyse.setOnClickListener(v -> {
            // 跳转到分析页面
            Intent intent = new Intent(this, AnalyseActivity.class);
            startActivity(intent);
        });
        rbHome.setOnClickListener(v -> {
            // 跳转到我的页面
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
    }

    // 显示月份选择对话框
    private void showMonthPickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
    
        BillingRecordDao billingRecordDao = MainApplication.getInstance().getRecordDB().getBillingRecordDao(); // 声明一个账单记录数据访问对象
    
        // 创建自定义对话框布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_month_picker, null);
    
        final NumberPicker npYear = dialogView.findViewById(R.id.np_year);
        final NumberPicker npMonth = dialogView.findViewById(R.id.np_month);
    
        // 设置年份选择器的范围
        npYear.setMinValue(2000);
        npYear.setMaxValue(2100);
        npYear.setValue(currentYear);
    
        // 设置月份选择器的范围
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setValue(currentMonth + 1);
    
        // 创建并显示对话框
        new AlertDialog.Builder(this)
            .setTitle("选择月份")
            .setView(dialogView)
            .setPositiveButton("确定", (dialog, which) -> {
                int selectedYear = npYear.getValue();
                int selectedMonth = npMonth.getValue();
                // 处理选择的月份和年份
                billingRecordDao.deleteRecordsByMonth(String.valueOf(selectedYear), String.valueOf(selectedMonth));
                Toast.makeText(this, "清空 " + selectedYear + " 年 " + selectedMonth + " 月记录成功", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }
}
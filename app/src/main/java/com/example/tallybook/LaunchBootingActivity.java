package com.example.tallybook;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tallybook.LaunchBooting.Adapter;

public class LaunchBootingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_booting);
        InitLaunchBooting();
    }

    // 初始化启动引导
    private void InitLaunchBooting() {
        ViewPager vpLaunch = findViewById(R.id.launch_booting_view_pager); // 获取单选按钮组
        int[] imageList = new int[4]; // 创建一个图片资源列表
        // 往图片资源列表中添加图片资源
        for(int i = 0; i < 4; i++) {
            imageList[i] = getResources().getIdentifier("image_booting" + (i+1), "drawable", getPackageName());
        }

        Adapter adapter = new Adapter(getSupportFragmentManager(), imageList); // 创建一个启动引导适配器
        vpLaunch.setAdapter(adapter); // 设置单选按钮组的适配器
    }
}

package com.example.tallybook.LaunchBooting;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tallybook.LaunchBooting.LaunchFragment;

// 适配器类
public class Adapter extends FragmentPagerAdapter{
    private int[] mImageResId;  //图片资源ID
    // 构造函数,传入碎片管理器和图片资源ID数组
    public Adapter(FragmentManager fm, int[] imageArray) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mImageResId = imageArray;
    }
    // 获取碎片数量
    public int getCount() {
        return mImageResId.length;
    }
    // 获取指定位置的碎片
    public Fragment getItem(int position) {
        return LaunchFragment.newInstance(position, mImageResId[position]);
    }dasfasfaf
}
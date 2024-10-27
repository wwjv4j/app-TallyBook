package com.example.tallybook.LaunchBooting;

import android.content.Intent;
import android.view.View;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import com.example.tallybook.R;

import com.example.tallybook.LaunchBooting.Adapter;
import com.example.tallybook.MainActivity;
/**
 *
 * LaunchFragment 类继承自 Fragment，用于启动应用时的界面展示。
 * 
 * 该类包含以下成员变量：
 *   mView：一个视图对象，用于表示该 Fragment 的视图。
 *   mContext：一个上下文对象，用于获取应用的上下文。
 */
public class LaunchFragment extends Fragment{
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private int mPosition; // 位置序号
    private int mImageId; // 图片的资源编号
    private int mCount = 4; // 引导页的数量

    // 获取该碎片的一个实例
    public static LaunchFragment newInstance(int position, int imageId, Adapter adapter) {
        LaunchFragment fragment = new LaunchFragment(); // 创建该碎片的一个实例
        Bundle bundle = new Bundle();
        bundle.putInt("position", position); // 往包裹存入位置序号
        bundle.putInt("imageId", imageId); // 往包裹存入图片的资源编号
        fragment.setArguments(bundle); // 把包裹塞给碎片
        return fragment; // 返回碎片实例
    }

    // 创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面上下文
        if(getArguments() != null) {  // 如果碎片携带有包裹，就打开包裹获取参数信息
            mPosition = getArguments().getInt("position", 0); // 从包裹获取位置序号
            mImageId = getArguments().getInt("imageId", 0); // 从包裹获取图片的资源编号
        }
        // 根据布局文件 launch_booting.xml 生成视图对象
        mView = inflater.inflate(R.layout.activity_launch_booting, container, false);
        ImageView ivImage = mView.findViewById(R.id.launch_booting_image); // 获取图片视图
        RadioGroup rgIndicator = mView.findViewById(R.id.launch_booting_radio_group); // 获取单选按钮组
        Button btn = mView.findViewById(R.id.launch_booting_button); // 获取按钮
        ivImage.setImageResource(mImageId); // 设置图片视图的图片资源
        // 设置每个引导页的按钮点击事件
        for(int i = 0; i < mCount; i++) { // 遍历引导页的数量
            RadioButton rb = new RadioButton(mContext); // 创建一个单选按钮
            rb.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)); // 设置单选按钮的布局参数
            rb.setButtonDrawable(R.drawable.launch_booting_icon_selector); // 设置单选按钮的图表
            rb.setPadding(10, 10, 10, 10); // 设置单选按钮的内边距
            rb.setClickable(false); // 设置单选按钮不可点击
            rgIndicator.addView(rb); // 把单选按钮添加到单选按钮组
        }
        // 设置高亮显示当前引导页的单选按钮
        ((RadioButton)rgIndicator.getChildAt(mPosition)).setChecked(true);
        // 设置按钮的点击事件
        if(mPosition == mCount - 1) { // 如果当前引导页是最后一页
            btn.setVisibility(View.VISIBLE); // 显示按钮
            btn.setOnClickListener(new View.OnClickListener() { // 设置按钮的点击事件监听器
                public void onClick(View v) {
                    // 跳转到主页面
                    Intent intent = new Intent(mContext, MainActivity.class); // 创建一个意图
                    mContext.startActivity(intent); // 启动指定意图
                    getActivity().finish(); // 结束当前活动页面
                }
            });
        }

        return mView; // 返回该碎片的视图对象
    }
}

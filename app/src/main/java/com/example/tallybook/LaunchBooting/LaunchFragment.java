package com.example.tallybook.LaunchBooting;

import android.view.View;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

/**
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
    public static LaunchFragment newInstance(int position, int imageId) {
        LaunchFragment fragment = new LaunchFragment(); // 创建该碎片的一个实例
        Bundle bundle = new Bundle();
        bundle.pushInt("position", position); // 往包裹存入位置序号
        bundle.pushInt("imageId", imageId); // 往包裹存入图片的资源编号
        fragment.setArguments(bundle); // 把包裹塞给碎片
        return fragment; // 返回碎片实例
    }

    // 创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewFroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面上下文
        if(getArguments() != null) {  // 如果碎片携带有包裹，就打开包裹获取参数信息
            mPosition = getArguments().getInt("position", 0); // 从包裹获取位置序号
            mImageId = getArguments().getInt("imageId", 0); // 从包裹获取图片的资源编号
        }
        // 根据布局文件 launch_booting.xml 生成视图对象
        mView = inflater.inflate(R.layout.launch_booting, container, false);
    }
}

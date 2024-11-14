package com.example.tallybook;

import android.app.Application;
import androidx.room.Room;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import android.content.Context;

import com.example.tallybook.Database.RecordDatabase;
import com.example.tallybook.Entity.BillingRecord;
import com.example.tallybook.FileHelper.CategoryFile;

public class MainApplication extends Application {
    private static MainApplication mApp; // 声明一个当前应用的静态实例
    private RecordDatabase recordDatabase; // 声明一个记录数据库对象
    // 利用单例模式获取当前应用的唯一实例
    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this; // 在打开应用时对静态的应用实例赋值
        // 构建记录数据库的实例
        recordDatabase = Room.databaseBuilder(mApp, RecordDatabase.class, "Records")
                            .addMigrations().allowMainThreadQueries().build();
        // 从文件中读取消费类别列表
        CategoryFile.categoryList = CategoryFile.readArrayFromFile(this, "category");
        if(CategoryFile.categoryList == null) {
            CategoryFile.categoryList = new java.util.ArrayList<>();
            CategoryFile.categoryList.add("餐饮");
            CategoryFile.categoryList.add("购物");
            CategoryFile.categoryList.add("交通");
            CategoryFile.categoryList.add("日用");
            CategoryFile.categoryList.add("娱乐");
            CategoryFile.categoryList.add("通讯");
            CategoryFile.categoryList.add("服饰");
            CategoryFile.categoryList.add("美容");
            CategoryFile.categoryList.add("住房");
            CategoryFile.categoryList.add("医疗");
            CategoryFile.categoryList.add("教育");
            CategoryFile.categoryList.add("旅行");
            CategoryFile.categoryList.add("人情");
            CategoryFile.categoryList.add("其他");
        }
    }

    // 获取记录数据库的实例
    public RecordDatabase getRecordDB() {
        return recordDatabase;
    }

}
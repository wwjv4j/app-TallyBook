package com.example.tallybook;

import android.app.Application;
import androidx.room.Room;

import com.example.tallybook.Database.RecordDatabase;
import com.example.tallybook.Entity.BillingRecord;

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

    }

    // 获取记录数据库的实例
    public RecordDatabase getRecordDB() {
        return recordDatabase;
    }
}
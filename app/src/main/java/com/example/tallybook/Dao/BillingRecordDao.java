package com.example.tallybook.Dao;

import androidx.room.Delete;
import androidx.room.Insert;    
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Dao;
import java.util.List;

import com.example.tallybook.Entity.BillingRecord;
@Dao
public interface BillingRecordDao {
    // 获取所有账单记录
    @Query("SELECT * FROM BillingRecords")
    List<BillingRecord> getAllRecords();

    // 获取指定年份的账单记录
    @Query("SELECT * FROM BillingRecords WHERE year = :year")
    List<BillingRecord> getRecordsByYear(String year);

    // 获取指定年份、月份的账单记录
    @Query("SELECT * FROM BillingRecords WHERE year = :year AND month = :month")
    List<BillingRecord> getRecordsByMonth(String year, String month);

    // 获取指定年份、月份、日期的账单记录
    @Query("SELECT * FROM BillingRecords WHERE year = :year AND month = :month AND day = :day")
    List<BillingRecord> getRecordsByDay(String year, String month, String day);

    // 获取指定年份、月份、类别的账单记录
    @Query("SELECT * FROM BillingRecords WHERE year = :year AND month = :month AND category = :category")
    List<BillingRecord> getRecordsByCategory(String year, String month, String category);

    // 获取指定年份、月份、类别、日期的账单记录 
    @Query("SELECT * FROM BillingRecords WHERE year = :year AND month = :month AND category = :category AND day = :day")
    List<BillingRecord> getRecordsByCategoryAndDay(String year, String month, String category, String day);

    // 插入一条账单记录
    @Insert
    void insertRecord(BillingRecord record);

    // 删除一条账单记录
    @Delete
    void deleteRecord(BillingRecord record);

    // 根据id删除一条账单记录
    @Query("DELETE FROM BillingRecords WHERE id = :id")
    void deleteRecordById(int id);

    // 删除指定年份的账单记录
    @Query("DELETE FROM BillingRecords WHERE year = :year")
    void deleteRecordsByYear(String year);

    // 删除指定年份、月份的账单记录
    @Query("DELETE FROM BillingRecords WHERE year = :year AND month = :month")
    void deleteRecordsByMonth(String year, String month);

    // 删除所有账单记录
    @Query("DELETE FROM BillingRecords")
    void deleteAllRecords();

    // 根据id更新一条账单记录
    @Update
    void updateRecord(BillingRecord record);

    // 获取最大id
    @Query("SELECT MAX(id) FROM BillingRecords")
    int getMaxId();

}
package com.example.tallybook.Database;

import androidx.room.RoomDatabase;
import androidx.room.Database;
import com.example.tallybook.Dao.BillingRecordDao;
import com.example.tallybook.Entity.BillingRecord;

@Database(entities = {BillingRecord.class}, version = 1, exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase{
    public abstract BillingRecordDao getBillingRecordDao();
}

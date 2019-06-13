package com.example.sqliteapplication.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建一个单例类DatabaseManager，
 * 它将保存并返回一个SQLiteOpenHelper对象。
 * Created by xwxwaa on 2019/6/13.
 */

public class DatabaseManager {
    /**
     * AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。
     * AtomicInteger是在使用非阻塞算法实现并发控制，在一些高并发程序中非常适合
     */
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        int incrementAndGet = mOpenCounter.incrementAndGet();
        Log.e("TAG","openDatabase->"+incrementAndGet);
        if( incrementAndGet == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        int decrementAndGet = mOpenCounter.decrementAndGet();
        Log.e("TAG","closeDatabase->"+decrementAndGet);
        if(decrementAndGet == 0) {
            // Closing database
            mDatabase.close();
        }
    }

}

package com.example.thirtyseven.myschedule;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ThirtySeven on 29.05.2017.
 */

public class Schedule extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public Schedule(Context context) {
        // конструктор суперкласса
        super(context, "schedule", null, 1);
    }

    public Schedule(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Schedule(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table schedule ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "time integer,"
                + "weekday text,"
                + "audience text,"
                + "group integer,"
                + "oddOrEvenOrNot integer,"
                + "teacher text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

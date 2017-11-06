package ru.mail.park.tpschedule.transport.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lieroz
 * 06.11.17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper INSTANCE;

    // Database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tp_schedule";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

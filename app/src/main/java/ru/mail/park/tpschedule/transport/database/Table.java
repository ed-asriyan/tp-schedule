package ru.mail.park.tpschedule.transport.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lieroz
 * 06.11.17.
 */

public interface Table {
    boolean createTable(SQLiteDatabase db);
    boolean dropTable(SQLiteDatabase db);
}

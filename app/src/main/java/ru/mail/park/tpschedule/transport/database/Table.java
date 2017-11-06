package ru.mail.park.tpschedule.transport.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lieroz
 * 06.11.17.
 */

@SuppressWarnings({"unused", "WeakerAccess"})

public interface Table {
    boolean createTable(SQLiteDatabase db);
    boolean dropTable(SQLiteDatabase db);
}

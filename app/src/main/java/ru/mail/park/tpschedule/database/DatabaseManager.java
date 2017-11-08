package ru.mail.park.tpschedule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by ed on 07.11.17
 */

@SuppressWarnings("unused")
public class DatabaseManager extends SQLiteOpenHelper {
    private static final String TAG = DatabaseManager.class.getSimpleName();

    // Database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tp_schedule";

    // Database entities
    private final Timetable timetable = new Timetable();

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        timetable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            timetable.dropTable(db);
            onCreate(db);
        }
    }

    public void clearTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        timetable.clearTable(db);
        db.endTransaction();
    }

    public void dropTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        timetable.dropTable(db);
        db.endTransaction();
    }

    public boolean updateSchedule(List<TimetableModel> schedule) {
        SQLiteDatabase db = getWritableDatabase();
        timetable.clearTable(db);
        return timetable.addEntries(db, schedule);
    }

    public boolean addTimetableEntries(List<TimetableModel> schedule) {
        SQLiteDatabase db = getWritableDatabase();
        return timetable.addEntries(db, schedule);
    }

    public List<TimetableModel> getTimetableEntries(List<String> filters) {
        SQLiteDatabase db = getReadableDatabase();
        return timetable.getEntries(db, filters);
    }

    public int getTimetableEntriesCount() {
        SQLiteDatabase db = getReadableDatabase();
        return timetable.countTableEntries(db);
    }
}

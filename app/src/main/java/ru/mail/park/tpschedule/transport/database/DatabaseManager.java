package ru.mail.park.tpschedule.transport.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by ed on 07.11.17
 */


@SuppressWarnings({"unused"})
public class DatabaseManager extends SQLiteOpenHelper {
    private static final String TAG = DatabaseManager.class.getSimpleName();

    private static DatabaseManager INSTANCE;

    // Database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tp_schedule";

    // Database entities
    private final Timetable timetable = new Timetable();

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManager(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!timetable.createTable(db)) {
            // TODO do something here...
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            timetable.dropTable(db);
            onCreate(db);
        }
    }

    public boolean addTimetableEntries(List<TimetableModel> objects) {
        SQLiteDatabase db = getWritableDatabase();
        return timetable.addEntries(db, objects);
    }

    public Map<String, List<TimetableModel>> getTimetableEntries(List<String> filters, String start, String end) {
        SQLiteDatabase db = getReadableDatabase();
        return timetable.getEntries(db, filters, start, end);
    }
}

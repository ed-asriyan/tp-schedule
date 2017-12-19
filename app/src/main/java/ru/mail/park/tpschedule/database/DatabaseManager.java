package ru.mail.park.tpschedule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    // Multithreaded communication with database
    private final static int THREAD_COUNT = 3;
    private final ExecutorService databaseExecutor = Executors.newFixedThreadPool(THREAD_COUNT);

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
        databaseExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                db.beginTransaction();
                timetable.clearTable(db);
                db.endTransaction();
            }
        });
    }

    public void dropTables() {
        databaseExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                db.beginTransaction();
                timetable.dropTable(db);
                db.endTransaction();
            }
        });
    }

    public void updateSchedule(final List<TimetableModel> schedule) {
        databaseExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                timetable.clearTable(db);
                timetable.addEntries(db, schedule);
            }
        });
    }

    public void addTimetableEntries(final List<TimetableModel> schedule) {
        databaseExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                timetable.addEntries(db, schedule);
            }
        });
    }

    public List<TimetableModel> getTimetableEntries(final List<String> filters) {
        Future<List<TimetableModel>> result = databaseExecutor.submit(new Callable<List<TimetableModel>>() {
            @Override
            public List<TimetableModel> call() throws Exception {
                SQLiteDatabase db = getReadableDatabase();
                return timetable.getEntries(db, filters);
            }
        });

        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getTimetableEntriesCount() {
        Future<Integer> result = databaseExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                SQLiteDatabase db = getReadableDatabase();
                return timetable.countTableEntries(db);
            }
        });

        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

package ru.mail.park.tpschedule.transport.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Locale;

/**
 * Created by lieroz
 * 06.11.17.
 */

public class Timetable implements Table {
    private static final String TAG = Timetable.class.getSimpleName();

    // Table name
    private static final String TABLE_TIMETABLE = "timetable";

    // Timetable table columns
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LESSON_TITLE = "lesson_title";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_LESSON_TOPIC = "lesson_topic";
    private static final String KEY_AUDITORIUM_NUMBER = "auditorium_number";
    private static final String KEY_SCHEDULE_DATE = "schedule_date";
    private static final String KEY_SUBGROUPS = "subgroups";
    private static final String KEY_TYPE_TITLE = "type_title";
    private static final String KEY_LESSON_TUTORS = "lesson_tutors";

    public boolean createTable(SQLiteDatabase db) {
        String CREATE_TIMETABLE_TABLE = "CREATE TABLE " + TABLE_TIMETABLE +
                "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_TITLE + " TEXT DEFAULT NULL," +
                    KEY_LESSON_TITLE + " TEXT DEFAULT NULL," +
                    KEY_START_TIME + " TEXT DEFAULT NULL," +
                    KEY_END_TIME + " TEXT DEFAULT NULL," +
                    KEY_LESSON_TOPIC + " TEXT DEFAULT NULL," +
                    KEY_AUDITORIUM_NUMBER + " TEXT DEFAULT NULL," +
                    KEY_SCHEDULE_DATE + " TEXT DEFAULT NULL" +
                    KEY_SUBGROUPS + " TEXT DEFAULT NULL" +
                    KEY_TYPE_TITLE + " TEXT DEFAULT NULL" +
                    KEY_LESSON_TUTORS + " TEXT DEFAULT NULL" +
                ")";
        try {
            db.execSQL(CREATE_TIMETABLE_TABLE);
        } catch (SQLException e) {
            String errorStringFormat = "error: %s, line: %d\n";
            Log.d(TAG, String.format(Locale.ENGLISH, errorStringFormat, e.toString(), e.getStackTrace()[2].getLineNumber()));
            return false;
        }
        return true;
    }

    public boolean dropTable(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE " + TABLE_TIMETABLE);
        } catch (SQLException e) {
            String errorStringFormat = "error: %s, line: %d\n";
            Log.d(TAG, String.format(Locale.ENGLISH, errorStringFormat, e.toString(), e.getStackTrace()[2].getLineNumber()));
            return false;
        }
        return true;
    }
}

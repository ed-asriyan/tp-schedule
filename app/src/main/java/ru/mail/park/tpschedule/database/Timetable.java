package ru.mail.park.tpschedule.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.mail.park.tpschedule.utils.ErrorMessage;

/**
 * Created by lieroz
 * 06.11.17.
 */

class Timetable implements DatabaseTable {
    private static final String TAG = Timetable.class.getSimpleName();

    // DatabaseTable name
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

    boolean createTable(SQLiteDatabase db) {
        String CREATE_TIMETABLE_TABLE = "CREATE TABLE " + TABLE_TIMETABLE +
                "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_TITLE + " TEXT DEFAULT NULL," +
                    KEY_LESSON_TITLE + " TEXT DEFAULT NULL," +
                    KEY_START_TIME + " TEXT DEFAULT NULL," +
                    KEY_END_TIME + " TEXT DEFAULT NULL," +
                    KEY_LESSON_TOPIC + " TEXT DEFAULT NULL," +
                    KEY_AUDITORIUM_NUMBER + " TEXT DEFAULT NULL," +
                    KEY_SCHEDULE_DATE + " TEXT DEFAULT NULL," +
                    KEY_SUBGROUPS + " TEXT DEFAULT NULL," +
                    KEY_TYPE_TITLE + " TEXT DEFAULT NULL," +
                    KEY_LESSON_TUTORS + " TEXT DEFAULT NULL" +
                ")";
        try {
            db.execSQL(CREATE_TIMETABLE_TABLE);
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        }
        return true;
    }

    boolean dropTable(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE " + TABLE_TIMETABLE);
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        }
        return true;
    }

    boolean addEntries(SQLiteDatabase db, List<TimetableModel> objects) {
        if (objects == null || objects.isEmpty()) {
            return false;
        }
        db.beginTransaction();
        try {
            StringBuilder INSERT_SQL = new StringBuilder(
                    String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES",
                            TABLE_TIMETABLE,
                            KEY_TITLE,
                            KEY_LESSON_TITLE,
                            KEY_START_TIME,
                            KEY_END_TIME,
                            KEY_LESSON_TOPIC,
                            KEY_AUDITORIUM_NUMBER,
                            KEY_SCHEDULE_DATE,
                            KEY_SUBGROUPS,
                            KEY_TYPE_TITLE,
                            KEY_LESSON_TUTORS
                    )
            );
            for (TimetableModel object : objects) {
                INSERT_SQL.append(" (")
                        .append(object.getTitle()).append(", ")
                        .append(object.getLessonTitle()).append(", ")
                        .append(object.getStartTime()).append(", ")
                        .append(object.getEndTime()).append(", ")
                        .append(object.getLessonTopic()).append(", ")
                        .append(object.getAuditoriumNumber()).append(", ")
                        .append(object.getScheduleDate()).append(", ")
                        .append(Joiner.on(",").join(object.getSubgroups())).append(", ")
                        .append(object.getTypeTitle()).append(", ")
                        .append(Joiner.on(",").join(object.getLessonTutors()))
                        .append("), ");
            }
            INSERT_SQL.setLength(INSERT_SQL.length() - 2);
            db.execSQL(INSERT_SQL.toString());
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        } finally {
            db.endTransaction();
        }
        return true;
    }

    Map<String, List<TimetableModel>> getEntries(SQLiteDatabase db, List<String> filters, String start, String end) {
        Map<String, List<TimetableModel>> map = new TreeMap<>();
        String SELECT_SQL = String.format("SELECT * FROM %s", TABLE_TIMETABLE);
        Cursor cursor = db.rawQuery(SELECT_SQL, null);
        TimetableModel timetableModel = new TimetableModel();
        try {
            while (cursor.moveToNext()) {
                timetableModel.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                timetableModel.setLessonTitle(cursor.getString(cursor.getColumnIndex(KEY_LESSON_TITLE)));
                timetableModel.setStartTime(cursor.getString(cursor.getColumnIndex(KEY_START_TIME)));
                timetableModel.setEndTime(cursor.getString(cursor.getColumnIndex(KEY_END_TIME)));
                timetableModel.setLessonTopic(cursor.getString(cursor.getColumnIndex(KEY_LESSON_TOPIC)));
                timetableModel.setAuditoriumNumber(cursor.getString(cursor.getColumnIndex(KEY_AUDITORIUM_NUMBER)));
                timetableModel.setScheduleDate(cursor.getString(cursor.getColumnIndex(KEY_SCHEDULE_DATE)));
                timetableModel.setSubgroups(
                        Lists.newArrayList(Splitter.on(",")
                                .trimResults()
                                .omitEmptyStrings()
                                .splitToList(cursor.getString(cursor.getColumnIndex(KEY_SUBGROUPS))))
                );
                timetableModel.setTypeTitle(cursor.getString(cursor.getColumnIndex(KEY_TYPE_TITLE)));
                timetableModel.setLessonTutors(
                        Lists.newArrayList(Splitter.on(",")
                                .trimResults()
                                .omitEmptyStrings()
                                .splitToList(cursor.getString(cursor.getColumnIndex(KEY_LESSON_TUTORS))))
                );
            }
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }
}

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

@SuppressWarnings("unused")
class Timetable implements DatabaseTable {
    private static final String TAG = Timetable.class.getSimpleName();

    // DatabaseTable name
    private static final String TABLE_TIMETABLE = "timetable";

    // Timetable table columns
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
        String SQL_QUERY = "CREATE TABLE " + TABLE_TIMETABLE +
                "(" +
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
            db.execSQL(SQL_QUERY);
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        }
        return true;
    }

    boolean dropTable(SQLiteDatabase db) {
        String SQL_QUERY = String.format("DROP TABLE %s", TABLE_TIMETABLE);
        try {
            db.execSQL(SQL_QUERY);
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        }
        return true;
    }

    boolean clearTable(SQLiteDatabase db) {
        String SQL_QUERY = String.format("DELETE FROM %s", TABLE_TIMETABLE);
        try {
            db.execSQL(SQL_QUERY);
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        }
        return true;
    }

    int countTableEntries(SQLiteDatabase db) {
        int count = 0;
        String SQL_QUERY = String.format("SELECT COUNT(*) FROM %s", TABLE_TIMETABLE);
        Cursor cursor = db.rawQuery(SQL_QUERY, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return 0;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    boolean addEntry(SQLiteDatabase db, TimetableModel object) {
        if (object == null) {
            return false;
        }
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, object.getTitle());
            values.put(KEY_LESSON_TITLE, object.getLessonTitle());
            values.put(KEY_START_TIME, object.getStartTime());
            values.put(KEY_END_TIME, object.getEndTime());
            values.put(KEY_LESSON_TOPIC, object.getLessonTopic());
            values.put(KEY_AUDITORIUM_NUMBER, object.getAuditoriumNumber());
            values.put(KEY_SCHEDULE_DATE, object.getScheduleDate());
            values.put(KEY_SUBGROUPS, Joiner.on(",").join(object.getSubgroups()));
            values.put(KEY_TYPE_TITLE, object.getTypeTitle());
            values.put(KEY_LESSON_TUTORS, Joiner.on(",").join(object.getLessonTutors()));
            db.insertOrThrow(TABLE_TIMETABLE, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        } finally {
            db.endTransaction();
        }
        return true;
    }

    boolean addEntries(SQLiteDatabase db, List<TimetableModel> objects) {
        if (objects == null || objects.isEmpty()) {
            return false;
        }
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (TimetableModel object : objects) {
                values.put(KEY_TITLE, object.getTitle());
                values.put(KEY_LESSON_TITLE, object.getLessonTitle());
                values.put(KEY_START_TIME, object.getStartTime());
                values.put(KEY_END_TIME, object.getEndTime());
                values.put(KEY_LESSON_TOPIC, object.getLessonTopic());
                values.put(KEY_AUDITORIUM_NUMBER, object.getAuditoriumNumber());
                values.put(KEY_SCHEDULE_DATE, object.getScheduleDate());
                values.put(KEY_SUBGROUPS, Joiner.on(",").join(object.getSubgroups()));
                values.put(KEY_TYPE_TITLE, object.getTypeTitle());
                values.put(KEY_LESSON_TUTORS, Joiner.on(",").join(object.getLessonTutors()));
                db.insertOrThrow(TABLE_TIMETABLE, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return false;
        } finally {
            db.endTransaction();
        }
        return true;
    }

    Map<String, List<TimetableModel>> getEntries(SQLiteDatabase db, List<String> filters) {
        Map<String, List<TimetableModel>> schedule = new TreeMap<>();
        String SQL_QUERY = String.format("SELECT * FROM %s", TABLE_TIMETABLE);
        Cursor cursor = db.rawQuery(SQL_QUERY, null);
        try {
            while (cursor.moveToNext()) {
                TimetableModel timetableModel = new TimetableModel();
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
                for (String subgroup : filters) {
                    if (timetableModel.getSubgroups().contains(subgroup)) {
                        if (!schedule.containsKey(subgroup)) {
                            schedule.put(subgroup, Lists.newArrayList(timetableModel));
                        } else {
                            schedule.get(subgroup).add(timetableModel);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.d(TAG, new ErrorMessage(e).toString());
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return schedule;
    }
}

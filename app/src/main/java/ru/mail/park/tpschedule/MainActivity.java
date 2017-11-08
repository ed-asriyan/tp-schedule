package ru.mail.park.tpschedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mail.park.tpschedule.database.DatabaseManager;
import ru.mail.park.tpschedule.database.TimetableModel;
import ru.mail.park.tpschedule.network.NetworkManager;
import ru.mail.park.tpschedule.network.OnScheduleGetListener;
import ru.mail.park.tpschedule.utils.ErrorMessage;

/*
 * TODO 1) implement logic for network manager to handle updates greatly
 * TODO 2) consider restructuring database
 * TODO 3) think on how to make MapBuilder templated
 * TODO 4) implement transport facade
 * TODO 5) test for work
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static NetworkManager networkManager;
    private static DatabaseManager databaseManager;

    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    /*
     * Called after timetable was returned from network request to TP server
     */
    private OnScheduleGetListener onScheduleGetListener = new OnScheduleGetListener() {
        // All updates write here
        @Override
        public void onSuccess(Map<String, List<TimetableModel>> result) {
            for (String group : result.keySet()) {
                for (TimetableModel model : result.get(group)) {
                    Log.d(TAG, group + " -> " + model.getTitle());
                }
            }
        }

        @Override
        public void onFailure(Throwable error) {
            String errorMessage = new ErrorMessage(error).toString();
            Log.d(TAG, errorMessage);
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkManager = NetworkManager.getInstance();
        databaseManager = DatabaseManager.getInstance(getApplicationContext());

        networkManager.getTimetable(Lists.newArrayList("АПО-31", "АПО-11"), 0, 0, "semester", onScheduleGetListener);
    }
}

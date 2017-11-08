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

import javax.inject.Inject;

import ru.mail.park.tpschedule.database.DatabaseManager;
import ru.mail.park.tpschedule.database.TimetableModel;
import ru.mail.park.tpschedule.injection.App;
import ru.mail.park.tpschedule.network.NetworkManager;
import ru.mail.park.tpschedule.network.OnScheduleGetListener;
import ru.mail.park.tpschedule.network.ParkResponse;
import ru.mail.park.tpschedule.utils.ContainerBuilder;
import ru.mail.park.tpschedule.utils.ErrorMessage;

/*
 * TODO 1) implement logic for network manager to handle updates greatly
 * TODO 2) consider restructuring database
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    NetworkManager networkManager;
    @Inject
    DatabaseManager databaseManager;

    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    /*
     * Called after timetable was returned from network request to TP server
     */
    private OnScheduleGetListener onScheduleGetListener = new OnScheduleGetListener() {
        // All updates write here
        @Override
        public void onSuccess(final List<ParkResponse.ResponseObject> schedule, List<String> filter) {
//            databaseExecutor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    databaseManager.addTimetableEntries(ContainerBuilder.toList(schedule));
//                }
//            });
            Map<String, List<TimetableModel>> filteredSchedule = ContainerBuilder.toMap(schedule, filter);
            for (String group : filteredSchedule.keySet()) {
                for (TimetableModel model : filteredSchedule.get(group)) {
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

        App.getComponent().inject(this);

        networkManager.getTimetable(Lists.newArrayList("АПО-31", "АПО-11"), 0, 0, "semester", onScheduleGetListener);
    }
}

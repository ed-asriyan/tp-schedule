package ru.mail.park.tpschedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import ru.mail.park.tpschedule.transport.TransportFacade;
import ru.mail.park.tpschedule.transport.database.TimetableModel;
import ru.mail.park.tpschedule.transport.network.OnScheduleGetListener;
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

    private TransportFacade transportFacade;

    /*
     * Called after timetable was returned from network request to TP server
     */
    private OnScheduleGetListener onScheduleGetListener = new OnScheduleGetListener() {
        // All updates write here
        @Override
        public void onSuccess(Map<String, List<TimetableModel>> schedule) {
            for (String group : schedule.keySet()) {
                for (TimetableModel model : schedule.get(group)) {
                    Log.d(TAG, group + " -> " + model.getTitle());
                }
            }
        }

        @Override
        public void onFailure(Throwable error) {
            Toast.makeText(getApplicationContext(), new ErrorMessage(error).toString(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transportFacade = new TransportFacade(getApplicationContext());
        transportFacade.updateTimetable(Lists.newArrayList("АПО-31", "АПО-11"), 0, 0, "semester", onScheduleGetListener);
    }
}

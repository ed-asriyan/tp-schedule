package ru.mail.park.tpschedule;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mail.park.tpschedule.adapters.ScheduleViewAdapter;
import ru.mail.park.tpschedule.database.DatabaseManager;
import ru.mail.park.tpschedule.database.TimetableModel;
import ru.mail.park.tpschedule.injection.App;
import ru.mail.park.tpschedule.network.NetworkManager;
import ru.mail.park.tpschedule.network.OnScheduleGetListener;
import ru.mail.park.tpschedule.network.ParkResponse;
import ru.mail.park.tpschedule.utils.ContainerBuilder;
import ru.mail.park.tpschedule.utils.ErrorMessage;

public class MainActivity extends AppCompatActivity {
    static {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .penaltyLog()
                .penaltyDeath()
                .build()
        );
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    NetworkManager networkManager;
    @Inject
    DatabaseManager databaseManager;

    // Wrap all database communication into this executor
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    /*
     * Called after timetable was returned from network request to TP server
     */
    private OnScheduleGetListener onScheduleGetListener = new OnScheduleGetListener() {
        // All updates write here
        @Override
        public void onSuccess(final List<ParkResponse.ResponseObject> result) {
            final List<TimetableModel> fullSchedule = ContainerBuilder.transformResponseList(result);
            databaseExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    databaseManager.updateSchedule(fullSchedule);
                }
            });
            List<String> groups = getGroups();
            currentSchedule = ContainerBuilder.filter(fullSchedule, groups);
            adapter.setData(currentSchedule);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Throwable error) {
            String errorMessage = new ErrorMessage(error).toString();
            Log.d(TAG, errorMessage);
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    };

    @BindView(R.id.update_button)
    Button updateButton;
    @BindView(R.id.get_count_button)
    Button getCountButton;
    @BindView(R.id.get_entries_button)
    Button getEntriesButton;
    @BindView(R.id.groups_field)
    EditText groupsEdit;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @OnClick(R.id.update_button)
    void onUpdateButtonClick() {
        List<String> groups = getGroups();
        networkManager.getTimetable(groups, 0, 0, "semester", onScheduleGetListener);
    }

    @OnClick(R.id.get_count_button)
    void onGetCountButtonClick() {
        int count = databaseManager.getTimetableEntriesCount();
        Log.d(TAG, Integer.toString(count));
    }

    @OnClick(R.id.get_entries_button)
    void onGetEntriesButtonClick() {
        List<String> groups = getGroups();
        List<TimetableModel> entries = databaseManager.getTimetableEntries(groups);
        for (TimetableModel model : entries) {
            Log.d(TAG, model.getSubgroups() + " -> " + model.getTitle() + " -> " + model.getScheduleDate());
        }
    }

    List<TimetableModel> currentSchedule;
    private ScheduleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getComponent().inject(this);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentSchedule = databaseManager.getTimetableEntries(Lists.newArrayList("АПО-31"));
        adapter = new ScheduleViewAdapter(currentSchedule);
        recyclerView.setAdapter(adapter);
    }

    // Cap made for testing purposes
    private List<String> getGroups() {
        return Lists.newArrayList(Splitter.on(",")
                .trimResults()
                .omitEmptyStrings()
                .splitToList(groupsEdit.getText().toString()));
    }
}

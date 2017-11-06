package ru.mail.park.tpschedule.transport.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.tpschedule.transport.database.TimetableModel;
import ru.mail.park.tpschedule.utils.MapBuilder;

/**
 * Created by lieroz
 * 06.11.17
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "WeakerAccess"})
public class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager INSTANCE;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final TechnoparkApi technoparkApi;
    private Call<List<ResponseObject>> currentTpApiCall;

    private static final String HOST = "https://park.mail.ru";

    private NetworkManager() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        technoparkApi = retrofit.create(TechnoparkApi.class);
    }

    public static synchronized NetworkManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkManager();
        }
        return INSTANCE;
    }

    public void getTimetable(final List<String> groups) {
        currentTpApiCall = technoparkApi.getTimetable(0, 0, "semester");
        currentTpApiCall.enqueue(new Callback<List<ResponseObject>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResponseObject>> call, @NonNull Response<List<ResponseObject>> response) {
                if (response.isSuccessful()) {
                    invokeSuccess(MapBuilder.toMap(response.body(), groups));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResponseObject>> call, @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    invokeError(t);
                }
            }
        });
    }

    private void invokeSuccess(final Map<String, List<TimetableModel>> timetable) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // TODO some good activity
            }
        });
    }

    private void invokeError(final Throwable error) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // TODO some error activity
            }
        });
    }
}

package ru.mail.park.tpschedule.transport.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.tpschedule.transport.database.TimetableModel;
import ru.mail.park.tpschedule.utils.ErrorMessage;
import ru.mail.park.tpschedule.utils.MapBuilder;

/**
 * Created by lieroz
 * 06.11.17
 */

public class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager INSTANCE;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final TechnoparkApi technoparkApi;
    private Call<ParkResponse> currentTpApiCall;

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
        currentTpApiCall.enqueue(new Callback<ParkResponse>() {
            @Override
            public void onResponse(@NonNull Call<ParkResponse> call, @NonNull Response<ParkResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ParkResponse.ResponseObject> objects = response.body().getSchedule();
                        invokeSuccess(MapBuilder.toMap(objects, groups));
                    } else {
                        throw new HttpException(response);
                    }
                } catch (HttpException e) {
                    invokeError(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ParkResponse> call, @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    invokeError(t);
                }
            }
        });
    }

    // TODO add some callback handler here to return to facade
    private void invokeSuccess(final Map<String, List<TimetableModel>> timetable) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (String key : timetable.keySet()) {
                    for (TimetableModel t : timetable.get(key)) {
                        Log.d(TAG, key + " -> " + t.getTitle());
                    }
                }
            }
        });
    }

    private void invokeError(final Throwable error) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, new ErrorMessage(error).toString());
            }
        });
    }
}

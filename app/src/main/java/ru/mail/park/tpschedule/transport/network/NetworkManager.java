package ru.mail.park.tpschedule.transport.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.tpschedule.transport.database.TimetableModel;

/**
 * Created by lieroz
 * 06.11.17
 */

@SuppressWarnings({"FieldCanBeLocal"})
public class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager INSTANCE = new NetworkManager();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final TechnoparkApi technoparkApi;
    private Call<ParkResponse> currentCall;

    private static final String HOST = "https://park.mail.ru";

    private NetworkManager() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        technoparkApi = retrofit.create(TechnoparkApi.class);
    }

    public static NetworkManager getInstance() {
        return INSTANCE;
    }

    public ListenerHandler<OnScheduleGetListener> getTimetable(final List<String> groups, int start, int end, String interval, final OnScheduleGetListener listener) {
        final ListenerHandler<OnScheduleGetListener> handler = new ListenerHandler<>(listener);
        currentCall = technoparkApi.getTimetable(end, start, interval);
        currentCall.enqueue(new Callback<ParkResponse>() {
            @Override
            public void onResponse(@NonNull Call<ParkResponse> call, @NonNull Response<ParkResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        // TODO what to do will this highlight!??? NullPointerException
                        List<ParkResponse.ResponseObject> objects = response.body().getSchedule();
                        invokeSuccess(handler, MapBuilder.toMap(objects, groups));
                    } else {
                        throw new HttpException(response);
                    }
                } catch (HttpException e) {
                    invokeError(handler, e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ParkResponse> call, @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    invokeError(handler, t);
                }
            }
        });
        return handler;
    }

    private void invokeSuccess(final ListenerHandler<OnScheduleGetListener> handler, final Map<String, List<TimetableModel>> timetable) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                OnScheduleGetListener listener = handler.getListener();
                if (listener != null) {
                    listener.onSuccess(timetable);
                }
            }
        });
    }

    private void invokeError(final ListenerHandler<OnScheduleGetListener> handler, final Throwable error) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                OnScheduleGetListener listener = handler.getListener();
                if (listener != null) {
                    listener.onFailure(error);
                }
            }
        });
    }
}

package ru.mail.park.tpschedule.transport.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.tpschedule.utils.ErrorMessage;

/**
 * Created by ed on 06.11.17.
 */

public class Client {
    public interface TechnoparkApiListener {
        void onSuccess(final List<ResponseObject> timetable);

        void onFailure(final Exception error);
    }

    private static final String TAG = Client.class.getSimpleName();

    private static Client INSTANCE;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final TechnoparkApi technoparkApi;

    private static final String HOST = "https://park.mail.ru";

    private Client() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        technoparkApi = retrofit.create(TechnoparkApi.class);
    }

    public static synchronized Client getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Client();
        }
        return INSTANCE;
    }

    public ListenerHandler<TechnoparkApiListener> getTimetable(List<String> groups, TechnoparkApiListener listener) {
        final ListenerHandler<TechnoparkApiListener> handler = new ListenerHandler<>(listener);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response<List<ResponseObject>> response = technoparkApi.getTimetable(0, 0, "semester").execute();
                    if (response.code() != HttpsURLConnection.HTTP_OK || response.body() == null) {
                        throw new HttpException(response);
                    }
                    invokeSuccess(handler, response.body());
                } catch (IOException e) {
                    Log.d(TAG, new ErrorMessage(e).toString());
                    invokeError(handler, e);
                }
            }
        });
        return handler;
    }

    private void invokeSuccess(final ListenerHandler<TechnoparkApiListener> handler, final List<ResponseObject> timetable) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                TechnoparkApiListener listener = handler.getListener();
                if (listener != null) {
                    listener.onSuccess(timetable);
                }
            }
        });
    }

    private void invokeError(final ListenerHandler<TechnoparkApiListener> handler, final Exception error) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                TechnoparkApiListener listener = handler.getListener();
                if (listener != null) {
                    listener.onFailure(error);
                }
            }
        });
    }
}

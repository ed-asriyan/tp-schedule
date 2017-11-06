package ru.mail.park.tpschedule.transport.network;

import android.support.annotation.Nullable;

/**
 * Created by ed on 06.11.17.
 */

public class ListenerHandler<T> {
    private T listener;

    public ListenerHandler(T listener) {
        this.listener = listener;
    }

    @Nullable
    public T getListener() {
        return listener;
    }

    public void unregister() {
        listener = null;
    }
}

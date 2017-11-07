package ru.mail.park.tpschedule.transport.network;

import android.support.annotation.Nullable;

/**
 * Created by yaches on 08.11.17
 */

public class ListenerHandler<T> {
    private T listener;

    public ListenerHandler(final T listener) {
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

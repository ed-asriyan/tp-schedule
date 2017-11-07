package ru.mail.park.tpschedule.transport;

import android.content.Context;

import java.util.List;

import ru.mail.park.tpschedule.transport.database.DatabaseManager;
import ru.mail.park.tpschedule.transport.network.NetworkManager;
import ru.mail.park.tpschedule.transport.network.OnScheduleGetListener;

/**
 * Created by ed on 07.11.17
 */

public class TransportFacade {
    private static DatabaseManager databaseManager;
    private static NetworkManager networkManager;

    public TransportFacade(Context context) {
        databaseManager = DatabaseManager.getInstance(context);
        networkManager = NetworkManager.getInstance();
    }

    public void updateTimetable(List<String> groups, int start, int end, String interval, OnScheduleGetListener listener) {
        networkManager.getTimetable(groups, start, end, interval, listener);
    }
}

package ru.mail.park.tpschedule.transport;

import android.content.Context;

import ru.mail.park.tpschedule.transport.database.DatabaseManager;
import ru.mail.park.tpschedule.transport.network.NetworkManager;

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
}

package ru.mail.park.tpschedule.injection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.mail.park.tpschedule.network.NetworkManager;

/**
 * Created by yaches on 08.11.17
 */

@Module
public class NetworkModule {
    @Provides
    @Singleton
    public NetworkManager provideSingleton() {
        return new NetworkManager();
    }
}

package ru.mail.park.tpschedule.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.mail.park.tpschedule.database.DatabaseManager;

/**
 * Created by yaches on 08.11.17
 */

@Module
public class DatabaseModule {
    private final Context context;

    public DatabaseModule(final Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    public DatabaseManager provideSingleton() {
        return new DatabaseManager(this.context);
    }
}

package ru.mail.park.tpschedule.injection;

import android.app.Application;

/**
 * Created by yaches on 08.11.17
 */

public class App extends Application {
    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .build();
    }
}

package ru.mail.park.tpschedule.injection;

import javax.inject.Singleton;

import dagger.Component;
import ru.mail.park.tpschedule.MainActivity;

/**
 * Created by yaches on 08.11.17
 */

@Component(modules = {NetworkModule.class, DatabaseModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity activity);
}

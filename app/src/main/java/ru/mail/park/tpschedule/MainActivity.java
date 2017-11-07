package ru.mail.park.tpschedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.mail.park.tpschedule.transport.TransportFacade;

/*
 * TODO 1) implement logic for network manager to handle updates greatly
 * TODO 2) consider restructuring database
 * TODO 3) think on how to make MapBuilder templated
 * TODO 4) implement transport facade
 * TODO 5) test for work
 */

public class MainActivity extends AppCompatActivity {
    private TransportFacade transportFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transportFacade = new TransportFacade(getApplicationContext());
        transportFacade.updateTimetable();
    }
}

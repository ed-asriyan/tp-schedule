package ru.mail.park.tpschedule.transport.network;

import java.util.List;
import java.util.Map;

import ru.mail.park.tpschedule.transport.database.TimetableModel;

/**
 * Created by yaches on 08.11.17
 */

public interface OnScheduleGetListener {
    void onSuccess(Map<String, List<TimetableModel>> schedule);

    void onFailure(Throwable error);
}

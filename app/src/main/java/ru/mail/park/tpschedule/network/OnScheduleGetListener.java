package ru.mail.park.tpschedule.network;

import java.util.List;

/**
 * Created by yaches on 08.11.17
 */

public interface OnScheduleGetListener {
    void onSuccess(List<ParkResponse.ResponseObject> schedule, List<String> filter);

    void onFailure(Throwable error);
}

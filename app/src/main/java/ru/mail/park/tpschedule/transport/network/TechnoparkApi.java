package ru.mail.park.tpschedule.transport.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lieroz on 06.11.17.
 */

public interface TechnoparkApi {
    @GET("/schedule/calendar_gtp")
    Call<List<ResponseObject>> getTimetable(@Query("start") int start,
                                            @Query("end") int end,
                                            @Query("interval") String interval);
}

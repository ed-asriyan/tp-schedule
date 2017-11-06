package ru.mail.park.tpschedule.transport.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lieroz
 * 06.11.17.
 */

public class ResponseObject {
    @SerializedName("short_title")
    private String title;
    @SerializedName("lesson_title")
    private String lessonTitle;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("lesson_topic")
    private String lessonTopic;
    @SerializedName("auditorium_number")
    private String auditoriumNumber;
    @SerializedName("schedule_date")
    private String scheduleDate;
    @SerializedName("subgroups")
    private List<String> subgroups;
    @SerializedName("type_title")
    private String typeTitle;
    @SerializedName("lesson_tutors")
    private List<String> lessonTutors;

    public String getTitle() {
        return title;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLessonTopic() {
        return lessonTopic;
    }

    public String getAuditoriumNumber() {
        return auditoriumNumber;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public List<String> getSubgroups() {
        return subgroups;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public List<String> getLessonTutors() {
        return lessonTutors;
    }
}

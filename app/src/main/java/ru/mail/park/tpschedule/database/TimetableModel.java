package ru.mail.park.tpschedule.database;

import java.util.List;

import ru.mail.park.tpschedule.network.ParkResponse;

/**
 * Created by lieroz
 * 06.11.17.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class TimetableModel implements DatabaseEntity {
    private String title;
    private String lessonTitle;
    private String startTime;
    private String endTime;
    private String lessonTopic;
    private String auditoriumNumber;
    private String scheduleDate;
    private List<String> subgroups;
    private String typeTitle;
    private List<String> lessonTutors;

    public TimetableModel() {

    }

    public TimetableModel(String title, String lessonTitle, String startTime,
                          String endTime, String lessonTopic, String auditoriumNumber,
                          String scheduleDate, List<String> subgroups, String typeTitle,
                          List<String> lessonTutors) {
        this.title = title;
        this.lessonTitle = lessonTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lessonTopic = lessonTopic;
        this.auditoriumNumber = auditoriumNumber;
        this.scheduleDate = scheduleDate;
        this.subgroups = subgroups;
        this.typeTitle = typeTitle;
        this.lessonTutors = lessonTutors;
    }

    public TimetableModel(ParkResponse.ResponseObject object) {
        this.title = object.getTitle();
        this.lessonTitle = object.getLessonTitle();
        this.startTime = object.getStartTime();
        this.endTime = object.getEndTime();
        this.lessonTopic = object.getLessonTopic();
        this.auditoriumNumber = object.getAuditoriumNumber();
        this.scheduleDate = object.getScheduleDate();
        this.subgroups = object.getSubgroups();
        this.typeTitle = object.getTypeTitle();
        this.lessonTutors = object.getLessonTutors();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLessonTopic() {
        return lessonTopic;
    }

    public void setLessonTopic(String lessonTopic) {
        this.lessonTopic = lessonTopic;
    }

    public String getAuditoriumNumber() {
        return auditoriumNumber;
    }

    public void setAuditoriumNumber(String auditoriumNumber) {
        this.auditoriumNumber = auditoriumNumber;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public List<String> getSubgroups() {
        return subgroups;
    }

    public void setSubgroups(List<String> subgroups) {
        this.subgroups = subgroups;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    public List<String> getLessonTutors() {
        return lessonTutors;
    }

    public void setLessonTutors(List<String> lessonTutors) {
        this.lessonTutors = lessonTutors;
    }
}

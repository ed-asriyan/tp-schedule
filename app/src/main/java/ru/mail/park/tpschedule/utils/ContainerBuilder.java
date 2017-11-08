package ru.mail.park.tpschedule.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ru.mail.park.tpschedule.database.TimetableModel;
import ru.mail.park.tpschedule.network.ParkResponse;

/**
 * Created by yaches 07.11.17
 */

public class ContainerBuilder {
    private static final String LESSON = "lesson";

    public static List<TimetableModel> transformResponseList(List<ParkResponse.ResponseObject> list) {
        List<TimetableModel> newList = new ArrayList<>();
        for (ParkResponse.ResponseObject object : list) {
            if (Objects.equals(object.getTypeEntity(), LESSON)) {
                newList.add(new TimetableModel(object));
            }
        }
        Collections.sort(newList, new Comparator<TimetableModel>() {
            @Override
            public int compare(TimetableModel o1, TimetableModel o2) {
                return o1.getScheduleDate().compareTo(o2.getScheduleDate());
            }
        });
        return newList;
    }

    public static List<TimetableModel> filter(List<TimetableModel> list, List<String> filters) {
        List<TimetableModel> filteredList = new ArrayList<>();
        for (TimetableModel model : list) {
            for (String filter : filters) {
                if (model.getSubgroups().contains(filter)) {
                    filteredList.add(model);
                }
            }
        }
        return filteredList;
    }
}

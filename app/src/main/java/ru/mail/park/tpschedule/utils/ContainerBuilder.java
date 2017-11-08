package ru.mail.park.tpschedule.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;
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
        sort(newList);
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

    public static void sort(List<TimetableModel> list) {
        Collections.sort(list, new Comparator<TimetableModel>() {
            @Override
            public int compare(TimetableModel o1, TimetableModel o2) {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date d1, d2;
                try {
                    d1 = dateFormat.parse(o1.getScheduleDate());
                    d2 = dateFormat.parse(o2.getScheduleDate());
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format!");
                }
                return d1.compareTo(d2);
            }
        });
    }
}

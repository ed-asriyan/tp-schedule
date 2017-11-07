package ru.mail.park.tpschedule.transport.network;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import ru.mail.park.tpschedule.transport.database.TimetableModel;

/**
 * Created by yaches 07.11.17
 */

class MapBuilder {
    private static final String LESSON = "lesson";

    static Map<String, List<TimetableModel>> toMap(List<ParkResponse.ResponseObject> sourceList, List<String> filters) {
        Map<String, List<TimetableModel>> map = new TreeMap<>();
        for (ParkResponse.ResponseObject object : sourceList) {
            if (Objects.equals(object.getTypeEntity(), LESSON)) {
                for (String subgroup : object.getSubgroups()) {
                    if (filters.contains(subgroup)) {
                        if (map.containsKey(subgroup)) {
                            map.get(subgroup).add(new TimetableModel(object));
                        } else {
                            map.put(subgroup, Lists.newArrayList(new TimetableModel(object)));
                        }
                    }
                }
            }
        }
        return map;
    }
}

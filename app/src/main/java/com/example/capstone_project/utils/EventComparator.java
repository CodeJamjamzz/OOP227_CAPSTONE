package com.example.capstone_project.utils;

import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        if (o1.getStartDate() == null && o2.getStartDate() == null) {
            return 0;
        }
        if (o1.getStartDate() == null) {
            return 1;
        }
        if (o2.getStartDate() == null) {
            return -1;
        }

        LocalDateTime startDate1 = LocalDateTime.parse(o1.getStartDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime startDate2 = LocalDateTime.parse(o2.getStartDate(), DateTimeFormatter.ISO_DATE_TIME);

        return startDate1.compareTo(startDate2);
    }
}

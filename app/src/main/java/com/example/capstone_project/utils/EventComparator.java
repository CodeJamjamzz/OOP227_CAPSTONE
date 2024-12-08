package com.example.capstone_project.utils;

import android.util.Log;

import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        LocalDateTime startDate1 = LocalDateTime.parse(o1.getStartDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime startDate2 = LocalDateTime.parse(o2.getStartDate(), DateTimeFormatter.ISO_DATE_TIME);

        if (startDate1 == null && startDate2 == null) {
            return 0;
        }
        if (startDate1 == null) {
            return 1;
        }
        if (startDate2 == null) {
            return -1;
        }


        return startDate1.compareTo(startDate2);
    }
}

package com.example.capstone_project.utils;

import com.example.capstone_project.models.Event;

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

        return o1.getStartDate().compareTo(o2.getStartDate());
    }
}

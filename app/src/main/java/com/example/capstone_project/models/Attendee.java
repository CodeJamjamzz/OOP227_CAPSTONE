package com.example.capstone_project.models;

import java.util.ArrayList;
import java.util.List;

public class Attendee {
    private String name;
    private String idNumber;
    private String email;
    private String yearLevel;
    private List<Event> eventsRegistered;

    public Attendee(String name, String idNumber, String email, String yearLevel) {
        this.name = name;
        this.idNumber = idNumber;
        this.email = email;
        this.yearLevel = yearLevel;
        eventsRegistered = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public List<Event> getEventsRegistered() {
        return eventsRegistered;
    }
}

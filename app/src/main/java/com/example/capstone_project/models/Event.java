package com.example.capstone_project.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Event {
    private String name;
    private final String eventId; // eventID must not be changeable
    private String startDate;
    private String endDate;
    private String venue;
    private int audienceLimit;
    private String description;
    private String category;
    private double ticketPrice;
    private Map<String, Attendee> attendees;

    public Event() {
        startDate = null;
        endDate = null;
        venue = "TBA";
        audienceLimit = 0;
        description = "TBA";
        category = null;
        ticketPrice = 0.0;
        this.eventId = UUID.randomUUID().toString();
        this.attendees = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getEventId() {
        return eventId;
    }

    public String getStartDate() { return startDate; }

    public String getEndDate() {
        return endDate;
    }

    public String getVenue() {
        return venue;
    }

    public int getAudienceLimit() {
        return audienceLimit;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public Map<String, Attendee> getAttendees() {
        return attendees;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setAudienceLimit(int audienceLimit) {
        this.audienceLimit = audienceLimit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setAttendees(Map<String, Attendee> attendees) {
        this.attendees = attendees;
    }

    @NonNull
    @Override
    // name of the event object will always be its own event ID as it will always stay and be unique
    public String toString() {
        return eventId;
    }
}

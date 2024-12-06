package com.example.capstone_project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event {
    private String name;
    private String eventId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;
    private int audienceLimit;
    private String description;
    private String category;
    private double ticketPrice;
    private List<Attendee> attendees;

    public Event() {
        startDate = null;
        endDate = null;
        venue = "TBA";
        audienceLimit = 0;
        description = "TBA";
        category = null;
        ticketPrice = 0.0;
        this.eventId = UUID.randomUUID().toString();
        this.attendees = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
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

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
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

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

}

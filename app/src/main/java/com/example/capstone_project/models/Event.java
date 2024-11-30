package com.example.capstone_project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;
    private int audienceLimit;
    private String description;
    private String category;
    private double ticketPrice;
    private List<Attendee> attendants;

    public Event(String name, LocalDateTime startDate, LocalDateTime endDate, String venue, int audienceLimit, String description, String category, double ticketPrice) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venue = venue;
        this.audienceLimit = audienceLimit;
        this.description = description;
        this.category = category;
        this.ticketPrice = ticketPrice;
        attendants = new ArrayList<>();
    }

    public String getName() {
        return name;
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

    public List<Attendee> getAttendants() {
        return attendants;
    }

}

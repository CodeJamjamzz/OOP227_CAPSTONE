package com.example.capstone_project.models;

public interface Builder {
    Builder setEventName(String eventName);
    Builder setStartDateTime(String startDate);
    Builder setEndDateTime(String endDate);
    Builder setVenue(String venue);
    Builder setAudienceLimit(int audienceLimit);
    Builder setDescription(String description);
    Builder setTicketPrice(double ticketPrice);
    Event build();
}

package com.example.capstone_project.models;

import java.time.LocalDateTime;

public interface Builder {
    Builder setEventName(String eventName);
    Builder setStartDateTime(LocalDateTime startDate);
    Builder setEndDateTime(LocalDateTime endDate);
    Builder setVenue(String venue);
    Builder setAudienceLimit(int audienceLimit);
    Builder setDescription(String description);
    Event build();
}

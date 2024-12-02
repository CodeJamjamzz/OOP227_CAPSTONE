package com.example.capstone_project.utils;

import com.example.capstone_project.models.Builder;
import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;

public class EventBuilder implements Builder {

    private Event event;

    public EventBuilder() {
        event = new Event();
    }

    @Override
    public EventBuilder setEventName(String eventName) {
        if (!eventName.equals("")) {
            event.setName(eventName);
        }
        return this;
    }

    @Override
    public EventBuilder setStartDateTime(LocalDateTime startDate) {
        event.setStartDate(startDate);
        return this;
    }

    @Override
    public EventBuilder setEndDateTime(LocalDateTime endDate) {
        event.setEndDate(endDate);
        return this;
    }

    @Override
    public EventBuilder setVenue(String venue) {
        event.setVenue(venue);
        return this;
    }

    @Override
    public EventBuilder setAudienceLimit(int audienceLimit) {
        event.setAudienceLimit(audienceLimit);
        return this;
    }

    @Override
    public EventBuilder setDescription(String description) {
        event.setDescription(description);
        return this;
    }

    @Override
    public Event build() {
        return event;
    }
}

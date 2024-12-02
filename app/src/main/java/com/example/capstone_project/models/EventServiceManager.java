package com.example.capstone_project.models;

import java.util.ArrayList;
import java.util.List;

public class EventServiceManager {
    private static EventServiceManager instance;
    private List<Event> events;

    private EventServiceManager() {
        // Load events from database here ig
        events = new ArrayList<>();
    }

    public static EventServiceManager getInstance() {
        if (instance == null) {
            instance = new EventServiceManager();
        }
        return instance;
    }

    public Event createEvent() {
        return null;
    }

    public void registerAttendee(Event e, Attendee a) {
        e.getAttendees().add(a);
    }

    public Event getEventFromId(String eventId) {
        for (Event e : events) {
            if (e.getEventId().equals(eventId)) {
                return e;
            }
        }
        return null;
    }

    public boolean verifyAttendee(String eventId, String attendeeId) {
        Event event = getEventFromId(eventId);
        if (event == null) {
            throw new IllegalStateException("Event not found!");
        }
        for (Attendee b : event.getAttendees()) {
            if (attendeeId.equals(b.getIdNumber())) {
                return true;
            }
        }
        return false;
    }

    public void deleteEvent(Event e) {
        if (!events.contains(e)) {
            return;
        }
        for (Attendee b : e.getAttendees()) {
            b.getEventsRegistered().remove(e.getEventId());
        }
        events.remove(e);
    }

}


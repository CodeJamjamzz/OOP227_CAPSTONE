package com.example.capstone_project.utils;

import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventServiceManager {
    private static EventServiceManager instance;
    private List<Event> events;

    private EventServiceManager() {
        // Load events from database here ig
        events = new ArrayList<>();
        createEvent();
    }

    public static EventServiceManager getInstance() {
        if (instance == null) {
            instance = new EventServiceManager();
        }
        return instance;
    }

    public void createEvent() {
        EventBuilder builder = new EventBuilder();
        // TODO: pass event details from EventForms here
        Event event = builder.setEventName("").setVenue("").setStartDateTime(LocalDateTime.now()).setEndDateTime(LocalDateTime.now()).setAudienceLimit(0).setDescription("").build();
        events.add(event);
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

    public Event[] getEvents() {
        return events.toArray(new Event[0]);
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

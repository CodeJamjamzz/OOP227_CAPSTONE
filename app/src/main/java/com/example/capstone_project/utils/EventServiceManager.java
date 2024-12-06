package com.example.capstone_project.utils;

import android.util.Log;

import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventServiceManager {
    private String TAG = "EventServiceManager";
    private static EventServiceManager instance;
    // TODO: make Comparator for Event to sort by start date
    private static List<Event> events;

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

    public String createEvent(String name, String description, String venue, LocalDateTime startDate, LocalDateTime endDate, double ticketPrice, int audienceLimit) {
        EventBuilder builder = new EventBuilder();
        // TODO: pass event details from EventForms here
        Event event = builder
                .setEventName(name)
                .setVenue(venue)
                .setStartDateTime(startDate)
                .setEndDateTime(endDate)
                .setAudienceLimit(audienceLimit)
                .setDescription(description)
                .setTicketPrice(ticketPrice)
                .build();
        events.add(event);
        return event.getEventId();
    }

    public boolean registerAttendee(String eventId, Attendee a) {
        for (Event e : events) {
            if (e.getEventId().equals(eventId)) {
                e.getAttendees().add(a);
                return true;
            }
        }
        return false;
    }

    public Event getEventFromId(String eventId) {
        for (Event e : events) {
            if (e.getEventId().equals(eventId)) {
                return e;
            }
        }
        return null;
    }

    public Attendee getAttendeeFromId(String eventId, String attendeeId) {
        for (Attendee a : getEventFromId(eventId).getAttendees()) {
            if (a.getIdNumber().equals(attendeeId)) {
                return a;
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

    public void deleteEvent(String eventId) {
        Event e = getEventFromId(eventId);
        if (e == null) {
            Log.d(TAG, "Delete Event Error: Event not in list!");
            return;
        }
        for (Attendee b : e.getAttendees()) {
            b.getEventsRegistered().remove(e.getEventId());
        }
        events.remove(e);
    }

    public String[] getAttendeeNames(String eventId) {
        Event event = null;
        for (Event e : events) {
            if (e.getEventId().equals(eventId)) {
                event = e;
                break;
            }
        }

        if (event == null) {
            return null;
        }

        String[] attendees = new String[event.getAttendees().size()];
        int i = 0;
        for (Attendee a : event.getAttendees()) {
            attendees[i] = a.getName();
            i++;
        }
        return attendees;
    }
}


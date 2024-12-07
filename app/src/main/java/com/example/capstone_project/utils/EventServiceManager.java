package com.example.capstone_project.utils;

import android.util.Log;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// Singleton
public class EventServiceManager {
    private static EventServiceManager instance;
    private static List<Event> events;
    private final RegItFirebaseController db = RegItFirebaseController.getInstance();

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

    public void createEvent(String name, String description, String venue, LocalDateTime startDate, LocalDateTime endDate, int audienceLimit) {
        EventBuilder builder = new EventBuilder();
        // TODO: pass event details from EventForms here
        Event event = builder
                .setEventName(name)
                .setVenue(venue)
                .setStartDateTime(startDate)
                .setEndDateTime(endDate)
                .setAudienceLimit(audienceLimit)
                .setDescription(description)
                .build();
        events.add(event);

        RegItFirebaseController.getInstance().createNewEvent(event);

    }

    // TODO: add firebase connetion to dis
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
        CompletableFuture<Event> eventFuture = db.getEvent(eventId);

        try {
            return eventFuture.get(); // This will block until the future completes
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception appropriately, e.g., log the error and return null
            Log.e("EventServiceManager", "Error fetching event: " + e.getMessage());
            return null;
        }
    }

    public Attendee getAttendeeFromId(String eventId, String attendeeId) {
        for (Attendee a : getEventFromId(eventId).getAttendees()) {
            // TODO: connect with firebase
            if (a.getAttendeeIDNumber().equals(attendeeId)) {
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
            // TODO: add firebase connections in Attendee getters
            if (attendeeId.equals(b.getAttendeeIDNumber())) {
                return true;
            }
        }
        return false;
    }

    public void deleteEvent(Event e) {
        if (!events.contains(e)) {
            return;
        }
        // TODO: change to access attendeeAccountID then delete from UserAccount in DB
        // attendee -> userAccount
//        for (Attendee b : e.getAttendees()) {
//            b.getEventsRegistered().remove(e.getEventId());
//        }
        events.remove(e);
    }

}


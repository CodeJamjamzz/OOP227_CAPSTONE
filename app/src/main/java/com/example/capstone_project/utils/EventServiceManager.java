package com.example.capstone_project.utils;

import android.util.Log;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// Singleton
public class EventServiceManager {
    private String TAG = "EventServiceManager";
    private static EventServiceManager instance;
    // DONE: make Comparator for Event to sort by start date
    private static List<Event> events;
    private final RegItFirebaseController db = RegItFirebaseController.getInstance();

    private EventServiceManager() {
        // Load events from database here ig
        events = db.getEventsfromDB();
        sortEvents();
    }

    private static void sortEvents() {
        events.sort(new EventComparator());
    }

    public static EventServiceManager getInstance() {
        if (instance == null) {
            instance = new EventServiceManager();
        }
        sortEvents();
        return instance;
    }

    public void createEvent(String name, String description, String venue, String startDate, String endDate, double ticketPrice, int audienceLimit) {
        EventBuilder builder = new EventBuilder();
        // DONE: pass event details from EventForms here
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

        RegItFirebaseController.getInstance().createNewEvent(event);

        sortEvents();
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

    public boolean unRegisterAttendee(String eventId, String attendeeId) {
        Event event = getEventFromId(eventId);
        for (Attendee a : event.getAttendees()) {
            if (a.getAttendeeIDNumber().equals(attendeeId)) {

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
            // TODO: connect with firebase
            if (a.getAttendeeIDNumber().equals(attendeeId)) {
                return a;
            }
        }
        return null;
    }

    public String getAttendeeFromName(String eventId, String attendeeName) {
        for (Attendee a : getEventFromId(eventId).getAttendees()) {
            if (a.getAttendeeName().equals(attendeeName)) {
                return a.getAttendeeName();
            }
        }
        return "";
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

    public void deleteEvent(String eventId) {
        Event e = getEventFromId(eventId);
        if (e == null) {
            Log.d(TAG, "Delete Event Error: Event not in list!");
            return;
        }
        // TODO: change to access attendeeAccountID then delete from UserAccount in DB
        // attendee -> userAccount
//        for (Attendee b : e.getAttendees()) {
//            b.getEventsRegistered().remove(e.getEventId());
//        }
        // TODO: deleteEvents will delete EventID from userAccount first
        db.deleteEventFromDB(eventId);
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
            attendees[i] = a.getAttendeeName();
            i++;
        }
        return attendees;
    }
}


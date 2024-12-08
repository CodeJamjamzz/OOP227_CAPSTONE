package com.example.capstone_project.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
import com.example.capstone_project.RegisterAttendee;
import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.models.Event;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

// Singleton
public class EventServiceManager {
    private String TAG = "EventServiceManager";
    private static EventServiceManager instance;
    // DONE: make Comparator for Event to sort by start date
    private static List<Event> events;
    private static final RegItFirebaseController db = RegItFirebaseController.getInstance();

    private EventServiceManager() {
        // Load events from database here ig
        updateEvents();
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

    public static void updateEvents() {
        events = db.getEventsfromDB();
        sortEvents();
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

    public CompletableFuture<Boolean> registerAttendee(String eventId, Attendee newAttendee) {
        return db.addNewAttendee(eventId, newAttendee);
    }

    public void unRegisterAttendee(String eventId, String attendeeId) {
        Event event = getEventFromId(eventId);

        if (event == null) {
            Log.d(TAG, "Event was not found!");
            return;
        }

        db.removeAttendeeFromEvent(eventId, attendeeId);
        db.removeEventFromUser(eventId, attendeeId);
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
        // TODO: turn to hashmap
//        for (Attendee a : getEventFromId(eventId).getAttendees()) {
//            // TODO: connect with firebase
//            if (a.getUserAccountID().equals(attendeeId)) {
//                return a;
//            }
//        }
        return null;
    }

    public String getAttendeeIdFromName(String eventId, String attendeeName) {
        List<Attendee>  attendList = RegItFirebaseController.getInstance().getAttendeesFromEvent(eventId);
        for (Attendee a : attendList) {
            Log.d("checking attendee", "Attendee "+ a.getUserAccountName()  + " with ID " + a.getUserAccountID());
            if (a.getUserAccountName().equals(attendeeName)) {
                return a.getUserAccountID();
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
        // TODO: turn to hashmap
//        Map<String, Attendee> map = event.getAttendees();
//        for (Map.Entry<String, Attendee> entry : map.entrySet()) {
//            attendees[i] = entry.getValue().getUserAccountName();
//        }

//        for (Attendee b : event.getAttendees()) {
//            // TODO: add firebase connections in Attendee getters
//            if (attendeeId.equals(b.getUserAccountID())) {
//                return true;
//            }
//        }
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
        db.deleteEventFromDB(eventId);
        events.remove(e);
    }

    public Task<String[]> getAttendeeNames(String eventId) {
        DatabaseReference eventReference = RegItFirebaseController.getInstance().getRegItEventsListDB().child(eventId);

        return eventReference.get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        Event event = task.getResult().getValue(Event.class);
                        if (event != null) {
                            List<String> attendeeNames = new ArrayList<>();
                            for (Attendee attendee : event.getAttendees().values()) {
                                attendeeNames.add(attendee.getUserAccountName());
                            }
                            return Tasks.forResult(attendeeNames.toArray(new String[0]));
                        } else {
                            throw new RuntimeException("Event not found");
                        }
                    } else {
                        throw Objects.requireNonNull(task.getException());
                    }
                });
    }
}


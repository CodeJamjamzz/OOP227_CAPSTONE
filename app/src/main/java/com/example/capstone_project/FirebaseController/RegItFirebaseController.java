package com.example.capstone_project.FirebaseController;


import static com.example.capstone_project.utils.PasswordEncryptor.checkPassword;
import static com.example.capstone_project.utils.PasswordEncryptor.hashPassword;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.models.Event;
import com.example.capstone_project.models.UserAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

// Singleton
public class RegItFirebaseController {

    private static RegItFirebaseController instance;
    private FirebaseDatabase database;
    private final DatabaseReference regItEventsListDB;
    private final DatabaseReference regItUserAccountListDB;

    private RegItFirebaseController() {
        FirebaseDatabase regItFirebaseDatabase = FirebaseDatabase.getInstance();
        regItEventsListDB = regItFirebaseDatabase.getReference("RegItEventListDatabaseSubtreeNode");
        regItUserAccountListDB = regItFirebaseDatabase.getReference("RegItUserAccountListDatabaseSubtreeNode");
    }

    public static RegItFirebaseController getInstance() {
        if (instance == null) {
            instance = new RegItFirebaseController();
        }
        return instance;
    }

    public DatabaseReference getRegItEventsListDB() { return regItEventsListDB; }

    public DatabaseReference getRegItUserAccountListDB() { return regItUserAccountListDB; }


    /* ================================ CREATION METHODS ================================ */

    // Firebase Account Creation Method
    public void createNewUser(String StudentNumber, String name, String email, String courseYear, String password) {

        // probably just implement firebase Authenticator for 2.0
        String hashedPassword = hashPassword(password);
        // creates a UserObject
        UserAccount user = new UserAccount(StudentNumber, name, email, courseYear, hashedPassword);

        // passes the data in the userObject to JSON
        regItUserAccountListDB.child(StudentNumber).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // User created successfully
                Log.d("User  created", "User  created successfully.");
            } else {
                // Handle failure
                Log.w("User  Created", "User  creation failed", task.getException());
            }
        });

    }

    // Firebase Event creation method
    public void createNewEvent(Event event) {
        regItEventsListDB.child(event.getEventId()).setValue(event).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // User created successfully
                Log.d("Event  created", "Event  created successfully.");
            } else {
                // Handle failure
                Log.w("Event  Created", "Event  creation failed", task.getException());
            }
        });
    }

    public CompletableFuture<Boolean> addNewAttendee(String eventID, Attendee attendee) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        DatabaseReference attendeesTree = regItEventsListDB.child(eventID).child("attendees");

        attendeesTree.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean attendeeExists = false;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Attendee existingAttendee = childSnapshot.getValue(Attendee.class);
                    assert existingAttendee != null;
                    if (existingAttendee.getUserAccountID().equals(attendee.getUserAccountID())) {
                        attendeeExists = true;
                        break;
                    }
                }

                if (!attendeeExists) {
                    attendeesTree.child(attendee.getUserAccountID()).setValue(attendee)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    future.complete(true);
                                    Log.d("Attendee added", "Attendee added successfully.");
                                } else {
                                    future.completeExceptionally(task.getException());
                                    Log.w("Attendee added", "Attendee added failed", task.getException());
                                }
                            });
                } else {
                    future.complete(false);
                    Log.d("Attendee Exists", "Attendee already exists.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(error.toException());
                Log.w("Attendee Added", "Attendee added failed", error.toException());
            }
        });

        return future;
    }

    public void addEventToUserAccount(String eventID, String studentNumber) {
        DatabaseReference curUser= regItUserAccountListDB.child(studentNumber).child("accountEventsAttending");
        curUser.child(eventID).setValue(eventID).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // User created successfully
                Log.d("Event  added", "User has joined the event");
            } else {
                // Handle failure
                Log.w("Event  added", "User failed to join the event", task.getException());
            }
        });
    }

    /* ================================ ACCESS METHODS ================================ */

    // Firebase Account Access Method
    private CompletableFuture<UserAccount> fetchUserFromSource(String studentNumber, String password) {
        CompletableFuture<UserAccount> future = new CompletableFuture<>();

        DatabaseReference userReference = regItUserAccountListDB.child(studentNumber);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                    assert userAccount != null;
                    if (checkPassword(password, userAccount.getAccountPassword())) {
                        future.complete(userAccount);
                    } else {
                        future.completeExceptionally(new Exception("Incorrect password"));
                    }
                } else {
                    future.completeExceptionally(new Exception("User not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    public CompletableFuture<UserAccount> getUser(String studentNumber, String password) {
        return fetchUserFromSource(studentNumber, password);
    }

    // Firebase Event Access Method
    private CompletableFuture<Event> fetchEventFromSource(String eventID) {
        CompletableFuture<Event> future = new CompletableFuture<>();

        DatabaseReference eventReference = regItEventsListDB.child(eventID);

        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event curEvent = dataSnapshot.getValue(Event.class);
                    assert curEvent != null;
                    future.complete(curEvent);
                } else {
                    future.completeExceptionally(new Exception("Event does not exist"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    public CompletableFuture<Event> getEvent(String eventID) {
        return fetchEventFromSource(eventID);
    }

    public List<Event> getEventsfromDB() {
        List<Event> eventList = new ArrayList<>();
        regItEventsListDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Event user = userSnapshot.getValue(Event.class);
                        // Add user to a list or perform other actions
                        eventList.add(user);
                    }
                } else {
                    // Handle the case where no users exist
                    Log.d("Firebase", "No users found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch users: " + error.getMessage());
            }
        });
        return eventList;
    }

    public List<Attendee> getAttendeesFromEvent(String eventID) {
        List<Attendee> eventList = new ArrayList<>();
        Log.d("Firebase ", "Getting attendees from event " + eventID);
        regItEventsListDB.child(eventID).child("attendees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Attendee user = userSnapshot.getValue(Attendee.class);
                        // Add user to a list or perform other actions
                        assert user != null;
                        Log.d("Fetch User", "Attendee fetched with ID " + user.getUserAccountID());
                        eventList.add(user);
                    }
                } else {
                    // Handle the case where no users exist
                    Log.d("Firebase", "No attendees found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch attendees: " + error.getMessage());
            }
        });
        return eventList;
    }

    /** Important Note on How to Use the Completable Future

    CompletableFuture<UserAccount> userFuture = db.getUser(studentID);
    userFuture.thenAccept(userAccount -> {
        // Error checking is already done in the method so just use the new userAccount object here
    }).exceptionally(e -> {
        Log.e("User Data", "Error fetching user: " + e.getMessage());
        return null;
    });

     */

    /* ================================ DELETIONS METHODS ================================ */

    // Event Deletion Method
    public void deleteEventFromDB(String eventId) {
        DatabaseReference eventRef = regItEventsListDB.child(eventId);

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        for (Attendee attendee : event.getAttendees().values()) {
                            removeEventFromUser(eventId, attendee.getUserAccountID());
                        }

                        eventRef.removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("Firebase", "Event and attendees deleted successfully");
                            } else {
                                Log.e("Firebase", "Error deleting event or attendees: " + Objects.requireNonNull(task.getException()).getMessage());
                            }
                        });
                    } else {
                        Log.w("Firebase", "Event not found");
                    }
                } else {
                    Log.w("Firebase", "Event not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Error fetching event data: " + error.getMessage());
            }
        });
    }

    // Remove Attendee From Event
    public void removeAttendeeFromEvent(String eventId, String attendeeId) {
        DatabaseReference eventRef = regItEventsListDB.child(eventId).child("attendees").child(attendeeId);

        eventRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Attendee Removed", "Attendee removed successfully");
            } else {
                Log.w("Attendee Removed", "Failed to remove attendee", task.getException());
            }
        });


    }

    public void removeEventFromUser(String eventId, String attendeeId) {
        Log.d("Removing event", "Event with id " + eventId + " removed from user id " + attendeeId);
        DatabaseReference eventRef = regItUserAccountListDB.child(attendeeId).child("accountEventsAttending").child(eventId);
        eventRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Removal", "Event removed successfully");
            } else {
                Log.w("Removal", "Failed to remove event", task.getException());
            }
        });
    }

    public CompletableFuture<Boolean> verifyAttendee(String eventId, Attendee attendee) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        DatabaseReference eventRef = regItEventsListDB.child(eventId).child("attendees");

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {
                        Attendee existingAttendee = attendeeSnapshot.getValue(Attendee.class);
                        if (existingAttendee != null && existingAttendee.getUserAccountID().equals(attendee.getUserAccountID())) {
                            // Update the attendee's confirmation status
                            existingAttendee.setUserConfirm(true);
                            attendeeSnapshot.getRef().setValue(existingAttendee)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            future.complete(true); // Attendee confirmed and updated
                                        } else {
                                            future.completeExceptionally(task.getException());
                                        }
                                    });
                            return;
                        }
                    }
                }
                future.complete(false); // Attendee not found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

}

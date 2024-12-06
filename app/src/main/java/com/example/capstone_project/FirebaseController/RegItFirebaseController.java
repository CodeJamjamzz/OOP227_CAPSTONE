package com.example.capstone_project.FirebaseController;


import static com.example.capstone_project.utils.PasswordEncryptor.checkPassword;
import static com.example.capstone_project.utils.PasswordEncryptor.hashPassword;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.models.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    // Firebase Account Creation Method
    // Technically I can disallow account creation here if account already exists
    public void createNewUser(String StudentNumber, String name, String email, String courseYear, String password) {

        // TODO: probably just implement firebase Authenticator
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

    private CompletableFuture<UserAccount> fetchUserFromSource(String studentNumber) {
        CompletableFuture<UserAccount> future = new CompletableFuture<>();

        DatabaseReference userReference = regItUserAccountListDB.child(studentNumber);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                    assert userAccount != null;
                    future.complete(userAccount);
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

    /* Use the user obtained within here

    CompletableFuture<UserAccount> userFuture = db.getUser("23-2772-181");
    userFuture.thenAccept(userAccount -> {
        // Error checking is already done in the method so just use the new userAccount object here
    }).exceptionally(e -> {
        Log.e("User Data", "Error fetching user: " + e.getMessage());
        return null;
    });

     */

    // Firebase get data Method
    /*   USAGE
    fetchData("23-2772-181").thenAccept(accountName -> {
        System.out.println("Account Name: " + accountName);
    }).exceptionally(e -> {
        System.err.println("Error fetching account name: " + e.getMessage());
    });
     */

    // Event Deletion Method
    public void deleteEventFromDB(String eventId) {
        //TODO: remove Attendee from DB before deleting event

        DatabaseReference eventRef = regItEventsListDB.child(eventId);
        eventRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Firebase", "Event deleted successfully");
                    // Handle successful deletion, e.g., update UI or show a message
                } else {
                    Log.e("Firebase", "Error deleting event: " + Objects.requireNonNull(task.getException()).getMessage());
                    // Handle the error, e.g., show an error message to the user
                }
            }
        });
    }

    // Firebase Add Attendee Method

    // Firebase Remove Attendee Method

    // Firebase Edit Attendee Method

}

package com.example.capstone_project.FirebaseController;


import static com.example.capstone_project.utils.PasswordEncryptor.checkPassword;
import static com.example.capstone_project.utils.PasswordEncryptor.hashPassword;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.models.UserAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


public class RegItFirebaseController {


    private final DatabaseReference regItUserAccountListDB;
    private final DatabaseReference regItEventsListDB;

    public RegItFirebaseController() {
        // create only one instance of the firebase database
        FirebaseDatabase regItFirebaseDatabase = FirebaseDatabase.getInstance("DB_URL");
        regItEventsListDB = regItFirebaseDatabase.getReference("EV_DB");
        regItUserAccountListDB = regItFirebaseDatabase.getReference("UA_DB");
    }

    // Firebase Account Creation Method
    public void createNewUser(String StudentNumber, String name, String email, String courseYear, String password) {

        // TODO: proly just implement firebase Authenticator
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

    public CompletableFuture<UserAccount> getUser(String studentNumber, String password) {
        return fetchUserFromSource(studentNumber, password);
    }

    /* HOW TO USE THE GET USER / can also be used in the getData for specific data
    CompletableFuture<UserAccount> userFuture = db.getUser("23-2772-181");
    userFuture.thenAccept(userAccount -> {
        if (userAccount != null) {
            Log.d("User Data", "User found: " + userAccount.toString());
        } else {
            Log.d("User Data", "User not found");
        }
    }).exceptionally(e -> {
        Log.e("User Data", "Error fetching user: " + e.getMessage());
        return null;
    });
     */
        // TODO: getting specific data
//    public CompletableFuture<String> fetchData(String studentNumber) {
//        return getUser(studentNumber).thenApply(userAccount -> {
//            if (userAccount != null) {
//                return userAccount.getAccountName();
//            } else {
//                return "User not found";
//            }
//        });
//    }

    // Firebase get data Method
    /*   USAGE
    fetchData("23-2772-181").thenAccept(accountName -> {
        System.out.println("Account Name: " + accountName);
    }).exceptionally(e -> {
        System.err.println("Error fetching account name: " + e.getMessage());
    });
     */


    // Firebase Store Event Method



    // Firebase Event Access Method

    // Firebase Add Attendee Method

    // Firebase Remove Attendee Method

    // Firebase Edit Attendee Method

}

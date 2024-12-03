package com.example.capstone_project.firebaseController;


import static com.example.capstone_project.utils.PasswordEncryptor.hashPassword;

import android.util.Log;

import androidx.annotation.NonNull;

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

// TODO: Here are the List of Stuff I need to add in the Firebase DB itself
// improve realtime database security rule ex:

/*
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    }
  }
}

Additional Security Considerations:

Password Hashing: Always hash and salt passwords before storing them in the database.
Data Validation: Validate user input to prevent malicious data injection.
Network Security: Use HTTPS to encrypt communication between your app and the Firebase servers.
Authentication: Implement strong authentication mechanisms to protect user accounts.

*/
// Disallow account creation if node already exists (reason: im too sleepy let me think again tom)



public class RegItFirebaseController {


    private final DatabaseReference regItUserAccountListDB;
    private final DatabaseReference regItEventsListDB;

    public RegItFirebaseController() {
        // create only one instance of the firebase database
        FirebaseDatabase regItFirebaseDatabase = FirebaseDatabase.getInstance("https://oop227capstone-default-rtdb.asia-southeast1.firebasedatabase.app/");
        regItEventsListDB = regItFirebaseDatabase.getReference("RegItEventListDatabaseSubtreeNode");
        regItUserAccountListDB = regItFirebaseDatabase.getReference("RegItUserAccountListDatabaseSubtreeNode");
    }

    // Firebase Account Creation Method
    public void createNewUser(String StudentNumber, String name, String email, String courseYear, String password) {

        // TODO: fix hashPassword since it keeps changing the PW, try Argon2
        String hashedPassword = hashPassword(password);
        // creates a UserObject
        UserAccount user = new UserAccount(StudentNumber, name, email, courseYear, password);

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

    // Firebase Account Access Method
    // TODO: Add password matching before getting user
    private CompletableFuture<UserAccount> fetchUserFromSource(String studentNumber) {
        CompletableFuture<UserAccount> future = new CompletableFuture<>();

        DatabaseReference userReference = regItUserAccountListDB.child(studentNumber);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                future.complete(userAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    public CompletableFuture<UserAccount> getUser(String studentNumber) {
        return fetchUserFromSource(studentNumber);
    }

    /* HOW TO USE THE GET USER / can also be used in the getData for specific data
    CompletableFuture<UserAccount> userFuture = db.getUser("23-2772-181");
        userFuture.thenAccept(userAccount -> {
            if (userAccount != null) {
                // You can store the userAccount in a variable or pass it to other methods
                UserAccount retrievedUser = userAccount;
            } else {
                Log.d("User Data", "User not found");
            }
        }).exceptionally(e -> {
            System.err.println("Error fetching user: " + e.getMessage());
            return null;
        });
     */

    public CompletableFuture<String> fetchData(String studentNumber) {
        return getUser(studentNumber).thenApply(userAccount -> {
            if (userAccount != null) {
                return userAccount.getAccountName();
            } else {
                return "User not found";
            }
        });
    }
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

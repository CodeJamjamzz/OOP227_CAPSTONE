package com.example.capstone_project.FirebaseController;


//import android.util.Log;

//import androidx.annotation.NonNull;

import com.example.capstone_project.models.UserAccount;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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

    // TODO: sensitive information, please remove before committing
    public RegItFirebaseController() {
        // create only one instance of the firebase database
        FirebaseDatabase regItFirebaseDatabase = FirebaseDatabase.getInstance("DB_URL");
        regItUserAccountListDB = regItFirebaseDatabase.getReference("AC_DB");
        regItEventsListDB = regItFirebaseDatabase.getReference("EV_DB");
    }

    // Firebase Account Creation Method
    public void createNewUser(String StudentNumber, String name, String email, String password) {
        // TODO: Hash the password for security
        // String hashedPassword = hashPassword(password);

        // creates a UserObject
        UserAccount user = new UserAccount(StudentNumber, name, email, password /* hashedPassword */);

        // passes the data in the userObject to JSON
        regItUserAccountListDB.child(StudentNumber).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // User created successfully
                System.out.println("User  created successfully.");
            } else {
                // Handle failure
                System.err.println("Failed to create user: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });


    }

    // Firebase Account Access Method
    // TODO: Add password matching before returning user
    // Review this tomorrow i don't understand anything
    // TODO: uncomment it out before working on it
    /*
    public void getUser(String StudentNumber, String password) {

        // TODO: add the make it listen to a specific node cuz im sleepy doing tomorrow
        ValueEventListener userAccountListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount currentUser = snapshot.getValue(UserAccount.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO: search what the fuck this does
                Log.w("User not Found error", error.toException());
            }
        };

    }
    */

}

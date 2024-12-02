package com.example.capstone_project.firebaseController;


import static com.example.capstone_project.utils.PasswordEncryptor.hashPassword;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.capstone_project.models.UserAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public RegItFirebaseController() {
        // create only one instance of the firebase database
        FirebaseDatabase regItFirebaseDatabase = FirebaseDatabase.getInstance("DB_URL");
        regItUserAccountListDB = regItFirebaseDatabase.getReference("AC_DB");
        regItEventsListDB = regItFirebaseDatabase.getReference("EV_DB");
    }

    // Firebase Account Creation Method
    public void createNewUser(String StudentNumber, String name, String email, String password) {
        String hashedPassword = hashPassword(password);

        // creates a UserObject
        UserAccount user = new UserAccount(StudentNumber, name, email, hashedPassword);

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
    // TODO: Add password matching before getting user
    public void getUser(String StudentNumber) {
        DatabaseReference curAccount = regItUserAccountListDB.child(StudentNumber);

        curAccount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserAccount userData = snapshot.getValue(UserAccount.class);
                    if (userData != null) {
                        // Successfully retrieved the user data
                        Log.d("User  Data", userData.toString());
                        // Add UI logic to display the user data
                    } else {
                        Log.d("User  Data", "User  data is null");
                    }
                } else {
                    Log.d("User  Data", "No user found with the given ID");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.w("User  Data", "loadUser :onCancelled", error.toException());
            }
        });
    }

    // Firebase get specific Data Method
    /**
     * @param
     */
    public <T> void getData(String StudentID, String key, final DataCallback<T> callback, Class<T> clazz) {
        regItUserAccountListDB.child(StudentID).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Use the provided class type to get the value
                    T data = dataSnapshot.getValue(clazz);
                    callback.onSuccess(data);
                } else {
                    callback.onFailure("No data found for the specified key.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    // Callback interface for data retrieval
    public interface DataCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }


    // Firebase Store Event Method



    // Firebase Event Access Method

    // Firebase Add Attendee Method

    // Firebase Remove Attendee Method

    // Firebase Edit Attendee Method

}

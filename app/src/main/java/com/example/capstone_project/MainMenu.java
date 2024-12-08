package com.example.capstone_project;

import static com.example.capstone_project.utils.PasswordEncryptor.checkPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
import com.example.capstone_project.models.Event;
import com.example.capstone_project.models.UserAccount;
import com.example.capstone_project.utils.EventServiceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone_project.utils.InputValidator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CompletableFuture;

import java.util.concurrent.atomic.AtomicReference;

// java code for activity_main.xml screen the first one
public class MainMenu extends AppCompatActivity {
    // opens activity_main.xml
    TextView createAccount;
    TextView adminDashboard;
    RegItFirebaseController db = RegItFirebaseController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.studentnumber), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EventServiceManager.updateEvents();
        createAccount = findViewById(R.id.createAccount);
        adminDashboard = findViewById(R.id.adminDashboard);

        /*
            TODO: load events from database here using EventServiceManager
            Hardcoded objects will be here for testing
        */
        /*
        Attendee testPerson1 = new Attendee("Ewican, James O.", "23-4496-954", "james.ewican@cit.edu", "BSCS - 2");
        Attendee testPerson2 = new Attendee("Pinca, Jamiel Kyne R.", "23-4205-826", "jamiel.pinca@cit.edu", "BSCS - 2");
        Attendee testPerson3 = new Attendee("Galorio, Sydney B.", "23-4105-856", "sydney.galorio@cit.edu", "BSCS - 2");
        LocalDateTime testStart = LocalDateTime.now().plusDays(1);
        LocalDateTime testEnd = LocalDateTime.now().plusDays(3);
//        Moved loading of events in AdminDashboard

        String id1 = EventServiceManager.getInstance().createEvent("CCS Akwe", "Find new friends!", "CIT-U Gym", testStart, testEnd, 0);
        String id2 = EventServiceManager.getInstance().createEvent("Food Bazaar", "Try our delicious meals!", "CIT-U RTL Quadrangle", testStart, testEnd, 0);
        EventServiceManager.getInstance().registerAttendee(id1, testPerson1);
        EventServiceManager.getInstance().registerAttendee(id2, testPerson2);
        EventServiceManager.getInstance().registerAttendee(id1, testPerson3);
        */
    }

    boolean isValid = false;
    public void RealTimeValidate(EditText text){
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    text.setError(null);
                    isValid = false; // or reset as needed based on your logic
                    return;
                }
                isValid = InputValidator.isValidStudentNumber(input);
                if (isValid) {
                    text.setError(null);
                } else {
                    text.setError("Invalid student number. Use the format xx-xxxx-xxx.");
                }
            }
        });
    }
    // Method to show the popup dialog
    public void showStudentNumberPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Student Number");
        // layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20); // padding
        // field for the student number
        final EditText input = new EditText(this);
        input.setHint("e.g. 22-2097-673");
        // centering
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 60); // padding
        input.setLayoutParams(params);
        // adding to layout
        layout.addView(input);
        // adding to popup
        builder.setView(layout);
        // validating sn
        RealTimeValidate(input);
        // confirm behavior
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String studentNumber = input.getText().toString().trim();
            if (isValid) {
                checkIfStudentNumberExists(studentNumber);
            } else {
                Toast.makeText(MainMenu.this, "Please enter a valid student number", Toast.LENGTH_SHORT).show();
            }
        });
        // cancel behavior
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        // Show the popup
        builder.show();
    }

    // Method to check if the student number exists in Firebase
    private void checkIfStudentNumberExists(String studentNumber) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                .getReference("RegItUserAccountListDatabaseSubtreeNode");

        databaseRef.child(studentNumber).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Student number exists, ask for pass
                    showPasswordPopup(studentNumber);
                } else {
                    // Student number not found, navigate to CreateAccount activity
                    Intent intent = new Intent(MainMenu.this, CreateAccount.class);
                    intent.putExtra("studentNumber", studentNumber);
                    startActivity(intent);
                }
            } else {
                // Handle potential errors
                Toast.makeText(MainMenu.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showPasswordPopup(String studentNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        // Layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20); // padding

        // Password field
        final EditText input = new EditText(this);
        input.setHint("Enter your password");
        input.setTransformationMethod(PasswordTransformationMethod.getInstance()); // to hide password
        // Centring
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 60); // padding
        input.setLayoutParams(params);

        // Adding to layout
        layout.addView(input);
        // Adding to popup
        builder.setView(layout);

        // Confirm behavior
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String password = input.getText().toString().trim();
            if (!password.isEmpty()) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                        .getReference("RegItUserAccountListDatabaseSubtreeNode");

                databaseRef.child(studentNumber).child("accountPassword").get().addOnCompleteListener(task -> {
                    Intent CreateQrCode = new Intent(MainMenu.this, CreateQRCode.class);
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            String storedPassword = task.getResult().getValue(String.class);
                            if(checkPassword(password, storedPassword)){
                                goToQRCode(CreateQrCode, studentNumber, password);
                            }else{
                                Toast.makeText(MainMenu.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Handle potential errors
                        Toast.makeText(MainMenu.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MainMenu.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel behavior
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the popup
        builder.show();
    }
    public void goToQRCode(Intent nextActivity,String studentNumber, String password){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                .getReference("RegItUserAccountListDatabaseSubtreeNode");
        databaseRef.child(studentNumber).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // Retrieve the fields from the Firebase snapshot
                String accountName = task.getResult().child("accountName").getValue(String.class);
                String accountCourseYear = task.getResult().child("accountCourseYear").getValue(String.class);
                String accountEmail = task.getResult().child("accountEmail").getValue(String.class);
                String accountID = task.getResult().child("accountID").getValue(String.class);

                // Put the data as extras in the Intent
                nextActivity.putExtra("InputedName", accountName);
                nextActivity.putExtra("InputedStudentNumber", accountID);
                nextActivity.putExtra("InputedEmail", accountEmail);
                nextActivity.putExtra("InputedCourseYear", accountCourseYear);
                nextActivity.putExtra("Password", password);
                // Start the next activity
                startActivity(nextActivity);
            } else {
                // Handle potential errors or the case where the student number doesn't exist
                Toast.makeText(MainMenu.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void createAccountActivity(View view){
        showStudentNumberPopup();
    }

    public void adminDashboardActivity(View view) {
        startActivity(new Intent(MainMenu.this, AdminDashboard.class));
    }
}
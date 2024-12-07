package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.utils.EventServiceManager;
import com.example.capstone_project.utils.InputValidator;

import java.time.LocalDateTime;

// java code for activity_main.xml screen the first one
public class MainMenu extends AppCompatActivity {
    // opens activity_main.xml
    TextView createAccount;
    TextView adminDashboard;
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

        createAccount = findViewById(R.id.createAccount);
        adminDashboard = findViewById(R.id.adminDashboard);

        /*
            TODO: load events from database here using EventServiceManager
            Hardcoded objects will be here for testing
        */
        Attendee testPerson1 = new Attendee("Ewican, James O.", "23-4496-954", "james.ewican@cit.edu", "BSCS - 2");
        Attendee testPerson2 = new Attendee("Pinca, Jamiel Kyne R.", "23-4205-826", "jamiel.pinca@cit.edu", "BSCS - 2");
        Attendee testPerson3 = new Attendee("Galorio, Sydney B.", "23-4105-856", "sydney.galorio@cit.edu", "BSCS - 2");
        LocalDateTime testStart = LocalDateTime.now().plusDays(1);
        LocalDateTime testEnd = LocalDateTime.now().plusDays(3);

        String id1 = EventServiceManager.getInstance().createEvent("CCS Akwe", "Find new friends!", "CIT-U Gym", testStart, testEnd, 0);
        String id2 = EventServiceManager.getInstance().createEvent("Food Bazaar", "Try our delicious meals!", "CIT-U RTL Quadrangle", testStart, testEnd, 0);
        EventServiceManager.getInstance().registerAttendee(id1, testPerson1);
        EventServiceManager.getInstance().registerAttendee(id2, testPerson2);
        EventServiceManager.getInstance().registerAttendee(id1, testPerson3);
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
                // TODO: Add if statement to check if it is in firebase
                // if in firebase go straight to CreateQRCode activity
                // else go to CreateAccount activity
                startActivity(new Intent(MainMenu.this, CreateAccount.class));
            } else {
                Toast.makeText(MainMenu.this, "Please enter a valid student number", Toast.LENGTH_SHORT).show();
            }
        });
        // cancel behavior
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        // Show the popup
        builder.show();
    }
    public void createAccountActivity(View view){
        showStudentNumberPopup();
    }

    public void adminDashboardActivity(View view) {
        startActivity(new Intent(MainMenu.this, AdminDashboard.class));
    }
}
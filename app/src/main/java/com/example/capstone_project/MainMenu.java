package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.utils.EventServiceManager;

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

    public void createAccountActivity(View view){
        startActivity(new Intent(MainMenu.this, CreateAccount.class));
    }

    public void adminDashboardActivity(View view) {
        startActivity(new Intent(MainMenu.this, AdminDashboard.class));
    }
}
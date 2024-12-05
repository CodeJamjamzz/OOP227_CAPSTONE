package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone_project.utils.EventServiceManager;

import java.time.LocalDateTime;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
import com.example.capstone_project.models.UserAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;

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

        // TODO: load events from database here using EventServiceManager
        LocalDateTime testStart = LocalDateTime.now().plusDays(1);
        LocalDateTime testEnd = LocalDateTime.now().plusDays(3);
        EventServiceManager.getInstance().createEvent("CCS Akwe", "Find new friends!", "CIT-U Gym", testStart, testEnd, 0);



        // firebase testing wwa

    }

    public void createAccountActivity(View view){
        startActivity(new Intent(MainMenu.this, CreateAccount.class));
    }

    public void adminDashboardActivity(View view) {
        startActivity(new Intent(MainMenu.this, AdminDashboard.class));
    }
}
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

//        Moved loading of events in AdminDashboard

    }

    public void createAccountActivity(View view){
        startActivity(new Intent(MainMenu.this, CreateAccount.class));
    }

    public void adminDashboardActivity(View view) {
        startActivity(new Intent(MainMenu.this, AdminDashboard.class));
    }
}
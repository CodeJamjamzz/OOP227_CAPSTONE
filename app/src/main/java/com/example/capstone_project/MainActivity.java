package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// java code for activity_main.xml screen the first one
public class MainActivity extends AppCompatActivity {
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

        // https://firebase.google.com/docs/database/android/start#java_1
        /* test firebase connectivity
        FirebaseDatabase database = FirebaseDatabase.getInstance("database-link");
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");
        */
    }

    public void createAccountActivity(View view){
        startActivity(new Intent(MainActivity.this, CreateAccount.class));
    }

    public void adminDashboardActivity(View view) {
        startActivity(new Intent(MainActivity.this, AdminDashboard.class));
    }
}
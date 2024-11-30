package com.example.capstone_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.UpcomingEventAdapter;

import java.time.LocalDateTime;

public class AdminDashboard extends AppCompatActivity {
    UpcomingEventAdapter upcomingEventAdapter;
    RecyclerView.LayoutManager layoutManager;
    CardView latestEvent;

    LocalDateTime testStart = LocalDateTime.now().plusDays(1);
    LocalDateTime testEnd = LocalDateTime.now().plusDays(3);

    Event event1 = new Event("CSS Tutorials", testStart, testEnd, "CIT-U", 0,"Master the Java language and code your way to success!", "CCS", 0.0);
    Event event2 = new Event("CCS Akwe", testStart, testEnd, "CIT-U", 0,"Find new friends!", "CCS", 249.0);
    Event event3 = new Event("Founder's Day", testStart, testEnd, "CIT-U", 0, "Honor our origins.", "General", 0.0);
    Event event4 = new Event("Final Examination", testStart, testEnd, "CIT-U", 0, "It's the final countdown", "General", 0);

    Event[] events = new Event[]{event1, event2, event3, event4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        RecyclerView recyclerView = findViewById(R.id.upcoming_events_cardList);

        upcomingEventAdapter = new UpcomingEventAdapter(events);
        layoutManager = new LinearLayoutManager(this);
        latestEvent = findViewById(R.id.latestEvent);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(upcomingEventAdapter);
    }
}



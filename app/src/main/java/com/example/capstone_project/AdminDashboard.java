package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.EventServiceManager;
import com.example.capstone_project.utils.UpcomingEventAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminDashboard extends AppCompatActivity {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    private UpcomingEventAdapter upcomingEventAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView latestEventTitle;
    private TextView latestEventDescription;
    private TextView latestEventStartDate;
    private FloatingActionButton addEvent;

    private LocalDateTime testStart = LocalDateTime.now().plusDays(1);
    private LocalDateTime testEnd = LocalDateTime.now().plusDays(3);

//    Event event1 = new Event("CSS Tutorials", testStart, testEnd, "CIT-U", 0,"Master the Java language and code your way to success!", "CCS", 0.0);
//    Event event2 = new Event("CCS Akwe", testStart, testEnd, "CIT-U", 0,"Find new friends!", "CCS", 249.0);
//    Event event3 = new Event("Founder's Day", testStart, testEnd, "CIT-U", 0, "Honor our origins.", "General", 0.0);
//    Event event4 = new Event("Final Examination", testStart, testEnd, "CIT-U", 0, "It's the final countdown", "General", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        RecyclerView recyclerView = findViewById(R.id.upcoming_events_cardList);

        // Hardcoded events, to be changed
        Event[] events = EventServiceManager.getInstance().getEvents();

        upcomingEventAdapter = new UpcomingEventAdapter(events);
        layoutManager = new LinearLayoutManager(this);
        latestEventTitle = findViewById(R.id.latestEventTitle);
        latestEventDescription = findViewById(R.id.latestEventDescription);
        addEvent = findViewById(R.id.addEvent);

        if (events.length == 0) {
            // no events view
        } else if (events.length == 1) {
            latestEventTitle.setText(events[0].getName());
            latestEventDescription.setText(events[0].getDescription());
            latestEventStartDate.setText(events[0].getStartDate().format(dateTimeFormatter));
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(upcomingEventAdapter);

        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventForms.class);
            v.getContext().startActivity(intent);
        });
    }
}



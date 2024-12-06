package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.EventBuilder;
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
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        RecyclerView recyclerView = findViewById(R.id.upcoming_events_cardList);

        // Existing events are loaded here
        Event[] events = EventServiceManager.getInstance().getEvents();

        UpcomingEventAdapter upcomingEventAdapter = new UpcomingEventAdapter(events);
        layoutManager = new LinearLayoutManager(this);
        CardView latestEvent = findViewById(R.id.latestEvent);
        TextView latestEventTitle = findViewById(R.id.latestEventTitle);
        TextView latestEventDescription = findViewById(R.id.latestEventDescription);
        TextView latestEventStartDate = findViewById(R.id.latestEventStartDate);
        TextView noEventsText = findViewById(R.id.noEventsText);
        TextView noUpcomingEventText = findViewById(R.id.noUpcomingEventText);
        FloatingActionButton addEvent = findViewById(R.id.addEvent);

        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventForms.class);
            v.getContext().startActivity(intent);
        });

        latestEvent.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventDetails.class);
            intent.putExtra("SELECTED_EVENT", events[0]);
            v.getContext().startActivity(intent);
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(upcomingEventAdapter);

        if (events.length == 0) {
            latestEvent.setVisibility(View.GONE);
            noEventsText.setVisibility(View.VISIBLE);
            noUpcomingEventText.setVisibility(View.VISIBLE);
            return;  // early return to avoid IndexOutOfBoundsException
        } else if (events.length == 1) {
            latestEvent.setVisibility(View.VISIBLE);
            noEventsText.setVisibility(View.GONE);
        } else {
            latestEvent.setVisibility(View.VISIBLE);
            noUpcomingEventText.setVisibility(View.GONE);
        }

        latestEventTitle.setText(events[0].getName());
        latestEventDescription.setText(events[0].getDescription());
        latestEventStartDate.setText(events[0].getStartDate().format(dateTimeFormatter));
    }
}



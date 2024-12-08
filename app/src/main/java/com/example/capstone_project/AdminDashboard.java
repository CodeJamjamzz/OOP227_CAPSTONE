package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.EventServiceManager;
import com.example.capstone_project.utils.UpcomingEventAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AdminDashboard extends AppCompatActivity {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    private RecyclerView.LayoutManager layoutManager;
    private CardView latestEvent;
    private TextView latestEventTitle;
    private TextView latestEventDescription;
    private TextView latestEventStartDate;
    private TextView latestEventEndDate;
    private TextView latestEventAttendeesRegistered;
    private TextView latestEventRemainingSlots;
    private TextView latestEventTotalRevenue;
    private TextView noEventsText;
    private TextView noUpcomingEventText;
    private RecyclerView recyclerView;
    private Event[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        recyclerView = findViewById(R.id.upcoming_events_cardList);

        layoutManager = new LinearLayoutManager(this);
        latestEvent = findViewById(R.id.latestEvent);
        latestEventTitle = findViewById(R.id.latestEventTitle);
        latestEventDescription = findViewById(R.id.latestEventDescription);
        latestEventStartDate = findViewById(R.id.latestEventStartDate);
        latestEventEndDate = findViewById(R.id.latestEventEndDate);
        latestEventAttendeesRegistered = findViewById(R.id.adminDashboard_attendeesRegistered);
        latestEventRemainingSlots = findViewById(R.id.adminDashboard_remainingSlots);
        latestEventTotalRevenue = findViewById(R.id.adminDashboard_totalRevenue);
        noEventsText = findViewById(R.id.noEventsText);
        noUpcomingEventText = findViewById(R.id.noUpcomingEventText);
        FloatingActionButton addEvent = findViewById(R.id.addEvent);
        events = EventServiceManager.getInstance().getEvents();

        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventForms.class);
            v.getContext().startActivity(intent);
        });

        latestEvent.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventDetails.class);
            Log.d("Selection", "Selecting Event with ID " + events[0].getEventId());
            intent.putExtra("SELECTED_EVENT_ID", events[0].getEventId());
            Log.d("Intent", intent.toString());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Existing events are loaded here
        events = EventServiceManager.getInstance().getEvents();

        UpcomingEventAdapter upcomingEventAdapter = new UpcomingEventAdapter(events);
        if (events.length == 0) {
            latestEvent.setVisibility(View.GONE);
            noEventsText.setVisibility(View.VISIBLE);
            noUpcomingEventText.setVisibility(View.VISIBLE);
            return;  // early return to avoid IndexOutOfBoundsException
        } else if (events.length == 1) {
            latestEvent.setVisibility(View.VISIBLE);
            noEventsText.setVisibility(View.GONE);
            noUpcomingEventText.setVisibility(View.VISIBLE);
        } else {
            latestEvent.setVisibility(View.VISIBLE);
            noEventsText.setVisibility(View.GONE);
            noUpcomingEventText.setVisibility(View.GONE);
        }

        latestEventTitle.setText(events[0].getName());
        latestEventDescription.setText(events[0].getDescription());
        if (events[0].getStartDate() != null) {
            latestEventStartDate.setText(events[0].getStartDate());
        } else {
            latestEventStartDate.setText(R.string.tba);
        }
        if (events[0].getEndDate() != null) {
            latestEventEndDate.setText(events[0].getEndDate());
        } else {
            latestEventEndDate.setText(R.string.tba);
        }
        // TODO: fix date format
        latestEventAttendeesRegistered.setText(String.format("%d", events[0].getAttendees().size()));

        if (events[0].getAudienceLimit() == 0) {
            latestEventRemainingSlots.setText("∞");
        } else {
            latestEventRemainingSlots.setText(String.format("%d", events[0].getAudienceLimit() - events[0].getAttendees().size()));
        }

        latestEventTotalRevenue.setText(String.format("%.2f", events[0].getTicketPrice() * events[0].getAttendees().size()));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(upcomingEventAdapter);
    }
}



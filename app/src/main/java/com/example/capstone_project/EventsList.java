package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.UpcomingEventAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// TODO: fix date format
public class EventsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UpcomingEventAdapter eventAdapter;
    private List<Event> eventList; // List to hold event data
    private TextView noUpcomingEventText; // Reference to "No Registered Events" TextView
    private String sNum; // Student number from previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_list); // Link to the XML layout

        Intent previousActivity = getIntent();
        sNum = previousActivity.getStringExtra("studentNumber");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.registered_events_cardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize "No Registered Events" TextView
        noUpcomingEventText = findViewById(R.id.noUpcomingEventText);

        // Initialize the event list
        eventList = new ArrayList<>();

        // Firebase reference to the events
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("RegItEventListDatabaseSubtreeNode");

        // Fetch events from Firebase
        eventsRef.orderByChild("startDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear(); // Clear previous event data

                // Loop through each event in the database
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                    // Check if the student is in the attendees list
                    if (eventSnapshot.child("attendees").hasChild(sNum)) {
                        String eventName = eventSnapshot.child("name").getValue(String.class);
                        String eventDescription = eventSnapshot.child("description").getValue(String.class);
                        String eventStartDate = eventSnapshot.child("startDate").getValue(String.class);

                        Event event = new Event();
                        event.setName(eventName);
                        event.setDescription(eventDescription);
                        event.setStartDate(eventStartDate);

                        // Add the event to the list
                        eventList.add(event);
                    }
                }

                // Check if there are any events to show
                if (eventList.isEmpty()) {
                    noUpcomingEventText.setVisibility(View.VISIBLE); // Show message if no events
                } else {
                    noUpcomingEventText.setVisibility(View.GONE); // Hide message if events are available
                    updateRecyclerView(); // Update RecyclerView with the fetched events
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("EventsList", "loadEvents:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateRecyclerView() {
        // Convert the List to an array and set the adapter
        Event[] eventsArray = new Event[eventList.size()];
        eventList.toArray(eventsArray);

        eventAdapter = new UpcomingEventAdapter(eventsArray, false);
        recyclerView.setAdapter(eventAdapter);
    }
}

package com.example.capstone_project;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.AttendeeListAdapter;
import com.example.capstone_project.utils.EventServiceManager;

public class UnregisterAttendee extends AppCompatActivity {
    private String eventId;
    private TextView noAttendeesText;
    private RecyclerView attendeeList;
    private String[] attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregister_attendee);

        eventId = getIntent().getStringExtra("SELECTED_EVENT_ID");
        attendeeList = findViewById(R.id.unregisterAttendee_attendeeListView);
        noAttendeesText = findViewById(R.id.unregisterAttendee_noAttendeesText);

        eventId = getIntent().getStringExtra("SELECTED_EVENT_ID");
        attendees = getIntent().getStringArrayExtra("ATTENDEES");

        if (attendees.length == 0) {
            noAttendeesText.setVisibility(View.VISIBLE);
        }

        AttendeeListAdapter attendeeListAdapter = new AttendeeListAdapter(eventId, attendees, 1);
        attendeeList.setLayoutManager(new LinearLayoutManager(this));
        attendeeList.setAdapter(attendeeListAdapter);
    }
}

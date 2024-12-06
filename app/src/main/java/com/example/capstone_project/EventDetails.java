package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.AttendeeListAdapter;
import com.example.capstone_project.utils.EventServiceManager;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class EventDetails extends AppCompatActivity {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        TextView eventTitle = findViewById(R.id.eventDetailName);
        TextView eventDescription = findViewById(R.id.eventDetailDescription);
        TextView eventStartDate = findViewById(R.id.eventDetailStartDate);
        TextView verifyAttendantButton = findViewById(R.id.eventDetails_verifyAttendant_button);
        TextView noAttendeeText = findViewById(R.id.eventDetails_noAttendeeText);
        RecyclerView attendeeList = findViewById(R.id.eventDetails_attendeeListView);

        event = getIntent().getParcelableExtra("SELECTED_EVENT");
        String[] attendeeListArray = EventServiceManager.getInstance().getAttendeeNames(event.getEventId());

        eventTitle.setText(event.getName());

        eventDescription.setText(event.getDescription());

        eventStartDate.setText(event.getStartDate().format(dateTimeFormatter));

        verifyAttendantButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VerifyAttendee.class);
            intent.putExtra("EVENT_ID", event.getEventId());
            v.getContext().startActivity(intent);
        });

        if (attendeeListArray.length != 0) {
            noAttendeeText.setVisibility(View.INVISIBLE);
        }

        AttendeeListAdapter attendeeListAdapter = new AttendeeListAdapter(attendeeListArray);
        attendeeList.setLayoutManager(new LinearLayoutManager(this));
        attendeeList.setAdapter(attendeeListAdapter);
    }
}

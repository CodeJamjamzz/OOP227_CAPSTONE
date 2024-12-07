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

    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventStartDate;
    private TextView verifyAttendeeButton;
    private TextView editDetailsButton;
    private TextView deleteEventButton;
    private TextView numAttendeesRegistered;
    private TextView numRemainingSlots;
    private TextView numTotalRevenue;
    private TextView noAttendeeText;
    private RecyclerView attendeeList;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        eventTitle = findViewById(R.id.eventDetailName);
        eventDescription = findViewById(R.id.eventDetailDescription);
        eventStartDate = findViewById(R.id.eventDetailStartDate);
        verifyAttendeeButton = findViewById(R.id.eventDetails_verifyAttendant_button);
        editDetailsButton = findViewById(R.id.eventDetails_editDetails_button);
        deleteEventButton = findViewById(R.id.eventDetails_deleteEvent_button);
        numAttendeesRegistered = findViewById(R.id.eventDetails_attendeesRegistered);
        numRemainingSlots = findViewById(R.id.eventDetails_remainingSlots);
        numTotalRevenue = findViewById(R.id.eventDetails_totalRevenue);
        noAttendeeText = findViewById(R.id.eventDetails_noAttendeeText);
        attendeeList = findViewById(R.id.eventDetails_attendeeListView);

        // TODO: register and unregister attendee

        verifyAttendeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VerifyAttendee.class);
            intent.putExtra("SELECTED_EVENT_ID", event.getEventId());
            v.getContext().startActivity(intent);
        });

        editDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventForms.class);
            intent.putExtra("SELECTED_EVENT_ID", event.getEventId());
            v.getContext().startActivity(intent);
        });

        deleteEventButton.setOnClickListener(n -> {
            // TODO: should probably add confirmation to delete
            EventServiceManager.getInstance().deleteEvent(event.getEventId());
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        event = EventServiceManager.getInstance().getEventFromId(getIntent().getStringExtra("SELECTED_EVENT_ID"));
        String[] attendeeListArray = EventServiceManager.getInstance().getAttendeeNames(event.getEventId());

        eventTitle.setText(event.getName());

        eventDescription.setText(event.getDescription());

        if (event.getStartDate() != null) {
            eventStartDate.setText(event.getStartDate().format(dateTimeFormatter));
        } else {
            eventStartDate.setText(R.string.tba);
        }

        numAttendeesRegistered.setText(String.format("%d", attendeeListArray.length));

        if (event.getAudienceLimit() == 0) {
            numRemainingSlots.setText("∞");
        } else {
            numRemainingSlots.setText(String.format("%d", event.getAudienceLimit() - attendeeListArray.length));
        }

        numTotalRevenue.setText(String.format("%.2f", event.getTicketPrice() * attendeeListArray.length));

        if (attendeeListArray.length != 0) {
            noAttendeeText.setVisibility(View.GONE);
        }

        AttendeeListAdapter attendeeListAdapter = new AttendeeListAdapter(attendeeListArray);
        attendeeList.setLayoutManager(new LinearLayoutManager(this));
        attendeeList.setAdapter(attendeeListAdapter);
    }
}

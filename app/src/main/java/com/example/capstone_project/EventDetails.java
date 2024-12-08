package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.AttendeeListAdapter;
import com.example.capstone_project.utils.EventServiceManager;
import com.google.android.gms.tasks.Task;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class EventDetails extends AppCompatActivity {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);

    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventStartDate;
    private TextView eventEndDate;

    private TextView registerAttendeeButton;
    private TextView unregisterAttendeeButton;
    private TextView verifyAttendeeButton;
    private TextView editDetailsButton;
    private TextView deleteEventButton;

    private TextView numAttendeesRegistered;
    private TextView numRemainingSlots;
    private TextView numTotalRevenue;
    private TextView noAttendeeText;
    private RecyclerView attendeeList;

    private Event event;
    private String[] attendeeListArray = null;

    private List<Attendee> ListOfAttendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        eventTitle = findViewById(R.id.eventDetailName);
        eventDescription = findViewById(R.id.eventDetailDescription);
        eventStartDate = findViewById(R.id.eventDetailStartDate);
        eventEndDate = findViewById(R.id.eventDetailEndDate);

        registerAttendeeButton = findViewById(R.id.eventDetails_registerAttendant_button);
        unregisterAttendeeButton = findViewById(R.id.eventDetails_unRegisterAttendant_button);
        verifyAttendeeButton = findViewById(R.id.eventDetails_verifyAttendant_button);
        editDetailsButton = findViewById(R.id.eventDetails_editDetails_button);
        deleteEventButton = findViewById(R.id.eventDetails_deleteEvent_button);

        numAttendeesRegistered = findViewById(R.id.eventDetails_attendeesRegistered);
        numRemainingSlots = findViewById(R.id.eventDetails_remainingSlots);
        numTotalRevenue = findViewById(R.id.eventDetails_totalRevenue);
        noAttendeeText = findViewById(R.id.eventDetails_noAttendeeText);
        attendeeList = findViewById(R.id.eventDetails_attendeeListView);

        // TODO: register and unregister attendee
        registerAttendeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RegisterAttendee.class);
            intent.putExtra("SELECTED_EVENT_ID", event.getEventId());
            intent.putExtra("ATTENDEES", attendeeListArray);
            v.getContext().startActivity(intent);
        });

        unregisterAttendeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UnregisterAttendee.class);
            intent.putExtra("SELECTED_EVENT_ID", event.getEventId());
            intent.putExtra("ATTENDEES", attendeeListArray);
            v.getContext().startActivity(intent);
        });

        verifyAttendeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VerifyAttendee.class);
            intent.putExtra("SELECTED_EVENT_ID", event.getEventId());
            v.getContext().startActivity(intent);
        });

        editDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventForms.class);
            intent.putExtra("SELECTED_EVENT_ID", event.getEventId());
            intent.putExtra("ATTENDEES", attendeeListArray);
            v.getContext().startActivity(intent);
        });

        deleteEventButton.setOnClickListener(n -> {
            // DONE: should probably add confirmation to delete
            AlertDialog.Builder popup = new AlertDialog.Builder(this);
            popup.setTitle("Delete Event");
            popup.setMessage("Are you sure you want to delete " + event.getName() + "?");
            popup.setPositiveButton("Yes", (dialog, which) -> {
                Toast.makeText(this, "Deleted " + event.getName(), Toast.LENGTH_SHORT).show();
                EventServiceManager.getInstance().deleteEvent(event.getEventId());
                finish();
            });
            popup.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog dialog = popup.create();
            dialog.show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        event = EventServiceManager.getInstance().getEventFromId(getIntent().getStringExtra("SELECTED_EVENT_ID"));
        Task<String[]> task = EventServiceManager.getInstance().getAttendeeNames(event.getEventId())
                .addOnSuccessListener(attendeeList -> {
                    attendeeListArray = attendeeList;

                    eventTitle.setText(event.getName());

                    eventDescription.setText(event.getDescription());

                    if (event.getStartDate() != null) {
                        eventStartDate.setText(event.getStartDate());
                    } else {
                        eventStartDate.setText(R.string.tba);
                    }

                    numAttendeesRegistered.setText(String.format("%d", attendeeList.length));
                    if (event.getEndDate() != null) {
                        eventEndDate.setText(event.getEndDate());
                    } else {
                        eventEndDate.setText(R.string.tba);
                    }

                    numAttendeesRegistered.setText(String.format("%d", attendeeListArray.length));

                    if (event.getAudienceLimit() == 0) {
                        numRemainingSlots.setText("∞");
                    } else {
                        numRemainingSlots.setText(String.format("%d", event.getAudienceLimit() - attendeeList.length));
                    }

                    numTotalRevenue.setText(String.format("%.2f", event.getTicketPrice() * attendeeList.length));

                    if (attendeeList.length != 0) {
                        noAttendeeText.setVisibility(View.GONE);
                    }

                    AttendeeListAdapter attendeeListAdapter = new AttendeeListAdapter(event.getEventId(), attendeeList, 0);
                    this.attendeeList.setLayoutManager(new LinearLayoutManager(this));
                    this.attendeeList.setAdapter(attendeeListAdapter);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }
}
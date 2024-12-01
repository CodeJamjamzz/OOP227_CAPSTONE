package com.example.capstone_project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone_project.models.Event;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class EventDetails extends AppCompatActivity {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventStartDate;
    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        eventTitle = findViewById(R.id.eventDetailName);
        eventDescription = findViewById(R.id.eventDetailDescription);
        eventStartDate = findViewById(R.id.eventDetailStartDate);
        event = getIntent().getParcelableExtra("SELECTED_EVENT");

        eventTitle.setText(event.getName());
        eventDescription.setText(event.getDescription());
        eventStartDate.setText(event.getStartDate().format(dateTimeFormatter));
    }
}

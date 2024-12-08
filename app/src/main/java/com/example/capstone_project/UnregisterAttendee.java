package com.example.capstone_project;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.utils.AttendeeListAdapter;
import com.example.capstone_project.utils.EventServiceManager;

import java.util.ArrayList;
import java.util.List;

public class UnregisterAttendee extends AppCompatActivity {
    private String eventId;
    private TextView noAttendeesText;
    private TextView confirmUnregistrationsButton;
    private RecyclerView attendeeList;
    private String[] attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregister_attendee);

        eventId = getIntent().getStringExtra("SELECTED_EVENT_ID");
        attendeeList = findViewById(R.id.unregisterAttendee_attendeeListView);
        noAttendeesText = findViewById(R.id.unregisterAttendee_noAttendeesText);
        confirmUnregistrationsButton = findViewById(R.id.unregisterAttendee_confirmUnregistrationsButton);

        eventId = getIntent().getStringExtra("SELECTED_EVENT_ID");
        attendees = getIntent().getStringArrayExtra("ATTENDEES");

        if (attendees.length == 0) {
            noAttendeesText.setVisibility(View.VISIBLE);
        }

        AttendeeListAdapter attendeeListAdapter = new AttendeeListAdapter(eventId, attendees, 1);
        attendeeList.setLayoutManager(new LinearLayoutManager(this));
        attendeeList.setAdapter(attendeeListAdapter);

        confirmUnregistrationsButton.setOnClickListener(v -> {
            List<String> newAttendees = new ArrayList<>();
            for (int i = 0; i < attendeeListAdapter.getItemCount(); i++) {
                RecyclerView.ViewHolder holder = attendeeList.findViewHolderForAdapterPosition(i);
                if (holder instanceof AttendeeListAdapter.ViewHolder) {
                    AttendeeListAdapter.ViewHolder viewHolder = (AttendeeListAdapter.ViewHolder) holder;
                    if (!viewHolder.isSelected()) {
                        newAttendees.add(attendees[i]);
                    } else {
                        String attendeeId = EventServiceManager.getInstance().getAttendeeIdFromName(eventId, attendees[i]);
                        EventServiceManager.getInstance().unRegisterAttendee(eventId, attendeeId);
                    }
                }
            }
            EventServiceManager.updateEvents();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                finish();
            }, 1500); // 2000 milliseconds = 2 seconds();
        });
    }
}

package com.example.capstone_project;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.models.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AdminDashboard extends AppCompatActivity {
    CustomAdapter customAdapter;
    RecyclerView.LayoutManager layoutManager;

    LocalDateTime testStart = LocalDateTime.now().plusDays(1);
    LocalDateTime testEnd = LocalDateTime.now().plusDays(3);

    Event event1 = new Event("CSS Tutorials", testStart, testEnd, "CIT-U", 0,"Master the Java language and code your way to success!", "CCS", 0.0);
    Event event2 = new Event("CCS Akwe", testStart, testEnd, "CIT-U", 0,"Find new friends!", "CCS", 249.0);
    Event event3 = new Event("Founder's Day", testStart, testEnd, "CIT-U", 0, "Honor our origins.", "General", 0.0);
    Event event4 = new Event("Final Examination", testStart, testEnd, "CIT-U", 0, "It's the final countdown", "General", 0);

    Event[] events = new Event[]{event1, event2, event3, event4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.upcoming_events_cardList);

        customAdapter = new CustomAdapter(events);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customAdapter);
    }
}


@RequiresApi(api = Build.VERSION_CODES.O)
class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Event[] localDataSet;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventTitle;
        private final TextView eventDescription;
        private final TextView eventStartDate;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            eventTitle = view.findViewById(R.id.eventTitle);
            eventDescription = view.findViewById(R.id.eventDescription);
            eventStartDate = view.findViewById(R.id.eventStartDate);
        }

        public TextView getEventTitle() {
            return eventTitle;
        }

        public TextView getEventStartDate() {
            return eventStartDate;
        }

        public TextView getEventDescription() {
            return eventDescription;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(Event[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.upcoming_event_view, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getEventTitle().setText(localDataSet[position].getName());
        viewHolder.getEventStartDate().setText(localDataSet[position].getStartDate().format(dateTimeFormatter));
        viewHolder.getEventDescription().setText(localDataSet[position].getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}


package com.example.capstone_project.utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.EventDetails;
import com.example.capstone_project.R;
import com.example.capstone_project.models.Event;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;

public class UpcomingEventAdapter extends RecyclerView.Adapter<UpcomingEventAdapter.ViewHolder> {

    private Event[] localDataSet;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventTitle;
        private final TextView eventDescription;
        private final TextView eventStartDate;
        private int position;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(v -> {
                Intent intent = new Intent(view.getContext(), EventDetails.class);
                intent.putExtra("SELECTED_EVENT", localDataSet[position]);
                view.getContext().startActivity(intent);
            });
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
     * dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public UpcomingEventAdapter(Event[] dataSet) {
        localDataSet = (dataSet.length > 1)
                ? Arrays.copyOfRange(dataSet, 1, dataSet.length)
                : new Event[0];

    }

    // Create new views (invoked by the layout manager)
    @NonNull
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
        viewHolder.position = viewHolder.getAdapterPosition();
        viewHolder.getEventTitle().setText(localDataSet[position].getName());
        if (localDataSet[position].getStartDate() == null) {
            viewHolder.getEventStartDate().setText(R.string.tba);
        } else {
            viewHolder.getEventStartDate().setText(localDataSet[position].getStartDate().format(dateTimeFormatter));
        }
        viewHolder.getEventDescription().setText(localDataSet[position].getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}



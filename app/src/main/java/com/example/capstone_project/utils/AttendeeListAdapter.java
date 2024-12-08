package com.example.capstone_project.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone_project.R;

public class AttendeeListAdapter extends RecyclerView.Adapter<AttendeeListAdapter.ViewHolder> {

    private final String eventId;
    private String[] localDataSet;
    protected int mode;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView unregisterButton;
        public boolean status = false;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.attendeeName);
            unregisterButton = view.findViewById(R.id.unregisterAttendeeButton);
        }

        public void toggle() {
            status = !status;

            if (status) {
                unregisterButton.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                unregisterButton.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.red_button));
                unregisterButton.setText("UNREGISTER");
            } else {
                unregisterButton.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                unregisterButton.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.white_button));
                unregisterButton.setText("REGISTERED");
            }
        }

        public boolean isSelected() {
            return status;
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getUnregisterButton() { return unregisterButton; }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     *
     * mode here will determine whether to show the unregister button or not
     */
    public AttendeeListAdapter(String eventId, String[] dataSet, int mode) {
        this.eventId = eventId;
        localDataSet = dataSet;
        this.mode = mode;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(String.format("%d.\t\t%s", position + 1, localDataSet[position]));
        if (mode == 0) {
            viewHolder.getUnregisterButton().setVisibility(View.GONE);
        } else {
            viewHolder.getUnregisterButton().setVisibility(View.VISIBLE);
            // TODO: confirm to unregister attendee
            viewHolder.getUnregisterButton().setOnClickListener(v -> {
//                String attendeeId = EventServiceManager.getInstance().getAttendeeIdFromName(eventId, localDataSet[position]);
//                EventServiceManager.getInstance().unRegisterAttendee(eventId, attendeeId);
                viewHolder.toggle();
            });
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

}

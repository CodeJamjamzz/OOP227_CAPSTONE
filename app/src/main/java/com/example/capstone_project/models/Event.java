package com.example.capstone_project.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event implements Parcelable {
    private String name;
    private final String eventId; // eventID must not be changeable
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;
    private int audienceLimit;
    private String description;
    private String category;
    private double ticketPrice;
    private List<Attendee> attendees;

    public Event() {
        startDate = null;
        endDate = null;
        venue = "TBA";
        audienceLimit = 0;
        description = "TBA";
        category = null;
        ticketPrice = 0.0;
        this.eventId = UUID.randomUUID().toString();
        this.attendees = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getVenue() {
        return venue;
    }

    public int getAudienceLimit() {
        return audienceLimit;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setAudienceLimit(int audienceLimit) {
        this.audienceLimit = audienceLimit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    // Parcelable-specific stuff
    protected Event(Parcel in) {
        name = in.readString();
        eventId = in.readString();

        // Read LocalDateTime as long timestamps
        long startDateTimestamp = in.readLong();
        startDate = startDateTimestamp == -1 ? null :
                LocalDateTime.ofInstant(Instant.ofEpochMilli(startDateTimestamp), ZoneOffset.UTC);

        long endDateTimestamp = in.readLong();
        endDate = endDateTimestamp == -1 ? null :
                LocalDateTime.ofInstant(Instant.ofEpochMilli(endDateTimestamp), ZoneOffset.UTC);

        venue = in.readString();
        audienceLimit = in.readInt();
        description = in.readString();
        category = in.readString();
        ticketPrice = in.readDouble();

        attendees = new ArrayList<>();
        in.readList(attendees, Attendee.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(eventId);

        // Write LocalDateTime as long timestamps
        dest.writeLong(startDate.toInstant(ZoneOffset.UTC).toEpochMilli());
        dest.writeLong(endDate.toInstant(ZoneOffset.UTC).toEpochMilli());

        dest.writeString(venue);
        dest.writeInt(audienceLimit);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeDouble(ticketPrice);
        dest.writeList(attendees);
    }

    @NonNull
    @Override
    // name of the event object will always be its own event ID as it will always stay and be unique
    public String toString() {
        return eventId;
    }
}

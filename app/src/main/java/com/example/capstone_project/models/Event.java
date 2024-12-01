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
    private String eventId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;
    private int audienceLimit;
    private String description;
    private String category;
    private double ticketPrice;
private List<Attendee> attendees;

    public Event(String name, LocalDateTime startDate, LocalDateTime endDate, String venue, int audienceLimit, String description, String category, double ticketPrice) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venue = venue;
        this.audienceLimit = audienceLimit;
        this.description = description;
        this.category = category;
        this.ticketPrice = ticketPrice;
        attendees = new ArrayList<>();
        this.eventId = UUID.randomUUID().toString();
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
}

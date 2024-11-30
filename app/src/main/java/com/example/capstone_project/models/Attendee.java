package com.example.capstone_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Attendee implements Parcelable {
    private String name;
    private String idNumber;
    private String email;
    private String yearLevel;
    private List<String> eventIds;

    public Attendee(String name, String idNumber, String email, String yearLevel) {
        this.name = name;
        this.idNumber = idNumber;
        this.email = email;
        this.yearLevel = yearLevel;
        eventIds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public List<String> getEventsRegistered() {
        return eventIds;
    }

    public void addEventId(String eventId) {
        if (!eventIds.contains(eventId)) {
            eventIds.add(eventId);
        }
    }

    // Parcelable-specific stuff
    protected Attendee(Parcel in) {
        name = in.readString();
        idNumber = in.readString();
        email = in.readString();
        yearLevel = in.readString();

        // Read event IDs
        eventIds = new ArrayList<>();
        in.readList(eventIds, String.class.getClassLoader());
    }

    public static final Creator<Attendee> CREATOR = new Creator<Attendee>() {
        @Override
        public Attendee createFromParcel(Parcel in) {
            return new Attendee(in);
        }

        @Override
        public Attendee[] newArray(int size) {
            return new Attendee[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(idNumber);
        dest.writeString(email);
        dest.writeString(yearLevel);
        dest.writeList(eventIds);
    }
}

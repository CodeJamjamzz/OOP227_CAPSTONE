package com.example.capstone_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

// Attendee Object is to access userAccount information through the database
// security checks for access are already setup there & locally

public class Attendee {
    private String attendeeIDNumber;
    private String userAccountID;
    private String course;
    private int year;

    public Attendee(String attendeeIDNumber, String userAccountID, String course, int year) {
        setAttendeeIDNumber(attendeeIDNumber);
        setUserAccountID(userAccountID); // student ID
        setCourse(course);
        setYear(year);
    }

    public String getAttendeeIDNumber() {
        return attendeeIDNumber;
    }

    public String getUserAccountID() {
        return userAccountID;
    }

    public String getCourse() {
        return course;
    }

    public int getYear() {
        return year;
    }

    // these are all for the attendee object in the event list
    public void setAttendeeIDNumber(String attendeeIDNumber) {
        this.attendeeIDNumber = attendeeIDNumber;
    }

    public void setUserAccountID(String userAccountID) {
        this.userAccountID = userAccountID;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // TODO: getting UserAccount Information getters
    // private String accountName;
    // private String accountEmail;

    public String getAttendeeName() {
        return null;
    }

    public String getAttendeeEmail() {
        return null;
    }
}

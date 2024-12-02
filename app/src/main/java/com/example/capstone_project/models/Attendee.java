package com.example.capstone_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Attendee {
    private String attendeeIDNumber;
    private String userAccountID;
    private String course;
    private int year;

    public Attendee(String attendeeIDNumber, String userAccountID, String course, int year) {
        setAttendeeIDNumber(attendeeIDNumber);
        setUserAccountID(userAccountID);
        setCourse(course);
        setYear(year);
    }

    public String getAttendeeIDNumber() {
        return attendeeIDNumber;
    }

    public void setAttendeeIDNumber(String attendeeIDNumber) {
        this.attendeeIDNumber = attendeeIDNumber;
    }

    public String getUserAccountID() {
        return userAccountID;
    }

    public void setUserAccountID(String userAccountID) {
        this.userAccountID = userAccountID;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}

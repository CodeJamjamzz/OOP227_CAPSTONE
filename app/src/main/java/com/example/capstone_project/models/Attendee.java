package com.example.capstone_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

// Attendee Object is to access userAccount information through the database
// security checks for access are already setup there & locally

public class Attendee {
    private String userAccountID;
    private String userAccountName;

    public Attendee() {

    }

    public Attendee(String userAccountID, String userAccountName) {
        setUserAccountID(userAccountID); // student ID
        setUserAccountName(userAccountName);
    }

    public String getUserAccountID() { return userAccountID; }

    public String getUserAccountName() { return userAccountName; }

    public void setUserAccountID(String userAccountID) { this.userAccountID = userAccountID; }

    public void setUserAccountName(String userAccountName) { this.userAccountName = userAccountName; }

    // TODO: getting UserAccount Information getters
    // private String accountEmail;

//    public String getAttendeeEmail() {
//        return null;
//    }
}

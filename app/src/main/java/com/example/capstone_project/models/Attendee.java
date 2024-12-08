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
    private boolean userConfirm;

    public Attendee() {
        userConfirm = false;
    }

    public Attendee(String userAccountID, String userAccountName) {
        setUserAccountID(userAccountID); // student ID
        setUserAccountName(userAccountName);
        userConfirm = false;
    }

    public String getUserAccountID() { return userAccountID; }

    public String getUserAccountName() { return userAccountName; }

    public boolean isUserConfirm() { return userConfirm; }

    public void setUserAccountID(String userAccountID) { this.userAccountID = userAccountID; }

    public void setUserAccountName(String userAccountName) { this.userAccountName = userAccountName; }

    public void setUserConfirm(boolean userConfirm) { this.userConfirm = userConfirm; }

    public void toggleUserConfirm() { this.userConfirm = !userConfirm; }

    // TODO: getting UserAccount Information getters
    // private String accountEmail;

//    public String getAttendeeEmail() {
//        return null;
//    }
}

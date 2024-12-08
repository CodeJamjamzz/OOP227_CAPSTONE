package com.example.capstone_project.models;

import java.util.ArrayList;
import java.util.List;

// UserAccount information is stored in JSON in the database, the object is only used and created
// temporarily through the firebaseController so that it could be used within some methods in the app.
// It is also used to make it easier to pass the data to firebase by storing & passing as an obj.

public class UserAccount {

    private String accountID;
    private String accountName;
    private String accountEmail;
    private String accountPassword;
    private String accountCourseYear;
    private List<String> accountEventsAttending;

    // Used by Firebase to add in DB (it needs an empty constructor)
    public UserAccount() {
        this.accountEventsAttending = new ArrayList<>(); // Initialize to avoid null
    }

    public UserAccount(String accountID, String accountName, String accountEmail, String CourseYear, String accountPassword) {

        setAccountCourseYear(CourseYear);
        setAccountEmail(accountEmail);
        accountEventsAttending = new ArrayList<>();
        setAccountID(accountID);
        setAccountName(accountName);
        setAccountPassword(accountPassword);
    }

                                        /* SETTERS */
    public void setAccountCourseYear(String accountCourseYear) { this.accountCourseYear = accountCourseYear; }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setAccountEventsAttending(List<String> accountEventsAttending) {
        this.accountEventsAttending = accountEventsAttending;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountPassword(String accountPassword) { this.accountPassword = accountPassword; }

                                        /* GETTERS */
    public String getAccountCourseYear() {
        return accountCourseYear;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public List<String> getAccountEventsAttending() {
        return new ArrayList<>(accountEventsAttending); // Return a copy to avoid external modification
    }

    public String getAccountID() {
        return accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

                                /* Attended Events Method */
    public void addEvent(String eventID) {
        this.accountEventsAttending.add(eventID); // Method to add an event
    }

    public void removeEvent(String eventID) {
        this.accountEventsAttending.remove(eventID);
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "accountID='" + accountID + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountEmail='" + accountEmail + '\'' +
                ", accountPassword='" + accountPassword + '\'' +
                ", accountCourseYear='" + accountCourseYear + '\'' +
                ", accountEventsAttending=" + accountEventsAttending +
                '}';
    }
}

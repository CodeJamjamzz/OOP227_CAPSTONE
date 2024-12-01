package com.example.capstone_project.models;

import java.util.ArrayList;
import java.util.List;

public class UserAccount {

    private String accountID;
    private String accountName;
    private String accountEmail;
    private String accountPassword;
    private List<String> accountEventsAttending;


    // TODO: for the account list in firebase, the accountID is the key (school ID), no idea for password
    // password can prolly be more secured if using diff DB system but for 2.0 lang hehe

    // Used by Firebase to create a JSON (it needs an empty constructor)
    public UserAccount() {
        this.accountEventsAttending = new ArrayList<>(); // Initialize to avoid null
    }

    public UserAccount(String accountID, String accountName, String accountEmail, String accountPassword) {
        // processing of account password (it should be hashed) is to be done in the FB-Controller

        setAccountID(accountID);
        setAccountEmail(accountEmail);
        setAccountName(accountName);
        setAccountPassword(accountPassword);
        accountEventsAttending = new ArrayList<>();
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public List<String> getAccountEventsAttending() {
        return new ArrayList<>(accountEventsAttending); // Return a copy to avoid external modification
    }

    public void setAccountEventsAttending(List<String> accountEventsAttending) {
        this.accountEventsAttending = accountEventsAttending;
    }

    public void addEvent(String eventID) {
        this.accountEventsAttending.add(eventID); // Method to add an event
    }

}

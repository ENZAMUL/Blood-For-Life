package com.enzamuls.blooddonation.model;

public class userachievements {
    private String userID, patientName, givenLocation, donateDate;

    public userachievements() {

    }

    public userachievements(String userID, String patientName, String givenLocation, String donateDate) {
        this.userID = userID;
        this.patientName = patientName;
        this.givenLocation = givenLocation;
        this.donateDate = donateDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGivenLocation() {
        return givenLocation;
    }

    public void setGivenLocation(String givenLocation) {
        this.givenLocation = givenLocation;
    }

    public String getDonateDate() {
        return donateDate;
    }

    public void setDonateDate(String donateDate) {
        this.donateDate = donateDate;
    }
}

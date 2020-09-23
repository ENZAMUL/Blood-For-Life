package com.enzamuls.blooddonation.model;

public class Ambulance {
    public String ambulanceName, ambulanceContact, ambulanceLocation, ambulanceDescription;

    public Ambulance() {
    }

    public Ambulance(String ambulanceName, String ambulanceContact, String ambulanceLocation, String ambulanceDescription) {
        this.ambulanceName = ambulanceName;
        this.ambulanceContact = ambulanceContact;
        this.ambulanceLocation = ambulanceLocation;
        this.ambulanceDescription = ambulanceDescription;
    }

    public String getAmbulanceName() {
        return ambulanceName;
    }

    public void setAmbulanceName(String ambulanceName) {
        this.ambulanceName = ambulanceName;
    }

    public String getAmbulanceContact() {
        return ambulanceContact;
    }

    public void setAmbulanceContact(String ambulanceContact) {
        this.ambulanceContact = ambulanceContact;
    }

    public String getAmbulanceLocation() {
        return ambulanceLocation;
    }

    public void setAmbulanceLocation(String ambulanceLocation) {
        this.ambulanceLocation = ambulanceLocation;
    }

    public String getAmbulanceDescription() {
        return ambulanceDescription;
    }

    public void setAmbulanceDescription(String ambulanceDescription) {
        this.ambulanceDescription = ambulanceDescription;
    }
}

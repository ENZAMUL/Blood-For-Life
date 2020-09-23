package com.enzamuls.blooddonation.model;

public class users {
    private String userName, email, contactNo, hideContact, birthDate, gender, bloodGroup, uDivision, uDistrict,
            policeStation, uArea, isDonor;

    public users() {

    }

    public users(String userName, String email, String contactNo, String hideContact,
                 String birthDate, String gender, String bloodGroup, String uDivision,
                 String uDistrict, String policeStation, String uArea, String isDonor) {
        this.userName = userName;
        this.email = email;
        this.contactNo = contactNo;
        this.hideContact = hideContact;
        this.birthDate = birthDate;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.uDivision = uDivision;
        this.uDistrict = uDistrict;
        this.policeStation = policeStation;
        this.uArea = uArea;
        this.isDonor = isDonor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getHideContact() {
        return hideContact;
    }

    public void setHideContact(String hideContact) {
        this.hideContact = hideContact;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getuDivision() {
        return uDivision;
    }

    public void setuDivision(String uDivision) {
        this.uDivision = uDivision;
    }

    public String getuDistrict() {
        return uDistrict;
    }

    public void setuDistrict(String uDistrict) {
        this.uDistrict = uDistrict;
    }

    public String getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public String getuArea() {
        return uArea;
    }

    public void setuArea(String uArea) {
        this.uArea = uArea;
    }

    public String getIsDonor() {
        return isDonor;
    }

    public void setIsDonor(String isDonor) {
        this.isDonor = isDonor;
    }
}

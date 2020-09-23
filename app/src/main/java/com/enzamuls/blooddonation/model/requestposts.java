package com.enzamuls.blooddonation.model;

public class requestposts {
    public String postTime, postDate, postUserID, postUserName, postPatientName, postPatientAge,
            postPatientBloodGroup, postPatientProblem, postBloodBagsNeeded, postHospitalName, postPatientLocation,
            postContactPersonName, postContactPersonPhone, postPatientBloodNeedDate, status;

    public requestposts() {

    }

    public requestposts(String postTime, String postDate, String postUserID, String postUserName,
                        String postPatientName, String postPatientAge, String postPatientBloodGroup,
                        String postPatientProblem, String postBloodBagsNeeded, String postHospitalName,
                        String postPatientLocation, String postContactPersonName, String postContactPersonPhone,
                        String postPatientBloodNeedDate, String status) {
        this.postTime = postTime;
        this.postDate = postDate;
        this.postUserID = postUserID;
        this.postUserName = postUserName;
        this.postPatientName = postPatientName;
        this.postPatientAge = postPatientAge;
        this.postPatientBloodGroup = postPatientBloodGroup;
        this.postPatientProblem = postPatientProblem;
        this.postBloodBagsNeeded = postBloodBagsNeeded;
        this.postHospitalName = postHospitalName;
        this.postPatientLocation = postPatientLocation;
        this.postContactPersonName = postContactPersonName;
        this.postContactPersonPhone = postContactPersonPhone;
        this.postPatientBloodNeedDate = postPatientBloodNeedDate;
        this.status = status;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostUserID() {
        return postUserID;
    }

    public void setPostUserID(String postUserID) {
        this.postUserID = postUserID;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    public String getPostPatientName() {
        return postPatientName;
    }

    public void setPostPatientName(String postPatientName) {
        this.postPatientName = postPatientName;
    }

    public String getPostPatientAge() {
        return postPatientAge;
    }

    public void setPostPatientAge(String postPatientAge) {
        this.postPatientAge = postPatientAge;
    }

    public String getPostPatientBloodGroup() {
        return postPatientBloodGroup;
    }

    public void setPostPatientBloodGroup(String postPatientBloodGroup) {
        this.postPatientBloodGroup = postPatientBloodGroup;
    }

    public String getPostPatientProblem() {
        return postPatientProblem;
    }

    public void setPostPatientProblem(String postPatientProblem) {
        this.postPatientProblem = postPatientProblem;
    }

    public String getPostBloodBagsNeeded() {
        return postBloodBagsNeeded;
    }

    public void setPostBloodBagsNeeded(String postBloodBagsNeeded) {
        this.postBloodBagsNeeded = postBloodBagsNeeded;
    }

    public String getPostHospitalName() {
        return postHospitalName;
    }

    public void setPostHospitalName(String postHospitalName) {
        this.postHospitalName = postHospitalName;
    }

    public String getPostPatientLocation() {
        return postPatientLocation;
    }

    public void setPostPatientLocation(String postPatientLocation) {
        this.postPatientLocation = postPatientLocation;
    }

    public String getPostContactPersonName() {
        return postContactPersonName;
    }

    public void setPostContactPersonName(String postContactPersonName) {
        this.postContactPersonName = postContactPersonName;
    }

    public String getPostContactPersonPhone() {
        return postContactPersonPhone;
    }

    public void setPostContactPersonPhone(String postContactPersonPhone) {
        this.postContactPersonPhone = postContactPersonPhone;
    }

    public String getPostPatientBloodNeedDate() {
        return postPatientBloodNeedDate;
    }

    public void setPostPatientBloodNeedDate(String postPatientBloodNeedDate) {
        this.postPatientBloodNeedDate = postPatientBloodNeedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

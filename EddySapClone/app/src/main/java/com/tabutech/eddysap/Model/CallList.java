package com.tabutech.eddysap.Model;

public class CallList {
    private String userID;
    private String userName;
    private String date;
    private String userProfile;
    private String callType;

    public CallList() {

    }

    public CallList(String userID, String userName, String date, String userProfile, String callType) {
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.userProfile = userProfile;
        this.callType = callType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}

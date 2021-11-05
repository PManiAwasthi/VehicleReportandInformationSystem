package com.example.carreportappv2;

public class UserInfoClass {
    String name;
    String email;
    String aadharNum;
    String mobileNum;
    String address;
    String userImageUri;
    String uid;
    String stationPincode;

    UserInfoClass(){

    }

    public UserInfoClass(String name, String email, String aadharNum, String mobileNum, String address, String userImageUri, String uid, String stationPincode) {
        this.name = name;
        this.email = email;
        this.aadharNum = aadharNum;
        this.mobileNum = mobileNum;
        this.address = address;
        this.userImageUri = userImageUri;
        this.uid = uid;
        this.stationPincode = stationPincode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadharNum() {
        return aadharNum;
    }

    public void setAadharNum(String aadharNum) {
        this.aadharNum = aadharNum;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserImageUri() {
        return userImageUri;
    }

    public void setUserImageUri(String userImageUri) {
        this.userImageUri = userImageUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStationPincode() {
        return stationPincode;
    }

    public void setStationPincode(String stationPincode) {
        this.stationPincode = stationPincode;
    }
}

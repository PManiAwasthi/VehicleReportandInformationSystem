package com.example.carreportappv2;

public class UserInfo {
    private String email;
    private String acc_type;

    UserInfo(){

    }
    UserInfo(String email, String acc_type){
        this.email = email;
        this.acc_type = acc_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }
}

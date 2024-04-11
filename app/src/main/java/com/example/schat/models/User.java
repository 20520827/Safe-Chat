package com.example.schat.models;

import com.google.firebase.Timestamp;

public class User {
    private String phoneNumber;
    private String userName;
    private Timestamp createTime;

    public User() {
    }

    public User(String phoneNumber, String userName, Timestamp createTime) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.createTime = createTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}

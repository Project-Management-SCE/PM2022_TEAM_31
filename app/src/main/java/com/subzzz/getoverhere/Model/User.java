package com.subzzz.getoverhere.Model;

import com.google.firebase.Timestamp;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String idNum;
    private String address;
    private String gender;
    private Timestamp birthDay;
    private String UType;

    public User(){}

    public User(String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay, String UType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.idNum = idNum;
        this.address = address;
        this.gender = gender;
        this.birthDay = birthDay;
        this.UType = UType;
    }

    public User(String userId, String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay, String UType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.idNum = idNum;
        this.address = address;
        this.gender = gender;
        this.birthDay = birthDay;
        this.UType = UType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Timestamp birthDay) {
        this.birthDay = birthDay;
    }

    public String getUType() {
        return UType;
    }

    public void setUType(String UType) {
        this.UType = UType;
    }
}

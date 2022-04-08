package com.subzzz.getoverhere.Model;

public class User {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String userName;
    private String idNum;
    private String UType;

    public User(){}

    public User(String email, String firstName, String lastName, String userName, String idNum, String UType) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.idNum = idNum;
        this.UType = UType;
    }

    public User(String userId, String email, String firstName, String lastName, String userName, String idNum, String UType) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.idNum = idNum;
        this.UType = UType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getUType() {
        return UType;
    }

    public void setUType(String UType) {
        this.UType = UType;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }
}

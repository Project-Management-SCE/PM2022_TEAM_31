package com.subzzz.getoverhere.Model;

import com.google.firebase.Timestamp;

public class Driver extends  User{
    private String licenceNum;
    private String BankAccountNum;
    private float rank;


    public Driver() {
        super();
    }

    public Driver(String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay, String licenceNum, String bankAccountNum, float rank) {
        super(firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Driver");
        this.licenceNum = licenceNum;
        BankAccountNum = bankAccountNum;
        this.rank = rank;
    }

    public Driver(String userId, String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay, String licenceNum, String bankAccountNum, float rank) {
        super(userId, firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Driver");
        this.licenceNum = licenceNum;
        BankAccountNum = bankAccountNum;
        this.rank = rank;
    }

    public String getLicenceNum() {
        return licenceNum;
    }

    public void setLicenceNum(String licenceNum) {
        this.licenceNum = licenceNum;
    }

    public String getBankAccountNum() {
        return BankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        BankAccountNum = bankAccountNum;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }
}

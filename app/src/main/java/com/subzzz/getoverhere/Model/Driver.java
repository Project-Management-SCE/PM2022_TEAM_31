package com.subzzz.getoverhere.Model;

public class Driver extends  User{
    private String licenceNum;
    private String BankAccountNum;
    private float rank;

    public Driver() {
        super();
    }

    public Driver(String email, String firstName, String lastName, String userName, String idNum, String licenceNum, String bankAccountNum, float rank) {
        super(email, firstName, lastName, userName, idNum, "Driver");
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

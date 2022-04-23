package com.subzzz.getoverhere.Model;

import com.google.firebase.Timestamp;

public class Driver extends  User{
    private String licenceNum;
    private String licenceType;
    private Timestamp licenceIssueDate;
    private Timestamp licenceExprDate;
    private String licenceImgUrl;
    private String idImgUrl;
    private float rank;


    public Driver() {
        super();
    }

    public Driver(String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay, String licenceNum, String licenceType, Timestamp licenceIssueDate, Timestamp licenceExprDate, String licenceImgUrl, String idImgUrl, float rank) {
        super(firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Driver");
        this.licenceNum = licenceNum;
        this.licenceType = licenceType;
        this.licenceIssueDate = licenceIssueDate;
        this.licenceExprDate = licenceExprDate;
        this.licenceImgUrl = licenceImgUrl;
        this.idImgUrl = idImgUrl;
        this.rank = rank;
    }

    public Driver(String userId, String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay, String licenceNum, String licenceType, Timestamp licenceIssueDate, Timestamp licenceExprDate, String licenceImgUrl, String idImgUrl, float rank) {
        super(userId, firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Driver");
        this.licenceNum = licenceNum;
        this.licenceType = licenceType;
        this.licenceIssueDate = licenceIssueDate;
        this.licenceExprDate = licenceExprDate;
        this.licenceImgUrl = licenceImgUrl;
        this.idImgUrl = idImgUrl;
        this.rank = rank;
    }

    public String getLicenceNum() {
        return licenceNum;
    }

    public void setLicenceNum(String licenceNum) {
        this.licenceNum = licenceNum;
    }

    public String getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(String licenceType) {
        this.licenceType = licenceType;
    }

    public Timestamp getLicenceIssueDate() {
        return licenceIssueDate;
    }

    public void setLicenceIssueDate(Timestamp licenceIssueDate) {
        this.licenceIssueDate = licenceIssueDate;
    }

    public Timestamp getLicenceExprDate() {
        return licenceExprDate;
    }

    public void setLicenceExprDate(Timestamp licenceExprDate) {
        this.licenceExprDate = licenceExprDate;
    }

    public String getLicenceImgUrl() {
        return licenceImgUrl;
    }

    public void setLicenceImgUrl(String licenceImgUrl) {
        this.licenceImgUrl = licenceImgUrl;
    }

    public String getIdImgUrl() {
        return idImgUrl;
    }

    public void setIdImgUrl(String idImgUrl) {
        this.idImgUrl = idImgUrl;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }
}

package com.subzzz.getoverhere.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class DriverApplicant {
    private String Uid;
    private String idNum;
    private String fName;
    private String lName;
    private String email;
    private String licenceNum;
    private String licenceType;
    private Timestamp licenceIssueDate;
    private Timestamp licenceExprDate;
    private String licenceImgUrl;
    private String idImgUrl;

    public DriverApplicant() {
    }

    public DriverApplicant(String uid, String idNum, String fName, String lName, String email, String licenceNum, String licenceType, Timestamp licenceIssueDate, Timestamp licenceExprDate, String licenceImgUrl, String idImgUrl) {
        this.Uid = uid;
        this.idNum = idNum;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.licenceNum = licenceNum;
        this.licenceType = licenceType;
        this.licenceIssueDate = licenceIssueDate;
        this.licenceExprDate = licenceExprDate;
        this.licenceImgUrl = licenceImgUrl;
        this.idImgUrl = idImgUrl;
    }


    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}

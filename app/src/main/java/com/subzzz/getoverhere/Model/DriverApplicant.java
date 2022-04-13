package com.subzzz.getoverhere.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverApplicant implements Parcelable {
    private String Uid;
    private String idNum;
    private String fName;
    private String lName;
    private String email;
    private String licenceFilePath;
    private String idFilePath;

    public DriverApplicant(){}

    public DriverApplicant(String uid, String idNum, String fName, String lName, String email, String licenceFilePath, String idFilePath) {
        Uid = uid;
        this.idNum = idNum;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.licenceFilePath = licenceFilePath;
        this.idFilePath = idFilePath;
    }

    protected DriverApplicant(Parcel in) {
        Uid = in.readString();
        idNum = in.readString();
        fName = in.readString();
        lName = in.readString();
        email = in.readString();
        licenceFilePath = in.readString();
        idFilePath = in.readString();
    }

    public static final Creator<DriverApplicant> CREATOR = new Creator<DriverApplicant>() {
        @Override
        public DriverApplicant createFromParcel(Parcel in) {
            return new DriverApplicant(in);
        }

        @Override
        public DriverApplicant[] newArray(int size) {
            return new DriverApplicant[size];
        }
    };

    public String getLicenceFilePath() {
        return licenceFilePath;
    }

    public void setLicenceFilePath(String licenceFilePath) {
        this.licenceFilePath = licenceFilePath;
    }

    public String getIdFilePath() {
        return idFilePath;
    }

    public void setIdFilePath(String idFilePath) {
        this.idFilePath = idFilePath;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Uid);
        parcel.writeString(idNum);
        parcel.writeString(fName);
        parcel.writeString(lName);
        parcel.writeString(email);
        parcel.writeString(licenceFilePath);
        parcel.writeString(idFilePath);
    }
}

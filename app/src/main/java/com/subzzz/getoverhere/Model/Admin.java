package com.subzzz.getoverhere.Model;

import com.google.firebase.Timestamp;

public class Admin extends User{
    public Admin() {
        super();
    }

    public Admin(String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay) {
        super(firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Admin");
    }

    public Admin(String userId, String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay) {
        super(userId, firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Admin");
    }

}

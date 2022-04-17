package com.subzzz.getoverhere.Model;

import com.google.firebase.Timestamp;

public class Passenger extends User{


    public Passenger() {
        super();
    }

    public Passenger(String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay) {
        super(firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Passenger");
    }

    public Passenger(String userId, String firstName, String lastName, String email, String phoneNum, String idNum, String address, String gender, Timestamp birthDay) {
        super(userId, firstName, lastName, email, phoneNum, idNum, address, gender, birthDay, "Passenger");
    }
}

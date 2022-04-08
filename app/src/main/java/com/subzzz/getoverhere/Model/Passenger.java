package com.subzzz.getoverhere.Model;

public class Passenger extends User{


    public Passenger() {
        super();
    }

    public Passenger(String userId, String email, String firstName, String lastName, String userName, String idNum) {
        super(userId, email, firstName, lastName, userName, idNum, "Passenger");
    }

    public Passenger(String email, String firstName, String lastName, String userName, String idNum) {
        super(email, firstName, lastName, userName, idNum, "Passenger");
    }
}

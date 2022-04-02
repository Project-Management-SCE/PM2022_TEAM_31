package com.subzzz.getoverhere.Model;

public class Admin extends User{
    public Admin() {
        super();
    }

    public Admin(String email, String firstName, String lastName, String userName, String idNum) {
        super(email, firstName, lastName, userName, idNum, "Admin");
    }
}

package com.subzzz.getoverhere.Util;

import android.app.Application;

import com.subzzz.getoverhere.Model.User;

public class UserApi extends Application {
    private User currentUser;
    private String UType;
    private static UserApi instance;

    public static UserApi getInstance(){
        if(instance == null){
            instance = new UserApi();
        }
        return instance;
    }

    public String getUType() {
        return UType;
    }

    public void setUType(String UType) {
        this.UType = UType;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}

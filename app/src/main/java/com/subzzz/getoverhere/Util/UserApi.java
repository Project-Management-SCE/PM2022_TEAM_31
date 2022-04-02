package com.subzzz.getoverhere.Util;

import android.app.Application;

import com.subzzz.getoverhere.Model.User;

public class UserApi extends Application {
    private User currentUser;
    private static UserApi instance;

    public static UserApi getInstance(){
        if(instance == null){
            instance = new UserApi();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}

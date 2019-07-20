package com.example.ttapp.db;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class UserProfile {
    private int image;
    private String userName;
    private UserGame userGame;

    public UserProfile(){
    }

    public UserProfile(int image, String userName) {
        this.image = image;
        this.userName = userName;
    }

    public UserProfile(String userName,UserGame userGame){

        this.userName=userName;
        this.userGame=userGame;
    }

    public UserGame getUserGame() {
        return userGame;
    }

    public void setUserGame(UserGame userGame) {
        this.userGame = userGame;
    }

    public UserProfile(String userName) {
        this.userName = userName;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

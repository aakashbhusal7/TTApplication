package com.example.ttapp.db;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserGame {
    private String day;
    private String matches;
    private String wins;

    public UserGame(){}

    public UserGame(String day, String matches, String wins) {
        this.day = day;
        this.matches = matches;
        this.wins = wins;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }
}

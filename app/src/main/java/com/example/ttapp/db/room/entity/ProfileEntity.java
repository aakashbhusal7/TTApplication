package com.example.ttapp.db.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile_table")
public class ProfileEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "email id")
    private String emailId;

    @ColumnInfo(name="day")
    private String day;

    @ColumnInfo(name = "matches")
    private String matches;

    @ColumnInfo(name = "wins")
    private String wins;

    @Ignore
    public ProfileEntity(int id, String emailId,String day,String matches,String wins){
        this.id=id;
        this.emailId=emailId;
        this.day=day;
        this.matches=matches;
        this.wins=wins;
    }

    public ProfileEntity(String emailId, String day, String matches, String wins) {
        this.emailId = emailId;
        this.day = day;
        this.matches = matches;
        this.wins = wins;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

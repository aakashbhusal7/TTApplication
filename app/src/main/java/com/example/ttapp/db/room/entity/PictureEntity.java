package com.example.ttapp.db.room.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "picture_table")

public class PictureEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String imageUri;

    @Ignore
    public PictureEntity(int id, String imageUri) {
        this.id = id;
        this.imageUri = imageUri;
    }

    public PictureEntity(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

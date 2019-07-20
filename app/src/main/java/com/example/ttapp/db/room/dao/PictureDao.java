package com.example.ttapp.db.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ttapp.db.room.entity.PictureEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PictureDao {

    @Query("SELECT * FROM picture_table ORDER BY id ASC")
    Flowable<List<PictureEntity>>getPicture();

    @Insert
    long insertInfo(PictureEntity pictureEntity);
}

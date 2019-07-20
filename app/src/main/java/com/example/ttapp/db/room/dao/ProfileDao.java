package com.example.ttapp.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ttapp.db.room.entity.ProfileEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM profile_table ORDER BY id ASC")
    Flowable<List<ProfileEntity>>getAllInfo();

    @Query("SELECT * FROM profile_table WHERE id=:id")
    ProfileEntity getInfoById(int id);

    @Insert
    long insertInfo(ProfileEntity profileEntity);

    @Update
    void updateInfo(ProfileEntity profileEntity);

    @Delete
    void deleteProfile(ProfileEntity profileEntity);

    @Query("DELETE FROM profile_table")
    void deleteAll();
}
